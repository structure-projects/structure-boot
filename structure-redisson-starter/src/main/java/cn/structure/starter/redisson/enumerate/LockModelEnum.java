package cn.structure.starter.redisson.enumerate;

/**
 * <p>
 * 锁模式枚举
 * </P>
 */
public enum LockModelEnum {
    /**
     * 可重入锁
     */
    REENTRANT,
    /**
     * 公平锁
     */
    FAIR,
    /**
     * 联锁
     */
    MULTIPLE,
    /**
     * 红锁
     */
    REDLOCK,
    /**
     * 读锁
     */
    READ,
    /**
     * 写锁
     */
    WRITE,
    /**
     * 自动模式,当参数只有一个.使用 REENTRANT 参数多个 MULTIPLE
     */
    AUTO
}
