# Structure Redisson Starter

Structure Redisson Starter 是 Structure Boot 框架的 Redisson 分布式锁和缓存模块，提供基于 Redis 的分布式锁、分布式集合和缓存注解支持。

## 功能特性

- **分布式锁**: 支持多种锁模式（可重入锁、公平锁、联锁、红锁等）
- **缓存注解**: 提供声明式缓存注解
- **分布式集合**: 支持分布式List、Map等集合操作
- **自动配置**: 自动配置Redisson客户端
- **多种部署模式**: 支持单节点、哨兵、集群等模式

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-redisson-starter</artifactId>
    <version>${version}</version>
</dependency>
```

### 2. 配置Redisson

在 `application.yml` 中添加配置：

```yaml
structure:
  redisson:
    enabled: true
    # 单节点配置
    single-server:
      address: redis://localhost:6379
      password:
      database: 0
    # 缓存配置
    cache:
      enabled: true
      # 缓存过期时间（秒）
      expire: 3600
```

### 3. 使用分布式锁

```java
@Service
public class MyService {
    
    @Autowired
    private IDistributedLocker distributedLocker;
    
    public void doSomething() {
        // 获取锁
        RLock lock = distributedLocker.lock("my-lock");
        try {
            // 业务逻辑
        } finally {
            // 释放锁
            distributedLocker.unlock(lock);
        }
    }
}
```

### 4. 使用缓存注解

```java
@Service
public class UserService {
    
    @RCache(key = "user:{#id}", expire = 3600)
    public User getUser(Long id) {
        // 从数据库查询
        return userRepository.findById(id).orElse(null);
    }
    
    @WCache(key = "user:{#user.id}")
    public User saveUser(User user) {
        return userRepository.save(user);
    }
    
    @CList(key = "user:list")
    public List<User> getUserList() {
        return userRepository.findAll();
    }
}
```

## 目录结构

```
structure-redisson-starter/
├── src/main/java/cn/structure/starter/redisson/
│   ├── anno/               # 注解定义
│   │   ├── CList.java      # 缓存List注解
│   │   ├── CMap.java       # 缓存Map注解
│   │   ├── CTime.java      # 缓存时间注解
│   │   ├── Lock.java       # 分布式锁注解
│   │   ├── RCache.java     # 只读缓存注解
│   │   ├── RCacheMap.java  # 缓存Map注解
│   │   ├── RListCache.java # List缓存注解
│   │   ├── RMapAllCache.java # 全量Map缓存注解
│   │   └── WCache.java     # 写缓存注解
│   ├── aop/                # AOP切面
│   │   ├── LockAop.java    # 分布式锁切面
│   │   └── RedisCacheAop.java # 缓存切面
│   ├── configuration/      # 配置类
│   │   └── RedissonConfiguration.java
│   ├── enumerate/          # 枚举类型
│   │   └── LockModelEnum.java # 锁模式枚举
│   ├── exception/          # 异常处理
│   │   └── LockException.java # 锁异常
│   ├── properties/         # 配置属性
│   │   ├── CacheProperties.java
│   │   ├── ClusterProperties.java
│   │   ├── MasterSlaveProperties.java
│   │   ├── MultipleServerProperties.java
│   │   ├── RedissonProperties.java
│   │   ├── ReplicatedProperties.java
│   │   ├── SentinelProperties.java
│   │   └── SingleServerProperties.java
│   └── utils/              # 工具类
│       ├── DistributedLockerImpl.java
│       ├── IDistributedLocker.java
│       └── StringUtil.java
```

## 缓存注解

| 注解 | 说明 |
| :--- | :--- |
| `@RCache` | 只读缓存，方法返回值存入缓存 |
| `@WCache` | 写缓存，方法执行后更新缓存 |
| `@CList` | 缓存List集合 |
| `@CMap` | 缓存Map集合 |
| `@RListCache` | 缓存Redis List |
| `@RMapAllCache` | 缓存Redis Map（全量） |
| `@CTime` | 指定缓存过期时间 |

## 分布式锁模式

| 模式 | 说明 |
| :--- | :--- |
| `REENTRANT` | 可重入锁（默认） |
| `FAIR` | 公平锁 |
| `MULTI` | 联锁 |
| `RED_LOCK` | 红锁 |
| `READ_WRITE` | 读写锁 |

## 配置项说明

### 单节点配置

```yaml
structure:
  redisson:
    single-server:
      address: redis://localhost:6379
      password:
      database: 0
      connection-pool-size: 64
      connection-min-idle-size: 10
```

### 哨兵配置

```yaml
structure:
  redisson:
    sentinel:
      master-name: mymaster
      sentinel-addresses:
        - redis://localhost:26379
        - redis://localhost:26380
```

### 集群配置

```yaml
structure:
  redisson:
    cluster:
      node-addresses:
        - redis://localhost:7000
        - redis://localhost:7001
        - redis://localhost:7002
```

## 使用示例

### 分布式锁注解

```java
@Service
public class OrderService {
    
    @Lock(key = "order:{#orderId}", model = LockModelEnum.FAIR)
    public void createOrder(Long orderId) {
        // 创建订单逻辑
    }
}
```

### 缓存注解组合使用

```java
@Service
public class ProductService {
    
    @RCache(key = "product:{#id}")
    @CTime(expire = 7200)
    public Product getProduct(Long id) {
        return productRepository.findById(id).orElse(null);
    }
    
    @WCache(key = "product:{#product.id}")
    @CList(key = "product:list")
    public Product updateProduct(Product product) {
        return productRepository.save(product);
    }
}
```

## 许可证

Apache License 2.0