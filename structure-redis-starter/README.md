# Structure Redis Starter

Structure Redis Starter 是 Structure Boot 框架的 Redis 集成模块，基于 `spring-boot-starter-data-redis` 封装，提供分布式锁等增强功能。

## 功能特性

- **自动配置**: 自动配置 RedisTemplate 和 StringRedisTemplate
- **分布式锁**: 基于 Redis 实现的分布式锁
- **Lua脚本**: 使用 Lua 脚本保证原子性
- **锁超时**: 支持锁超时自动释放
- **灵活配置**: 支持多种 Redis 配置选项

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-redis-starter</artifactId>
    <version>${version}</version>
</dependency>
```

### 2. 配置Redis

在 `application.yml` 中添加配置：

```yaml
spring:
  redis:
    host: localhost
    port: 6379
    password:
    database: 0

structure:
  redis:
    lock:
      enabled: true
      # 默认锁超时时间（毫秒）
      default-timeout: 30000
      # 锁等待时间（毫秒）
      wait-time: 1000
```

### 3. 使用分布式锁

#### 方式一：使用注解

```java
@Service
public class OrderService {
    
    @RedisLock(key = "order:create:{#orderId}", timeout = 30000)
    public void createOrder(Long orderId) {
        // 创建订单逻辑
    }
}
```

#### 方式二：使用API

```java
@Service
public class InventoryService {
    
    @Autowired
    private IDistributedLock distributedLock;
    
    public void deductStock(Long productId, Integer quantity) {
        String lockKey = "stock:lock:" + productId;
        
        // 获取锁
        boolean locked = distributedLock.tryLock(lockKey, 30000, 1000);
        if (!locked) {
            throw new RuntimeException("系统繁忙，请稍后重试");
        }
        
        try {
            // 业务逻辑
            deductStockInternal(productId, quantity);
        } finally {
            // 释放锁
            distributedLock.unlock(lockKey);
        }
    }
}
```

## 目录结构

```
structure-redis-starter/
├── src/main/java/cn/structure/starter/redis/
│   ├── annotation/          # 注解定义
│   │   └── RedisLock.java
│   ├── configuration/       # 配置类
│   │   ├── DistributedLockAspectConfiguration.java
│   │   └── StructureRedisAutoConfiguration.java
│   └── lock/                # 锁实现
│       ├── IDistributedLock.java
│       └── RedisDistributedLockImpl.java
└── src/main/resources/
    ├── META-INF/
    │   ├── spring/
    │   │   └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
    │   └── spring.factories
    └── script/
        ├── lock.lua         # 加锁脚本
        └── unLock.lua       # 解锁脚本
```

## 分布式锁API

### IDistributedLock 接口

| 方法 | 说明 |
| :--- | :--- |
| `tryLock(String key, long timeout, long waitTime)` | 尝试获取锁 |
| `lock(String key, long timeout)` | 获取锁（阻塞） |
| `unlock(String key)` | 释放锁 |
| `tryLock(String key, long timeout)` | 尝试获取锁（无等待） |
| `isLocked(String key)` | 检查锁是否存在 |

### @RedisLock 注解

| 属性 | 类型 | 说明 | 默认值 |
| :--- | :--- | :--- | :--- |
| `key` | String | 锁的键（支持SpEL） | 必填 |
| `timeout` | long | 锁超时时间（毫秒） | 30000 |
| `waitTime` | long | 等待时间（毫秒） | 0 |

## Lua脚本

### 加锁脚本 (lock.lua)

```lua
if redis.call('setnx', KEYS[1], ARGV[1]) == 1 then
    redis.call('pexpire', KEYS[1], ARGV[2])
    return 1
else
    return 0
end
```

### 解锁脚本 (unLock.lua)

```lua
if redis.call('get', KEYS[1]) == ARGV[1] then
    return redis.call('del', KEYS[1])
else
    return 0
end
```

## 配置项说明

### Redis配置

| 配置项 | 类型 | 说明 | 默认值 |
| :--- | :--- | :--- | :--- |
| `spring.redis.host` | String | Redis服务器地址 | `localhost` |
| `spring.redis.port` | Integer | Redis端口 | `6379` |
| `spring.redis.password` | String | Redis密码 | - |
| `spring.redis.database` | Integer | 数据库索引 | `0` |

### 分布式锁配置

| 配置项 | 类型 | 说明 | 默认值 |
| :--- | :--- | :--- | :--- |
| `structure.redis.lock.enabled` | Boolean | 是否启用分布式锁 | `true` |
| `structure.redis.lock.default-timeout` | Long | 默认锁超时时间（毫秒） | `30000` |
| `structure.redis.lock.wait-time` | Long | 默认等待时间（毫秒） | `1000` |

## 使用示例

### 商品扣减库存

```java
@Service
public class StockService {
    
    @Autowired
    private IDistributedLock distributedLock;
    
    @Autowired
    private StringRedisTemplate redisTemplate;
    
    @RedisLock(key = "stock:lock:{#productId}", timeout = 30000)
    public void deductStock(Long productId, Integer quantity) {
        String stockKey = "stock:" + productId;
        
        // 获取当前库存
        String currentStock = redisTemplate.opsForValue().get(stockKey);
        int stock = Integer.parseInt(currentStock);
        
        if (stock < quantity) {
            throw new RuntimeException("库存不足");
        }
        
        // 扣减库存
        redisTemplate.opsForValue().decrement(stockKey, quantity);
    }
}
```

## 许可证

Apache License 2.0