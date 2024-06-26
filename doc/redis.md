# structure-redis
这个项目是对redis相关的封装
## 主要功能
- redis分布式锁进行了封装 structure-redis-starter 对spring-boot-starter-data-redis 启动器进行封装
## 使用方法 
### pom 引用 
```xml
        <dependency>
            <groupId>cn.structured</groupId>
            <artifactId>structure-redis-starter</artifactId>
            <version>${last.version}</version>
        </dependency>
```

### 使用分布式锁 ###
- 注解使用分布式锁
- 手动获取分布式锁
####  注解使用redis锁 参数为非对象的使用
```java
    /**
     * 注解使用redis锁 参数为非对象的使用
     * @param key
     */
    @RedisLock("#key")
    public void redisLock(String key){
        System.out.println("redisLock ----> key = " + key);
    }
```
####  注解使用redis锁 参数为对象的使用
```java
    /**
     * 注解使用redis锁 参数为对象的使用
     * @param redisLockBo
     */
    @RedisLock("#redisLockBo.key")
    public void redisLock(RedisLockBo redisLockBo) {
        System.out.println("redisLock ----> redisLockBo ----> key = " + redisLockBo.getKey());
    }
```
####  注解使用redis锁 多个key拼接的key
```java
    /**
     * 注解使用redis锁 多个key拼接的key
     * @param redisLockBo
     */
    @RedisLock("#redisLockBo.key:_#key")
    public void redisLock(RedisLockBo redisLockBo,String key) {
        System.out.println("redisLock ----> redisLockBo ----> key = " + redisLockBo.getKey()+ ":" + key);
    }
```
#### 手动获取分布式锁的方式 - 更为灵活
```java

    @Resource
    private IDistributedLock iDistributedLock;

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
```
### 案例 ###
[structure-redis-example](structure-redis-example/README.md)
