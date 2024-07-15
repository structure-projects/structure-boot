package cn.structure.example.redis.service;

import cn.structure.example.redis.entity.RedisLockBo;
import cn.structure.starter.redis.annotation.RedisLock;
import cn.structure.starter.redis.lock.IDistributedLock;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;


/**
 * redis Lock 的service
 *
 * @author chuck
 */
@Service
public class RedisLockService {

    @Resource
    private IDistributedLock iDistributedLock;

    /**
     * 注解使用redis锁 参数为非对象的使用
     *
     * @param key
     */
    @RedisLock("#key")
    public void redisLock(String key) {
        System.out.println("redisLock ----> key = " + key);
    }

    /**
     * 注解使用redis锁 参数为对象的使用
     *
     * @param redisLockBo
     */
    @RedisLock("#key")
    public void redisLock(RedisLockBo redisLockBo) {
        System.out.println("redisLock ----> redisLockBo ----> key = " + redisLockBo.getKey());
    }

    /**
     * 手动处理分布式锁
     */
    public void redisLock() {
        //redis - key
        String key = "123456";
        //获取锁
        boolean lock = iDistributedLock.lock(key);
        //判断是否获取到锁
        if (!lock) {
            return;
        }
        //todo 执行您的业务
        //释放锁
        iDistributedLock.releaseLock(key);
    }

}
