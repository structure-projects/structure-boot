package cn.structure.starter.redis.lock;

/**
 * <p>
 * 分布式锁
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 */
public interface IDistributedLock {
    long TIMEOUT_MILLIS = 30000;
    int RETRY_TIMES = Integer.MAX_VALUE;
    long SLEEP_MILLIS = 500;

    default boolean lock(String key) {
        return lock(key, TIMEOUT_MILLIS, RETRY_TIMES, SLEEP_MILLIS);
    }

    default boolean lock(String key, int retryTimes) {
        return lock(key, TIMEOUT_MILLIS, retryTimes, SLEEP_MILLIS);
    }

    default boolean lock(String key, int retryTimes, long sleepMillis) {
        return lock(key, TIMEOUT_MILLIS, retryTimes, sleepMillis);
    }

    default boolean lock(String key, long expire) {
        return lock(key, expire, RETRY_TIMES, SLEEP_MILLIS);
    }

    default boolean lock(String key, long expire, int retryTimes) {
        return lock(key, expire, retryTimes, SLEEP_MILLIS);
    }

    /**
     * <p>
     * 获取锁
     * </p>
     *
     * @param key         锁ID
     * @param expire      超时时间
     * @param retryTimes  重试次数
     * @param sleepMillis 重试的等待时间
     */
    boolean lock(String key, long expire, int retryTimes, long sleepMillis);

    /**
     * <p>
     * 释放锁
     * </p>
     *
     * @param key redisLock 的key
     */
    boolean releaseLock(String key);
}
