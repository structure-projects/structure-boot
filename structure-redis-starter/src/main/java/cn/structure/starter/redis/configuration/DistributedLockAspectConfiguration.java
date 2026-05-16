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
import lombok.extern.slf4j.Slf4j;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import java.lang.reflect.Method;

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

    @Pointcut("@annotation(cn.structure.starter.redis.annotation.RedisLock)")
    private void lockPoint() {

    }

    /**
     * <p>
     * 解析spel表达式获取redisLock 的key
     * </p>
     *
     * @param key            spel 表达式key入参
     * @param parameterNames 代理方法中的参数名称列表
     * @param values         代理方法中的参数值
     * @return 返回redisLock key
     */
    public static String getValueBySpelKey(String key, String[] parameterNames, Object[] values) {
        log.debug("[DistributedLockAspect] 解析SpEL表达式 - spelKey: {}, parameterNames: {}", key, parameterNames);
        //不存在表达式返回
        if (!key.contains("#")) {
            log.debug("[DistributedLockAspect] SpEL表达式不包含变量，直接返回 - key: {}", key);
            return key;
        }
        //使用下划线拆分表达式
        String[] spelKeys = key.split("_");
        //要返回的key
        StringBuilder sb = new StringBuilder();
        //遍历拆分结果用解析器解析
        for (int i = 0; i <= spelKeys.length - 1; i++) {
            if (!spelKeys[i].startsWith("#")) {
                sb.append(spelKeys[i]);
                continue;
            }
            String tempKey = spelKeys[i];
            //spel解析器
            ExpressionParser parser = new SpelExpressionParser();
            //spel上下文
            EvaluationContext context = new StandardEvaluationContext();
            for (int j = 0; j < parameterNames.length; j++) {
                context.setVariable(parameterNames[j], values[j]);
            }
            Expression expression = parser.parseExpression(tempKey);
            Object value = expression.getValue(context);
            if (value != null) {
                sb.append(value);
            }
        }
        String resultKey = sb.toString();
        log.debug("[DistributedLockAspect] SpEL表达式解析完成 - originalKey: {}, resultKey: {}", key, resultKey);
        //返回
        return resultKey;
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
        Method method = ((MethodSignature) pjp.getSignature()).getMethod();
        String className = pjp.getTarget().getClass().getName();
        String methodName = method.getName();
        RedisLock redisLock = method.getAnnotation(RedisLock.class);
        //获取参数名
        String[] parameterNames = new StandardReflectionParameterNameDiscoverer().getParameterNames(((MethodSignature) pjp.getSignature()).getMethod());
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
}
