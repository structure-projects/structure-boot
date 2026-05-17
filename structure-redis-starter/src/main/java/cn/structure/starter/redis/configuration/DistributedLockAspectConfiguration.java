/*
 * Copyright (c) 2025 Structure Boot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.structure.starter.redis.configuration;

import cn.structure.starter.redis.annotation.RedisLock;
import cn.structure.starter.redis.lock.IDistributedLock;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <p>
 * RedisLock代理实现类
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 */
@Slf4j
@Aspect
@Configuration
public class DistributedLockAspectConfiguration {

    @Resource
    private IDistributedLock distributedLock;

    private static final Pattern VARIABLE_PATTERN = Pattern.compile("#([a-zA-Z_][a-zA-Z0-9_.]*)");
    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    @Pointcut("@annotation(cn.structure.starter.redis.annotation.RedisLock)")
    private void lockPoint() {

    }

    /**
     * <p>
     * 解析spel表达式获取redisLock 的key
     * 支持格式：#key, #redisLockBo.key, #redisLockBo.key:_#key
     * </p>
     *
     * @param key            spel 表达式key入参
     * @param parameterNames 代理方法中的参数名称列表
     * @param values         代理方法中的参数值
     * @return 返回redisLock key
     */
    public static String getValueBySpelKey(String key, String[] parameterNames, Object[] values) {
        log.debug("[DistributedLockAspect] 解析SpEL表达式 - spelKey: {}, parameterNames: {}, values: {}", key, Arrays.toString(parameterNames), Arrays.toString(values));
        //不存在表达式返回
        if (!key.contains("#")) {
            log.debug("[DistributedLockAspect] SpEL表达式不包含变量，直接返回 - key: {}", key);
            return key;
        }

        try {
            //创建 SpEL 上下文并设置变量
            ExpressionParser parser = new SpelExpressionParser();
            StandardEvaluationContext context = new StandardEvaluationContext();
            for (int i = 0; i < parameterNames.length; i++) {
                //同时设置两种变量名：参数名和p0/p1/p2...，提高兼容性
                if (parameterNames[i] != null && !parameterNames[i].isEmpty()) {
                    context.setVariable(parameterNames[i], values[i]);
                }
                context.setVariable("p" + i, values[i]);
                log.debug("[DistributedLockAspect] 设置SpEL变量 - 参数名: {}, p{}: {}", parameterNames[i], i, values[i]);
            }

            //使用正则匹配变量并替换
            StringBuffer result = new StringBuffer();
            Matcher matcher = VARIABLE_PATTERN.matcher(key);
            while (matcher.find()) {
                String variable = matcher.group(1);
                try {
                    Object value = parser.parseExpression("#" + variable).getValue(context);
                    String replacement = value != null ? value.toString() : "";
                    matcher.appendReplacement(result, Matcher.quoteReplacement(replacement));
                } catch (Exception e) {
                    log.warn("[DistributedLockAspect] 解析变量失败 - variable: {}, error: {}", variable, e.getMessage());
                    matcher.appendReplacement(result, "");
                }
            }
            matcher.appendTail(result);
            
            String resultKey = result.toString();
            log.debug("[DistributedLockAspect] SpEL表达式解析完成 - originalKey: {}, resultKey: {}", key, resultKey);
            return resultKey;
        } catch (Exception e) {
            log.error("[DistributedLockAspect] SpEL表达式解析失败 - key: {}, error: {}", key, e.getMessage(), e);
            return key;
        }
    }

    /**
     * <p>
     * 增强代理 - redisLock的代理核心代理业务
     * </p>
     *
     * @param pjp 增强代理参数
     * @return Object
     */
    @Around("lockPoint()")
    public Object around(ProceedingJoinPoint pjp) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
        Method method = methodSignature.getMethod();
        String className = pjp.getTarget().getClass().getName();
        String methodName = method.getName();
        RedisLock redisLock = method.getAnnotation(RedisLock.class);

        //获取参数名 - 多重保障
        String[] parameterNames = resolveParameterNames(method, methodSignature);

        log.debug("[DistributedLockAspect] 获取方法参数名 - class: {}, method: {}, parameterNames: {}", className, methodName, Arrays.toString(parameterNames));
        //获取参数值
        Object[] args = pjp.getArgs();
        String key = getValueBySpelKey(redisLock.value(), parameterNames, args);
        int retryTimes = redisLock.action().equals(RedisLock.LockFailAction.CONTINUE) ? redisLock.retryTimes() : 0;
        long keepMills = redisLock.keepMills();
        long sleepMills = redisLock.sleepMills();

        log.info("[DistributedLockAspect] 尝试获取分布式锁 - class: {}, method: {}, key: {}, keepMills: {}, retryTimes: {}, sleepMills: {}",
                className, methodName, key, keepMills, retryTimes, sleepMills);

        boolean lock = distributedLock.lock(key, keepMills, retryTimes, sleepMills);
        if (!lock) {
            log.warn("[DistributedLockAspect] 获取分布式锁失败 - class: {}, method: {}, key: {}", className, methodName, key);
            return null;
        }
        //得到锁,执行方法，释放锁
        log.info("[DistributedLockAspect] 获取分布式锁成功 - class: {}, method: {}, key: {}", className, methodName, key);
        try {
            log.debug("[DistributedLockAspect] 执行加锁方法 - class: {}, method: {}, key: {}", className, methodName, key);
            Object result = pjp.proceed();
            log.debug("[DistributedLockAspect] 加锁方法执行完成 - class: {}, method: {}, key: {}", className, methodName, key);
            return result;
        } catch (Exception e) {
            log.error("[DistributedLockAspect] 执行加锁方法发生异常 - class: {}, method: {}, key: {}", className, methodName, key, e);
            throw e;
        } finally {
            log.debug("[DistributedLockAspect] 准备释放分布式锁 - class: {}, method: {}, key: {}", className, methodName, key);
            boolean releaseResult = distributedLock.releaseLock(key);
            if (releaseResult) {
                log.info("[DistributedLockAspect] 释放分布式锁成功 - class: {}, method: {}, key: {}", className, methodName, key);
            } else {
                log.warn("[DistributedLockAspect] 释放分布式锁失败 - class: {}, method: {}, key: {}", className, methodName, key);
            }
        }
    }

    /**
     * 尝试多种方式获取参数名
     */
    private String[] resolveParameterNames(Method method, MethodSignature methodSignature) {
        String[] parameterNames = null;
        int argCount = method.getParameterCount();

        // 1. 首先尝试使用MethodSignature
        try {
            parameterNames = methodSignature.getParameterNames();
            if (parameterNames != null && parameterNames.length == argCount) {
                log.debug("[DistributedLockAspect] 使用MethodSignature获取参数名成功");
                return parameterNames;
            }
        } catch (Exception e) {
            log.debug("[DistributedLockAspect] MethodSignature获取参数名失败: {}", e.getMessage());
        }

        // 2. 使用Spring的ParameterNameDiscoverer
        try {
            parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
            if (parameterNames != null && parameterNames.length == argCount) {
                log.debug("[DistributedLockAspect] 使用ParameterNameDiscoverer获取参数名成功");
                return parameterNames;
            }
        } catch (Exception e) {
            log.debug("[DistributedLockAspect] ParameterNameDiscoverer获取参数名失败: {}", e.getMessage());
        }

        // 3. 使用默认参数名
        parameterNames = new String[argCount];
        for (int i = 0; i < argCount; i++) {
            parameterNames[i] = "p" + i;
        }
        log.debug("[DistributedLockAspect] 使用默认参数名: {}", Arrays.toString(parameterNames));
        return parameterNames;
    }
}
