package cn.structure.example.redis.entity;



/**
 * <p>
 *     redis锁的测试实体
 * </p>
 * @author chuck
 */
public class RedisLockBo {

    private String key;

    private String type;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
