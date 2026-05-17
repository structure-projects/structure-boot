# structure-redis

这个项目是对 redis 相关的封装

## 主要功能

- redis 分布式锁进行了封装 structure-redis-starter 对 spring-boot-starter-data-redis 启动器进行封装

## Spring Boot 3.0 兼容性说明

本项目已适配 Spring Boot 3.0，SpEL 表达式解析支持两种写法：

1. **使用真实参数名**（推荐，需要 Maven 编译配置保留参数名）
2. **使用 p0/p1/p2... 索引形式**（兼容性更好，随时可用）

## 使用方法

### pom 引用

```xml
        <dependency>
            <groupId>cn.structured</groupId>
            <artifactId>structure-redis-starter</artifactId>
            <version>1.2.3</version>
        </dependency>
```

### 使用分布式锁

- 注解使用分布式锁
- 手动获取分布式锁

#### 注解使用 redis 锁 参数为非对象的使用

```java
    /**
     * 注解使用redis锁 参数为非对象的使用
     * @param key
     */
    @RedisLock("#key")  // 使用真实参数名
    // @RedisLock("#p0")  // 或使用索引形式，兼容性更好
    public void redisLock(String key){
        System.out.println("redisLock ----> key = " + key);
    }
```

#### 注解使用 redis 锁 参数为对象的使用

```java
    /**
     * 注解使用redis锁 参数为对象的使用
     * @param redisLockBo
     */
    @RedisLock("#redisLockBo.key")  // 使用真实参数名
    // @RedisLock("#p0.key")  // 或使用索引形式
    public void redisLock(RedisLockBo redisLockBo) {
        System.out.println("redisLock ----> redisLockBo ----> key = " + redisLockBo.getKey());
    }
```

#### 注解使用 redis 锁 多个 key 拼接的 key

```java
    /**
     * 注解使用redis锁 多个key拼接的key
     * @param redisLockBo
     * @param key
     */
    @RedisLock("#redisLockBo.key:_#key")  // 使用真实参数名
    // @RedisLock("#p0.key:_#p1")  // 或使用索引形式
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

## Maven 编译配置说明

为确保能正确获取参数名，请确保项目的 `pom.xml` 中 maven-compiler-plugin 配置了 `<parameters>true</parameters>`：

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <version>3.8.1</version>
    <configuration>
        <source>17</source>
        <target>17</target>
        <parameters>true</parameters>
    </configuration>
</plugin>
```

### 案例

[structure-redis-example](structure-redis-example/README.md)
