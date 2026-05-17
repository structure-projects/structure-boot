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
package cn.structure.starter.redisson.aop;

import cn.structure.starter.redisson.anno.*;
import cn.structure.starter.redisson.properties.CacheProperties;
import cn.structure.starter.redisson.properties.RedissonProperties;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.*;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.*;

import static cn.structure.starter.redisson.utils.StringUtil.getValueBySpelKey;

/**
 * <p>
 * Redis缓存Aop实现
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020-12-23
 */
@Slf4j
@Aspect
public class RedisCacheAop {

    @Resource
    private RedissonClient redissonClient;

    @Resource
    private RedissonProperties redissonProperties;

    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();

    /**
     * <p>
     * 写入缓存
     * </p>
     **/
    @Around("@annotation(wCache)")
    public Object writeCache(ProceedingJoinPoint proceedingJoinPoint, WCache wCache) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = methodSignature.getName();
        //获取参数名
        String[] parameterNames = resolveParameterNames(method, methodSignature);
        log.debug("[RedisCacheAop] 获取方法参数名 - class: {}, method: {}, parameterNames: {}", className, methodName, Arrays.toString(parameterNames));
        //获取参数值
        Object[] args = proceedingJoinPoint.getArgs();
        //1.0 获取Redis中的key
        String key = getKey(getValueBySpelKey(wCache.key(), parameterNames, args));
        //判断key是否无效
        if (key == null || key.isEmpty() || key.length() <= 0) {
            log.error("[RedisCacheAop] 写入缓存失败 - 无效的KEY - class: {}, method: {}", className, methodName);
            return null;
        }
        log.info("[RedisCacheAop] 开始写入缓存 - class: {}, method: {}, key: {}", className, methodName, key);
        //从方法中拿到返回结果
        Object proceed = proceedingJoinPoint.proceed();
        if (wCache.isObjCache()) {
            log.debug("[RedisCacheAop] 写入对象缓存 - key: {}", key);
            RBucket<Object> bucket = redissonClient.getBucket(key);
            //判断是否有时效
            if (null != wCache.time() && wCache.time().isTime()) {
                bucket.set(proceed, wCache.time().time(), wCache.time().timeType());
                log.debug("[RedisCacheAop] 对象缓存已设置过期时间 - key: {}, time: {}, timeType: {}", key, wCache.time().time(), wCache.time().timeType());
            } else {
                //放入redis中
                bucket.set(proceed);
                log.debug("[RedisCacheAop] 对象缓存已写入 - key: {}", key);
            }
        }
        //判断是是否有list类表更新list列表
        if (null != wCache.list() && wCache.list().isList()) {
            //集合的key
            String listKeyName = getValueBySpelKey(getKey(wCache.list().listKeyName()), parameterNames, args);
            log.info("[RedisCacheAop] 写入缓存列表 - listKey: {}, type: {}", listKeyName, wCache.list().value());
            Object data = proceed;
            //更新集合key
            if (wCache.list().value() == CList.ListType.KEY) {
                data = key;
            } else if (wCache.list().value() == CList.ListType.MAP) {
                data = getValueBySpelKey(wCache.map().mapKey(), parameterNames, args);
            }
            updateCacheList(listKeyName, data, wCache.list().size(), wCache.list().time());
        }
        //判断存储map
        if (null != wCache.map() && wCache.map().isMap()) {
            //mapKey
            String mapKey = getKey(getValueBySpelKey(wCache.map().mapKey(), parameterNames, args));
            String subMapKey = getValueBySpelKey(wCache.key(), parameterNames, args);
            //更新map緩存
            updateMapCache(mapKey, subMapKey, proceed, wCache.map().time());
        }
        log.info("[RedisCacheAop] 写入缓存完成 - class: {}, method: {}, key: {}", className, methodName, key);
        return proceed;
    }

    /**
     * <p>
     * 更新map缓存
     * </p>
     *
     * @author chuck
     **/
    private void updateMapCache(String mapKey, String key, Object data, CTime time) {
        log.debug("[RedisCacheAop] 更新Map缓存 - mapKey: {}, key: {}", mapKey, key);
        RMap<String, Object> map = redissonClient.getMap(mapKey);
        if (time.isTime()) {
            map.expire(time.time(), time.timeType());
            log.debug("[RedisCacheAop] Map缓存已设置过期时间 - mapKey: {}, time: {}, timeType: {}", mapKey, time.time(), time.timeType());
        }
        map.put(key, data);
        log.debug("[RedisCacheAop] Map缓存已更新 - mapKey: {}, key: {}", mapKey, key);
    }

    /**
     * <p>
     * 更新缓存列表
     * </p>
     **/
    private void updateCacheList(String listKey, Object data, int size, CTime time) {
        log.debug("[RedisCacheAop] 更新缓存列表 - listKey: {}, data: {}", listKey, data);
        //从redis中获取
        RList<Object> keyList = redissonClient.getList(listKey);
        if (time.isTime()) {
            keyList.expire(time.time(), time.timeType());
            log.debug("[RedisCacheAop] 缓存列表已设置过期时间 - listKey: {}, time: {}, timeType: {}", listKey, time.time(), time.timeType());
        }
        if (!keyList.contains(data)) {
            keyList.add(data);
            //判断溢出出队
            if (keyList.size() >= size) {
                keyList.remove(0);
                log.debug("[RedisCacheAop] 缓存列表已满，移除最早元素 - listKey: {}, size: {}", listKey, size);
            }
        }
        log.debug("[RedisCacheAop] 缓存列表更新完成 - listKey: {}", listKey);
    }

    /**
     * <p>
     * 读列表缓存
     * </p>
     **/
    @Around("@annotation(rListCache)")
    public Object readListCache(ProceedingJoinPoint proceedingJoinPoint, RListCache rListCache) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = methodSignature.getName();
        //获取参数名
        String[] parameterNames = resolveParameterNames(method, methodSignature);
        log.debug("[RedisCacheAop] 获取方法参数名 - class: {}, method: {}, parameterNames: {}", className, methodName, Arrays.toString(parameterNames));
        //获取参数值
        Object[] args = proceedingJoinPoint.getArgs();
        //key
        String key = getKey(getValueBySpelKey(rListCache.key(), parameterNames, args));
        //page
        int page = Integer.parseInt(getValueBySpelKey(rListCache.page(), parameterNames, args));
        //size
        int size = Integer.parseInt(getValueBySpelKey(rListCache.size(), parameterNames, args));
        log.info("[RedisCacheAop] 开始读取列表缓存 - class: {}, method: {}, key: {}, page: {}, size: {}, type: {}", className, methodName, key, page, size, rListCache.value());
        //获取redis
        if (rListCache.value() == CList.ListType.KEY) {
            RList<String> list = redissonClient.getList(key);
            //获取队列
            if (list != null && list.size() >= (page * size)) {
                int start = page * size - size;
                List<String> keys = list.readSortAlpha(rListCache.sort(), start, size);
                RBuckets buckets = redissonClient.getBuckets();
                String[] bKeys = new String[keys.size()];
                for (int i = 0; i < bKeys.length; i++) {
                    bKeys[i] = keys.get(i);
                }
                Map<String, Object> objectMap = buckets.get(bKeys);
                List<Object> objectList = new ArrayList<>();
                for (String mapKey : objectMap.keySet()) {
                    objectList.add(objectMap.get(mapKey));
                }
                log.info("[RedisCacheAop] 从缓存获取列表成功 - KEY类型, count: {}", objectList.size());
                return objectList;
            }
        } else if (rListCache.value() == CList.ListType.DATA) {
            RList<Object> list = redissonClient.getList(key);
            if (null != list && list.size() > (page * size)) {
                int start = page * size - size;
                List<Object> objectList = list.readSort(rListCache.sort(), start, size);
                log.info("[RedisCacheAop] 从缓存获取列表成功 - DATA类型, count: {}", objectList.size());
                return objectList;
            }
        } else if (rListCache.value() == CList.ListType.MAP) {
            RList<String> list = redissonClient.getList(key);
            if (null != list && list.size() > (page * size)) {
                int start = page * size - size;
                List<String> keys = list.readSortAlpha(rListCache.sort(), start, size);
                String mapKey = getValueBySpelKey(rListCache.mapKey(), parameterNames, args);
                RMap<Object, Object> map = redissonClient.getMap(mapKey);
                List<Object> objectList = new ArrayList<>();
                for (String objKey : keys) {
                    objectList.add(map.get(objKey));
                }
                log.info("[RedisCacheAop] 从缓存获取列表成功 - MAP类型, count: {}", objectList.size());
                return objectList;
            }
        }
        //队列的补偿操作请写在原方法上
        log.info("[RedisCacheAop] 缓存未命中，从数据库获取数据 - class: {}, method: {}", className, methodName);
        Object proceed = proceedingJoinPoint.proceed();
        log.info("[RedisCacheAop] 从数据库获取数据完成 - class: {}, method: {}", className, methodName);
        return proceed;
    }

    @Around("@annotation(rCache)")
    public Object readCache(ProceedingJoinPoint proceedingJoinPoint, RCache rCache) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = methodSignature.getName();
        //获取参数名
        String[] parameterNames = resolveParameterNames(method, methodSignature);
        log.debug("[RedisCacheAop] 获取方法参数名 - class: {}, method: {}, parameterNames: {}", className, methodName, Arrays.toString(parameterNames));
        //获取参数值
        Object[] args = proceedingJoinPoint.getArgs();
        //key
        String key = getKey(getValueBySpelKey(rCache.key(), parameterNames, args));
        log.info("[RedisCacheAop] 开始读取对象缓存 - class: {}, method: {}, key: {}", className, methodName, key);
        RBucket<Object> bucket = redissonClient.getBucket(key);
        //从redis获取对象
        Object obj = bucket.get();
        //判断是否为空
        if (null == obj) {
            log.info("[RedisCacheAop] 缓存未命中，从数据库获取数据 - class: {}, method: {}, key: {}", className, methodName, key);
            //如果空从方法中获取对象
            obj = proceedingJoinPoint.proceed();
            //并且存储到redis中 默认时长为 1 天
            bucket.set(obj, rCache.time(), rCache.timeType());
            log.info("[RedisCacheAop] 数据已写入缓存 - class: {}, method: {}, key: {}, time: {}, timeType: {}", className, methodName, key, rCache.time(), rCache.timeType());
        } else {
            log.info("[RedisCacheAop] 缓存命中 - class: {}, method: {}, key: {}", className, methodName, key);
        }
        log.info("[RedisCacheAop] 读取对象缓存完成 - class: {}, method: {}, key: {}", className, methodName, key);
        return obj;
    }

    @Around("@annotation(rMapCache)")
    public Object readMapCache(ProceedingJoinPoint proceedingJoinPoint, RCacheMap rMapCache) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = methodSignature.getName();
        //获取参数名
        String[] parameterNames = resolveParameterNames(method, methodSignature);
        log.debug("[RedisCacheAop] 获取方法参数名 - class: {}, method: {}, parameterNames: {}", className, methodName, Arrays.toString(parameterNames));
        //获取参数值
        Object[] args = proceedingJoinPoint.getArgs();
        //mapKey
        String mapKey = getKey(getValueBySpelKey(rMapCache.mapKey(), parameterNames, args));
        //map
        String key = getValueBySpelKey(rMapCache.key(), parameterNames, args);
        log.info("[RedisCacheAop] 开始读取Map缓存 - class: {}, method: {}, mapKey: {}, key: {}", className, methodName, mapKey, key);
        RMap<Object, Object> map = redissonClient.getMap(mapKey);
        Object object = map.get(key);
        if (null == object) {
            log.info("[RedisCacheAop] Map缓存未命中，从数据库获取数据 - class: {}, method: {}, mapKey: {}, key: {}", className, methodName, mapKey, key);
            if (rMapCache.isTime()) {
                map.expire(rMapCache.time(), rMapCache.timeType());
            }
            Object proceed = proceedingJoinPoint.proceed();
            map.put(key, proceed);
            if (rMapCache.list().isList()) {
                //获取listKey
                String listKey = getKey(getValueBySpelKey(rMapCache.list().listKeyName(), parameterNames, args));
                //更新list
                updateCacheList(listKey, key, rMapCache.list().size(), rMapCache.list().time());
            }
            log.info("[RedisCacheAop] Map缓存已更新 - class: {}, method: {}, mapKey: {}, key: {}", className, methodName, mapKey, key);
            return proceed;
        } else {
            log.info("[RedisCacheAop] Map缓存命中 - class: {}, method: {}, mapKey: {}, key: {}", className, methodName, mapKey, key);
        }
        return object;
    }

    @Around("@annotation(rMapAllCache)")
    public Object readMapAllCache(ProceedingJoinPoint proceedingJoinPoint, RMapAllCache rMapAllCache) throws Throwable {
        MethodSignature methodSignature = (MethodSignature) proceedingJoinPoint.getSignature();
        Method method = methodSignature.getMethod();
        String className = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = methodSignature.getName();
        //获取参数名
        String[] parameterNames = resolveParameterNames(method, methodSignature);
        log.debug("[RedisCacheAop] 获取方法参数名 - class: {}, method: {}, parameterNames: {}", className, methodName, Arrays.toString(parameterNames));
        //获取参数值
        Object[] args = proceedingJoinPoint.getArgs();
        //mapKey
        String mapKey = getKey(getValueBySpelKey(rMapAllCache.mapKey(), parameterNames, args));
        log.info("[RedisCacheAop] 开始读取Map全量缓存 - class: {}, method: {}, mapKey: {}", className, methodName, mapKey);
        RMap<String, Object> map = redissonClient.getMap(mapKey);
        if (map.size() <= 0) {
            log.info("[RedisCacheAop] Map全量缓存未命中，从数据库获取数据 - class: {}, method: {}, mapKey: {}", className, methodName, mapKey);
            if (rMapAllCache.time().isTime()) {
                map.expire(rMapAllCache.time().time(), rMapAllCache.time().timeType());
            }
            Object proceed = proceedingJoinPoint.proceed();
            Map<String, Object> addMap = new HashMap<>();
            if (proceed instanceof List) {
                List list = (List) proceed;
                for (Object obj : list) {
                    Field declaredField = obj.getClass().getDeclaredField(rMapAllCache.keyName());
                    declaredField.setAccessible(true);
                    Object key = declaredField.get(obj);
                    addMap.put(key.toString(), obj);
                }
            }
            map.putAll(addMap);
            log.info("[RedisCacheAop] Map全量缓存已更新 - class: {}, method: {}, mapKey: {}, count: {}", className, methodName, mapKey, addMap.size());
            return proceed;
        }
        List<Object> mapList = new ArrayList<>();
        //不是读部分的时候执行
        if (!rMapAllCache.keys().equals("")) {
            Set<String> keys = new HashSet<>();
            for (int i = 0; i < parameterNames.length; i++) {
                if (parameterNames[i].equals(rMapAllCache.keys())) {
                    keys.addAll((Collection) args[i]);
                }
            }
            Map<String, Object> mapAll = map.getAll(keys);
            for (String key : mapAll.keySet()) {
                mapList.add(mapAll.get(key));
            }
        } else {
            for (String key : map.keySet()) {
                mapList.add(map.get(key));
            }
        }
        log.info("[RedisCacheAop] Map全量缓存命中 - class: {}, method: {}, mapKey: {}, count: {}", className, methodName, mapKey, mapList.size());
        return mapList;
    }

    /**
     * <p>
     * 获取队列的KEY
     * </p>
     **/
    public String getKey(String key) {
        StringBuffer stringBuffer = new StringBuffer();
        CacheProperties cache = redissonProperties.getCache();
        if (null != cache) {
            String groupName = cache.getKeyGroupName();
            if (groupName != null && groupName.length() > 0) {
                stringBuffer.append(groupName);
                stringBuffer.append(":");
            }
        }
        stringBuffer.append(key);
        return stringBuffer.toString();
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
                log.debug("[RedisCacheAop] 使用MethodSignature获取参数名成功");
                return parameterNames;
            }
        } catch (Exception e) {
            log.debug("[RedisCacheAop] MethodSignature获取参数名失败: {}", e.getMessage());
        }

        // 2. 使用Spring的ParameterNameDiscoverer
        try {
            parameterNames = PARAMETER_NAME_DISCOVERER.getParameterNames(method);
            if (parameterNames != null && parameterNames.length == argCount) {
                log.debug("[RedisCacheAop] 使用ParameterNameDiscoverer获取参数名成功");
                return parameterNames;
            }
        } catch (Exception e) {
            log.debug("[RedisCacheAop] ParameterNameDiscoverer获取参数名失败: {}", e.getMessage());
        }

        // 3. 使用默认参数名
        parameterNames = new String[argCount];
        for (int i = 0; i < argCount; i++) {
            parameterNames[i] = "p" + i;
        }
        log.debug("[RedisCacheAop] 使用默认参数名: {}", Arrays.toString(parameterNames));
        return parameterNames;
    }
}
