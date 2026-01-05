# Structure Boot Starter 模块详细功能与扩展指南

## 🎯 设计哲学

Structure Boot的设计核心原则是：
1. **保持原生功能完整性** - 不破坏Spring Boot和第三方库的原生功能
2. **零配置开箱即用** - 提供合理的默认配置，一键启用
3. **高度可扩展性** - 在封装的基础上提供丰富的自定义扩展点
4. **配置灵活化** - 支持多种配置方式，满足不同场景需求
5. **非侵入式设计** - 对业务代码的侵入性降到最低

---

## 1. structure-restful-web-starter

### 🌟 核心功能

#### 1.1 统一API响应封装
**原生功能保持**: 完全保持Spring MVC的原有功能，只在Controller层提供统一的响应格式

**自动配置类**: `cn.structure.starter.web.restful.configuration.AutoConfiguration`

**配置类**: `RestfulWebConfigProperties` (前缀: `structure.web.restful`)

**使用方式**:
```java
// 原生使用方式 - 完全不受影响
@RestController
@RequestMapping("/native")
public class NativeController {
    @GetMapping("/test")
    public String test() {
        return "原生响应";
    }
}

// 使用统一响应封装
@RestController
@RequestMapping("/api")
public class ApiController {
    @GetMapping("/user/{id}")
    public CommonResult<User> getUser(@PathVariable Long id) {
        User user = userService.getById(id);
        return CommonResult.success(user);
    }
}
```

**配置选项**:
```yaml
structure:
  web:
    restful:
      # 预留扩展配置，目前使用默认配置
      enable: true  # 默认启用
      base-package: cn.structure  # 扫描包范围（预留）
```

#### 1.2 FastJson消息转换器
**特色设计**: 自动解决JavaScript精度丢失问题，Long类型转String

**配置扩展**:
```java
// 自定义FastJson配置
@Configuration
public class CustomFastJsonConfig {

    @Bean
    @ConditionalOnMissingBean
    public HttpMessageConverters customConverters() {
        FastJsonHttpMessageConverter converter = new FastJsonHttpMessageConverter();
        FastJsonConfig config = new FastJsonConfig();

        // 自定义序列化配置
        config.setSerializerFeatures(
            SerializerFeature.WriteMapNullValue,     // null值显示
            SerializerFeature.WriteNullStringAsEmpty, // null字符串显示为空
            SerializerFeature.DisableCircularReferenceDetect // 禁用循环引用
        );

        config.setDateFormat("yyyy-MM-dd HH:mm:ss");  // 自定义日期格式
        converter.setFastJsonConfig(config);

        return new HttpMessageConverters(converter);
    }
}
```

#### 1.3 Swagger文档集成
**注解启用**: 支持通过注解开启，不影响原生配置

**完全自定义配置**:
```java
// 方式1: 使用框架注解（简化配置）
@EnableSwagger  // 只添加这一个注解即可
@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// 方式2: 完全自定义Swagger配置（推荐，功能更强大）
@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(apiInfo())
            .select()
            .apis(RequestHandlerSelectors.basePackage("cn.structure.controller"))
            .paths(PathSelectors.any())
            .build()
            .directModelSubstitute(LocalDateTime.class, String.class) // 完全自定义
            .globalOperationParameters(Arrays.asList(
                new ParameterBuilder()
                    .name("Authorization")
                    .description("令牌")
                    .modelRef(new ModelRef("string"))
                    .parameterType("header")
                    .required(false)
                    .build()
            ));
    }
}
```

#### 1.4 全局异常处理
**并保持原生异常**: 支持原生异常抛出，框架只做兜底处理

**自定义异常配置**:
```java
// 原生方式 - 完全支持
@RestControllerAdvice
public class NativeExceptionHandler {
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<String> handleBusinessException(BusinessException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

// 扩展全局异常 - 增加业务异常类型
@Component
public class CustomGlobalExceptionHandler {

    @ExceptionHandler(YourBusinessException.class)
    public CommonResult handleYourException(YourBusinessException e) {
        log.error("业务异常: {}", e.getMessage(), e);
        return CommonResult.failed(e.getCode(), e.getMessage());
    }

    // 还可以添加更多自定义异常...
}
```

#### 1.5 扩展点总结
| 扩展点 | 实现方式 | 说明 |
|--------|----------|------|
| 自定义响应格式 | 实现 `IResultUtil` 接口 | 替换默认的CommonResult |
| 自定义消息转换器 | 配置 `HttpMessageConverters` Bean | 保留或覆盖FastJson配置 |
| 自定义Swagger配置 | 使用原生 `@EnableSwagger2` | 完全自定义，不受框架限制 |
| 扩展异常处理 | 添加 `@ControllerAdvice` | 多层次异常拦截 |
| CORS跨域配置 | 原生配置 `WebMvcConfigurer` | 完全支持原生配置 |

---

## 2. structure-mybatis-plus-starter

### 🌟 核心功能

#### 2.1 Snowflake分布式ID生成器
**智能workerId分配**: 基于IP地址自动生成，零配置部署

**完全自定义配置**:
```yaml
structure:
  snowflake:
    # 高性能ID生成器配置
    worker-id: 1          # 工作机ID (0-31)，不配置则自动获取
    datacenter-id: 1      # 数据中心ID (0-31)，不配置则自动获取
    # 可选: 时钟回拨处理策略
    clock-backward-seconds: 1  # 允许的最大时钟回拨秒数
```

**自定义Snowflake实现**:
```java
// 完全替换框架的Snowflake实现
@Component
@ConditionalOnMissingBean(Snowflake.class)
public class CustomSnowflake implements Snowflake {

    // 你的自定义实现
    @Override
    public synchronized Long nextId() {
        // 自定义ID生成逻辑
        return generateCustomId();
    }

    @Override
    public String nextIdStr() {
        return String.valueOf(nextId());
    }
}
```

#### 2.2 MyBatis Plus增强功能
**保持原生功能**: 所有MyBatis Plus原生功能完全保留

##### 2.2.1 基础Mapper扩展
**IBaseMapper接口**提供强化功能:
```java
// 原生使用方式 - 完全支持
@Mapper
public interface UserMapper extends BaseMapper<User> {
    // MyBatis Plus原生方法完全保留
}

// 增强使用方式 - 功能扩展
@Mapper
public interface UserMapper extends IBaseMapper<User> {

    // 新增: 批量插入（性能优化）
    int insertList(List<User> userList);

    // 新增: 关联查询分页
    IPage<UserVO> selectJoinPageList(IPage<UserVO> page,
                                   @Param(Constants.WRAPPER) Wrapper<UserVO> queryWrapper);

    // 新增: 逻辑删除完善
    int logicDeleteById(@Param("id") Long id,
                       @Param("updateUser") Long updateUser);
}
```

**配置灵活性**:
```yaml
# MyBatis Plus原生配置 - 完全支持
mybatis-plus:
  configuration:
    map-underscore-to-camel-case: true
    cache-enabled: true
    log-impl: org.apache.ibatis.logging.stdout.StdOutImpl  # SQL打印控制台
  global-config:
    db-config:
      id-type: ASSIGN_ID           # ID策略
      logic-delete-field: deleted  # 逻辑删除字段
      logic-delete-value: 1        # 删除值
      logic-not-delete-value: 0    # 未删除值
  mapper-locations: classpath:mapper/**/*.xml  # XML映射文件位置
```

##### 2.2.2 关联查询注解系统
**注解驱动配置** - 替代复杂的XML配置:
```java
@Entity
@Table(name = "t_user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @FieldJoin(
        table = "t_department",     // 关联表
        field = "name",             // 关联字段
        joinField = "department_id", // 本表关联字段
        alias = "department_name",   // 查询别名
        joinType = JoinType.LEFT     // 关联类型
    )
    private String departmentName;  // 非数据库字段，自动关联查询

    @Where(
        column = "status",           // 条件字段
        condition = "= #{status}",   // 条件表达式
        test = "status != null"      // 条件测试
    )
    private Integer status;         // 查询条件
}

// 使用示例
@Test
public void testComplexQuery() {
    // 自动关联查询，支持复杂条件
    List<UserVO> users = userMapper.selectJoinList(new QueryWrapper<User>()
        .like("name", "张%")
        .eq("status", 1));
}
```

**支持的注解配置**:
| 注解 | 功能 | 配置项 |
|------|------|--------|
| `@FieldJoin` | 字段级关联 | table, field, joinField, alias, joinType |
| `@Join` | 表级关联 | table, alias, joinType, on条件 |
| `@Keyword` | 模糊查询 | column, operator, test条件 |
| `@Where` | 自定义条件 | column, condition, test条件 |
| `@DateTime` | 时间查询 | format, timezone |
| `@LogicDelete` | 逻辑删除 | deletedValue, notDeletedValue |

##### 2.2.3 代码生成器集成
**完全自定义的代码生成**:
```java
// 方式1: 使用框架默认配置
@GetMapping("/generate")
public CommonResult generate() {
    return CommonResult.success("使用默认配置生成代码");
}

// 方式2: 完全自定义生成规则
Configuration config = new Configuration();
config.setAuthor("your-name");
config.setOutputDir("src/main/java");
config.setParent("cn.structure.module");
config.setEntityLombokModel(true);  // Lombok
config.setRestControllerStyle(true); // RestController
config.setControllerMappingHyphenStyle(true); // 连字符映射
config.setTablePrefix("t_");        // 表前缀
config.setNaming(NamingStrategy.underline_to_camel); // 命名策略

// 数据库配置
dataSource.setUrl("jdbc:mysql://localhost:3306/test");
dataSource.setDriverName("com.mysql.cj.jdbc.Driver");
dataSource.setUsername("root");
dataSource.setPassword("123456");

// 自定义模板（可选）
Map<String, String> customTemplates = new HashMap<>();
customTemplates.put("controller.java.vm", "custom/controller_template.vm");
customTemplates.put("service.java.vm", "custom/service_template.vm");
```

#### 2.3 高级配置扩展
##### 2.3.1 多数据源配置
**框架不限制原生多数据源使用**:
```java
// 原生方式 - 完全支持
@Configuration
@MapperScan(basePackages = "com.example.mapper.db1", sqlSessionTemplateRef = "db1SqlSessionTemplate")
public class DataSource1Config {

    @Bean("db1DataSource")
    @ConfigurationProperties(prefix = "spring.datasource.db1")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }
}

// 结合框架特性 - 支持增强功能
@Mapper
public interface UserMapper extends IBaseMapper<User> {
    // 框架功能在多数据源环境下完全工作
}
```

##### 2.3.2 分表分库支持
**可扩展的分表策略**:
```java
// 自定义分表规则
@Component
public class CustomShardingStrategy implements ITableShardingStrategy {

    @Override
    public String getShardingTableName(Class<?> entityClass, Object shardingKey) {
        // 根据分片key计算表名
        int tableIndex = calculateTableIndex(shardingKey);
        return entityClass.getAnnotation(TableName.class).value() + "_" + tableIndex;
    }

    private int calculateTableIndex(Object shardingKey) {
        return Math.abs(shardingKey.hashCode()) % 8; // 8张分表
    }
}

// 支持原生ShardingSphere等分库分表中间件 - 完全兼容
```

#### 2.4 扩展点总结
| 扩展点 | 实现方式 | 原生支持 |
|--------|----------|----------|
| 自定义Snowflake | 实现 `Snowflake` 接口 | ✅ 完全替换 |
| 增强Mapper功能 | 继承 `IBaseMapper<T>` | ✅ 向下兼容 |
| 关联查询注解 | 注解驱动配置 | ✅ 可选使用 |
| 代码生成器模板 | 自定义Velocity模板 | ✅ 完全自定义 |
| 分表分库策略 | 实现 `ITableShardingStrategy` | ✅ 兼容原生 |
| 多数据源配置 | 原生配置方式 | ✅ 完全支持 |
| MyBatis Plus配置 | 原生配置属性 | ✅ 全部支持 |

---

## 3. structure-redis-starter

### 🌟 核心功能

#### 3.1 分布式锁实现
**保持RedisTemplate原生功能**: 所有Redis操作完全保留

**智能分布式锁注解**:
```java
// 基本使用 - 简化配置
@RedisLock(value = "#user.id", keepMills = 30000)
public CommonResult saveUser(User user) {
    // 业务逻辑
    return CommonResult.success();
}

// 高级配置 - 完整控制
@RedisLock(
    value = "#{#order.userId + '_' + #order.productId}",  // 复合key
    keepMills = 10000,      // 锁最长持有时间
    sleepMills = 100,       // 重试间隔
    retryTimes = 5,         // 重试次数
    action = LockFailAction.GIVEUP) // 失败策略
public CommonResult processOrder(Order order) {
    // 订单处理逻辑 - 保证幂等性
    return CommonResult.success();
}

// 支持SPEL表达式 - 灵活配置
@RedisLock(
    value = "#{#type + '_' + #id}")  // 动态生成锁key
public void updateByType(String type, Long id) {
    // 更新逻辑
}
```

#### 3.2 配置灵活性
**原生Redis配置支持** - 所有Spring Boot Redis配置完全保留:
```yaml
# Spring Boot原生配置 - 完全支持
spring:
  redis:
    host: localhost
    port: 6379
    password: 123456
    database: 0
    timeout: 5000ms
    lettuce:
      pool:
        max-active: 8
        max-idle: 8
        min-idle: 0
        max-wait: -1ms

# Redis分布式锁配置 - 可选
structure:
  redis:
    lock:
      default-keep-mills: 30000  # 默认锁持有时间
      default-retry-times: 3     # 默认重试次数
      default-sleep-mills: 100   # 默认重试间隔
```

#### 3.3 分布式锁高级功能
##### 3.3.1 失败策略配置
```java
// 策略1: 放弃执行
@RedisLock(value = "#id", action = LockFailAction.GIVEUP)
public void giveupStrategy(Long id) {
    // 获取锁失败直接放弃，不执行方法
}

// 策略2: 继续执行（无锁）
@RedisLock(value = "#id", action = LockFailAction.CONTINUE)
public void continueStrategy(Long id) {
    // 获取锁失败但仍执行方法（记录日志等）
}
```

##### 3.3.2 自定义分布式锁实现
**完全替换框架实现**:
```java
// 自定义分布式锁实现
@Component
@ConditionalOnMissingBean(IDistributedLock.class)
public class CustomDistributedLock implements IDistributedLock {

    private final RedissonClient redissonClient;

    @Override
    public boolean tryLock(String key, long expireTime, TimeUnit unit) {
        RLock lock = redissonClient.getLock(key);
        try {
            return lock.tryLock(expireTime, unit);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
    }

    @Override
    public void unlock(String key) {
        RLock lock = redissonClient.getLock(key);
        if (lock.isHeldByCurrentThread()) {
            lock.unlock();
        }
    }
}
```

#### 3.4 扩展点总结
| 扩展点 | 实现方式 | SpEL支持 |
|--------|----------|----------|
| 锁实现替换 | 实现 `IDistributedLock` 接口 | N/A |
| Redis连接配置 | Spring Boot原生属性 | N/A |
| 锁超时配置 | 注解参数 + 配置文件 | ✅ |
| 锁key表达式 | SpEL表达式 | ✅ |
| 失败策略配置 | `LockFailAction` 枚举 | N/A |

---

## 4. structure-redisson-starter

### 🌟 核心功能

#### 4.1 多模式Redis部署支持
**完全支持Redisson原生配置**: 同时支持所有Redis架构模式

**配置方式对比**:
```yaml
# 模式1: 单节点模式 (开发环境)
structure:
  redisson:
    model: single
    single-server-config:
      address: redis://localhost:6379
      password: null
      database: 0
      connection-pool-size: 32
      connection-minimum-idle-size: 8

# 模式2: 哨兵模式 (高可用)
structure:
  redisson:
    model: sentinel
    sentinel-servers-config:
      master-name: mymaster
      sentinel-addresses:
        - redis://sentinel1:26379
        - redis://sentinel2:26379
        - redis://sentinel3:26379
      password: sentinel-password
      database: 0

# 模式3: 集群模式 (大数据量)
structure:
  redisson:
    model: cluster
    cluster-servers-config:
      node-addresses:
        - redis://127.0.0.1:7000
        - redis://127.0.0.1:7001
        - redis://127.0.0.1:7002
        - redis://127.0.0.1:7003
        - redis://127.0.0.1:7004
        - redis://127.0.0.1:7005
      password: cluster-password
      scan-interval: 1000

# 模式4: 主从模式 (读写分离)
structure:
  redisson:
    model: master-slave
    master-slave-servers-config:
      master-address: redis://master:6379
      slave-addresses:
        - redis://slave1:6379
        - redis://slave2:6379
      read-mode: SLAVE  # 读操作使用从节点
      subscription-mode: SLAVE  # 订阅使用从节点
      load-balancer: !<org.redisson.connection.balancer.RoundRobinLoadBalancer> {}
```

#### 4.2 三种客户端类型同时支持
**保持Redisson原生能力**: 所有客户端类型都完全支持

```java
// 注入方式1: 同步客户端 (常用)
@Autowired
private RedissonClient redissonClient;

// 注入方式2: 响应式客户端 (WebFlux集成)
@Autowired
private RedissonReactiveClient redissonReactiveClient;

// 注入方式3: RxJava客户端
@Autowired
private RedissonRxClient redissonRxClient;
```

#### 4.3 企业级高级配置
##### 4.3.1 SSL安全连接
```yaml
structure:
  redisson:
    ssl-enable: true
    ssl-provider: JDK  # 或 OPENSSL
    ssl-keystore: /path/to/keystore.jks
    ssl-keystore-password: keystore-password
    ssl-truststore: /path/to/truststore.jks
    ssl-truststore-password: truststore-password
    ssl-protocols: [TLSv1.2, TLSv1.3]
```

##### 4.3.2 连接池优化配置
```yaml
structure:
  redisson:
    # 连接池配置
    connection-pool-size: 64          # 连接池大小
    connection-minimum-idle-size: 16  # 最小空闲连接
    idle-connection-timeout: 10000    # 连接空闲超时
    connect-timeout: 10000            # 连接超时
    timeout: 3000                     # 命令超时
    retry-attempts: 3                 # 重试次数
    retry-interval: 1500              # 重试间隔

    # 性能优化
    threads: 16                       # 线程数
    netty-threads: 16                 # Netty线程数
    codec: !<org.redisson.codec.JsonJacksonCodec> {}  # 编解码器
```

##### 4.3.3 高性能场景配置
```yaml
structure:
  redisson:
    # 响应式编程支持
    reactive-mode: true

    # 管道模式优化
    pipeline-mode: true

    # 读/写分离策略
    read-mode: MASTER_SLAVE  # 读写分离
    subscription-mode: MASTER  # 订阅主节点
```

#### 4.4 分布式高级功能
##### 4.4.1 分布式锁增强
```java
// 方式1: 使用框架分布式锁（简化）
@Autowired
private IDistributedLocker distributedLocker;

public void businessMethod(String key) {
    RLock lock = distributedLocker.lock(key, 10, TimeUnit.SECONDS);
    try {
        // 业务逻辑
    } finally {
        distributedLocker.unlock(key);
    }
}

// 方式2: 使用Redisson原生功能（完全控制）
@Autowired
private RedissonClient redissonClient;

public void advancedLockExample(String key) {
    RLock fairLock = redissonClient.getFairLock(key);  // 公平锁
    RLock multiLock = redissonClient.getMultiLock(lock1, lock2, lock3); // 多重锁
    RLock redLock = redissonClient.getRedLock(lock1, lock2, lock3);    // 红锁

    // 读写锁
    RReadWriteLock rwLock = redissonClient.getReadWriteLock(key);
    RLock readLock = rwLock.readLock();
    RLock writeLock = rwLock.writeLock();

    // 信号量
    RSemaphore semaphore = redissonClient.getSemaphore(key);

    // 计数器
    RCountDownLatch latch = redissonClient.getCountDownLatch(key);
}
```

##### 4.4.2 分布式集合和映射
```java
// 分布式列表
RList<String> list = redissonClient.getList("distributed-list");

// 分布式映射
RMap<String, Object> map = redissonClient.getMap("distributed-map");

// 分布式队列
RQueue<String> queue = redissonClient.getQueue("distributed-queue");

// 分布式阻塞队列
RBlockingQueue<String> blockingQueue = redissonClient.getBlockingQueue("blocking-queue");
```

##### 4.4.3 缓存集成AOP
```java
// 基于注解的缓存使用
@Service
public class UserService {

    // 缓存穿透保护
    @Cacheable(value = "users", key = "#id")
    @CacheExpire(expireTime = 300, timeUnit = TimeUnit.SECONDS)  // 自定义过期时间
    public User getUserById(Long id) {
        return userMapper.selectById(id);
    }

    // 缓存更新
    @CacheEvict(value = "users", key = "#user.id")
    public void updateUser(User user) {
        userMapper.updateById(user);
    }

    // 条件缓存
    @Cacheable(value = "users", key = "#age", condition = "#age > 18")
    public List<User> getUsersByAge(Integer age) {
        return userMapper.selectList(new QueryWrapper<User>().eq("age", age));
    }
}
```

#### 4.5 完全自定义的RedissonClient
```java
// 完全替换框架的配置（最高级自定义）
@Configuration
public class CustomRedissonConfig {

    @Value("${custom.redisson.config}")
    private String configFile;

    @Bean(destroyMethod = "shutdown")
    @ConditionalOnMissingBean(RedissonClient.class)
    public RedissonClient customRedissonClient() throws IOException {
        // 方式1: 从JSON配置创建
        Config config = Config.fromJSON(new File(configFile));

        // 方式2: 完全编程式配置
        Config config = new Config();

        // 单节点配置
        config.useSingleServer()
            .setAddress("redis://127.0.0.1:6379")
            .setDatabase(0)
            .setPassword(null)
            .setConnectionPoolSize(64)
            .setConnectionMinimumIdleSize(16)
            .setKeepAlive(true)              // 保活机制
            .setTcpNoDelay(true)             // TCP_NODELAY
            .setIdleConnectionTimeout(10000)
            .setConnectTimeout(10000)
            .setTimeout(3000)
            .setRetryAttempts(3)
            .setRetryInterval(1500)
            .setSslEnable(true)              // SSL开启
            .setSslProvider(SslProvider.JDK)
            .setSslTruststore("/path/to/truststore.jks")
            .setSslTruststorePassword("password");

        // 编解码器配置
        config.setCodec(new JsonJacksonCodec());

        // 线程池配置
        config.setThreads(16);
        config.setNettyThreads(16);

        // 性能调优
        config.setReferenceEnabled(true);
        config.setReferenceCodec(new FstCodec());

        return Redisson.create(config);
    }

    // 创建响应式客户端
    @Bean(destroyMethod = "shutdown")
    public RedissonReactiveClient redissonReactiveClient(RedissonClient redissonClient) {
        return redissonClient.reactive();
    }
}
```

#### 4.6 扩展点总结
| 扩展级别 | 实现方式 | 适用场景 |
|----------|----------|----------|
| **配置级扩展** | YAML属性配置 | 参数调整，快速配置 |
| **注入级扩展** | @Autowired类型选择 | 同步/响应式/RxJava |
| **实现级扩展** | 实现IDistributedLocker | 分布式锁策略 |
| **完全级扩展** | 自定义RedissonClient Bean | 企业级复杂需求 |

---

## 5. structure-minio-starter

### 🌟 核心功能

#### 5.1 MinIO客户端自动配置
**保持MinIO原生功能**: 所有MinIO操作完全兼容

**多配置方式支持**:
```yaml
# 基础配置 - 单节点
structure:
  minio:
    url: http://localhost:9000             # MinIO服务器地址
    access-key: minioadmin                  # 访问密钥
    secret-key: minioadmin                  # 私钥
    expires-second: 604800                  # 签名URL过期时间（7天）
    endpoint-enable: true                   # 是否启用HTTP端点

# 高级配置 - 企业级应用
structure:
  minio:
    url: https://minio.example.com:9000     # HTTPS支持
    access-key: ${MINIO_ACCESS_KEY}         # 环境变量注入
    secret-key: ${MINIO_SECRET_KEY}
    expires-second: 3600                    # 1小时过期
    endpoint-enable: true
    secure: true                            # 启用SSL
    region: us-west-1                       # 区域设置
    ok-http:
      connect-timeout: 10000                # 连接超时
      read-timeout: 30000                   # 读取超时
      write-timeout: 30000                  # 写入超时
      follow-redirects: true                # 跟随重定向
```

#### 5.2 MinioTemplate模板操作
**简化常用操作** - 基于此可以继续扩展:
```java
@RestController
@RequestMapping("/api/file")
public class FileController {

    @Autowired
    private MinioTemplate minioTemplate;

    // 1. 文件上传
    @PostMapping("/upload")
    public CommonResult<FileInfo> uploadFile(@RequestParam("file") MultipartFile file,
                                           @RequestParam("bucket") String bucket) {
        FileInfo fileInfo = minioTemplate.uploadFile(file, bucket);
        return CommonResult.success(fileInfo);
    }

    // 2. 获取文件信息
    @GetMapping("/{bucket}/{objectName}/info")
    public CommonResult<ObjectStat> getFileInfo(@PathVariable String bucket,
                                              @PathVariable String objectName) {
        ObjectStat stat = minioTemplate.getObjectInfo(bucket, objectName);
        return CommonResult.success(stat);
    }

    // 3. 生成临时访问URL
    @GetMapping("/{bucket}/{objectName}/url")
    public CommonResult<String> getPresignedUrl(@PathVariable String bucket,
                                              @PathVariable String objectName) {
        String url = minioTemplate.getPresignedObjectUrl(bucket, objectName);
        return CommonResult.success(url);
    }

    // 4. 分片上传（大文件）
    @PostMapping("/multipart/upload")
    public CommonResult<String> startMultipartUpload(@RequestBody MultipartUploadRequest request) {
        String uploadId = minioTemplate.initiateMultipartUpload(
            request.getBucket(),
            request.getObjectName(),
            request.getContentType()
        );
        return CommonResult.success(uploadId);
    }
}
```

#### 5.3 完全自定义的MinIO配置
**突破框架限制的自由配置**:
```java
@Configuration
public class CustomMinioConfig {

    // 方式1: 使用默认配置但自定义MinioTemplate实现
    @Bean
    @ConditionalOnMissingBean(MinioTemplate.class)
    public MinioTemplate customMinioTemplate(MinioClient minioClient) {
        return new EnhancedMinioTemplate(minioClient) {

            // 扩展功能: 图片处理
            public String createThumbnail(String bucket, String objectName,
                                        int width, int height) {
                // 生成缩略图逻辑
                String thumbnailName = "thumb/" + objectName;
                // ... 处理逻辑
                return thumbnailName;
            }

            // 扩展功能: 文件加密
            public String uploadEncryptedFile(InputStream inputStream, String bucket,
                                            String objectName, String encryptionKey) {
                // 加密上传逻辑
                return uploadId;
            }

            // 扩展功能: 文件重复检查（MD5）
            public boolean checkFileExists(String bucket, String md5) {
                // 基于MD5检查文件是否已存在
                return fileExists;
            }
        };
    }

    // 方式2: 完全自定义MinioClient（最高级）
    @Bean
    @ConditionalOnMissingBean(MinioClient.class)
    public MinioClient customMinioClient() {
        try {
            return MinioClient.builder()
                .endpoint("https://minio.example.com", 9000, true)  // HTTPS
                .credentials("access-key", "secret-key")
                .region("us-west-1")
                .httpClient(customOkHttpClient())  // 自定义HTTP客户端
                .build();
        } catch (Exception e) {
            throw new RuntimeException("Failed to create MinioClient", e);
        }
    }

    // 自定义OkHttp客户端配置
    @Bean
    public OkHttpClient customOkHttpClient() {
        return new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .followSslRedirects(true)
            .followRedirects(true)
            .certificatePinner(new CertificatePinner.Builder()
                .add("minio.example.com", "sha256/afwi88w...")
                .build())
            .addInterceptor(chain -> {
                // 添加认证头、请求日志等
                Request request = chain.request().newBuilder()
                    .addHeader("X-Custom-Header", "custom-value")
                    .build();
                return chain.proceed(request);
            })
            .build();
    }
}
```

#### 5.4 HTTP端点自定义
**条件化启用** - 可以自定义或禁用:
```java
// 扩展文件上传端点
@RestController
@RequestMapping("/api/minio")
@ConditionalOnProperty(name = "structure.minio.endpoint-enable", havingValue = "true")
public class CustomMinioEndpoint {

    @Autowired
    private MinioTemplate minioTemplate;

    // 自定义文件上传验证
    @PostMapping("/upload/validated")
    public CommonResult<FileInfo> uploadValidatedFile(@RequestParam("file") MultipartFile file) {
        // 文件类型验证
        if (!isValidFileType(file.getContentType())) {
            return CommonResult.validateFailed("不支持的文件类型");
        }

        // 文件大小验证
        if (file.getSize() > 10 * 1024 * 1024) { // 10MB限制
            return CommonResult.validateFailed("文件大小超过限制");
        }

        // 病毒扫描等额外验证...

        FileInfo fileInfo = minioTemplate.uploadFile(file, "validated-files");
        return CommonResult.success(fileInfo);
    }

    // 批量下载为ZIP
    @PostMapping("/download/zip")
    public void downloadAsZip(@RequestBody List<String> fileNames, HttpServletResponse response) {
        // ZIP打包逻辑
    }
}
```

#### 5.5 扩展点总结
| 扩展级别 | 配置方式 | 适用场景 |
|----------|----------|----------|
| **模板扩展** | MinioTemplate方法扩展 | 业务逻辑封装 |
| **客户端自定义** | 自定义MinioClient Bean | HTTP客户端高级配置 |
| **端点扩展** | 自定义@RestController | RESTful API定制 |
| **完整替代** | 不使用starter，完全原生 | 极端定制需求 |

---

## 6. structure-log-starter

### 🌟 核心功能

#### 6.1 AOP日志切面
**保持日志框架原生能力**: 所有Logger功能完全保留

**智能AOP配置**:
```yaml
structure:
  log:
    aop:
      enable: true                                     # 全局日志开关
      expression: "execution(* cn.structure..controller..*.*(..))"  # 切面表达式
      log-format: JSON                                 # 日志格式
      include-response: true                          # 是否记录响应
      include-request: true                           # 是否记录请求
      max-response-length: 2000                       # 响应最大长度
      exclude-patterns:                               # 排除路径
        - "/actuator/*"
        - "/swagger*"
        - "/v2/api-docs"
```

#### 6.2 多模式日志支持
**注解驱动启用** - 完全可选:
```java
// 方式1: 全局启用 + 注解精细控制
@SpringBootApplication
@EnableWebAopLog  // 启用Web AOP日志
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}

// 特定Controller - 可选启用
@RestController
@RequestMapping("/api/user")
@WebAopLog(exclude = true)  // 全局启用但排除此Controller
public class UserController {

    // 特定方法 - 可选启用
    @PostMapping("/save")
    @AspectParamLog  // 启用参数日志
    public CommonResult save(@RequestBody User user) {
        // 记录详细的参数信息
        return CommonResult.success();
    }

    @GetMapping("/{id}")
    @WebAopLog(exclude = true)  // 此方法不记录日志
    public CommonResult getById(@PathVariable Long id) {
        return CommonResult.success(userService.getById(id));
    }
}
```

#### 6.3 自定义日志格式和存储
##### 6.3.1 JSON结构化日志
```java
// 自定义JSON日志布局
@Component
@ConditionalOnMissingBean(JSONLogLayout.class)
public class CustomJSONLogLayout extends JSONLogLayout {

    @Override
    public String doLayout(ILoggingEvent event) {
        Map<String, Object> logMap = new HashMap<>();

        // 基础信息
        logMap.put("timestamp", event.getTimeStamp());
        logMap.put("level", event.getLevel().levelStr);
        logMap.put("thread", event.getThreadName());
        logMap.put("logger", event.getLoggerName());

        // 自定义字段添加
        logMap.put("application", "my-app");
        logMap.put("environment", System.getProperty("spring.profiles.active", "default"));
        logMap.put("server-ip", getServerIp());
        logMap.put("trace-id", MDC.get("traceId"));
        logMap.put("user-id", MDC.get("userId"));

        // 消息和异常
        logMap.put("message", event.getFormattedMessage());
        if (event.getThrowableProxy() != null) {
            logMap.put("exception", getExceptionStackTrace(event.getThrowableProxy()));
        }

        // MDC上下文数据
        logMap.putAll(event.getMDCPropertyMap());

        return JSON.toJSONString(logMap);
    }
}
```

##### 6.3.2 日志存储扩展（ELK、数据库等）
```java
// 自定义日志存储策略
@Component
public class CustomLogStorage {

    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void storeLog(WebLogInfo logInfo) {
        // 并行存储: ES用于搜索，数据库用于持久化
        CompletableFuture.allOf(
            CompletableFuture.runAsync(() -> storeToElasticsearch(logInfo)),
            CompletableFuture.runAsync(() -> storeToDatabase(logInfo))
        ).join();
    }

    private void storeToElasticsearch(WebLogInfo logInfo) {
        IndexQuery indexQuery = new IndexQueryBuilder()
            .withIndexName("application-logs")
            .withType("log")
            .withObject(logInfo)
            .build();
        elasticsearchTemplate.index(indexQuery);
    }

    private void storeToDatabase(WebLogInfo logInfo) {
        String sql = "INSERT INTO t_api_logs (url, method, ip, user_agent, status, response_time, log_time) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql,
            logInfo.getUrl(),
            logInfo.getMethod(),
            logInfo.getIp(),
            logInfo.getUserAgent(),
            logInfo.getStatus(),
            logInfo.getResponseTime(),
            new Date()
        );
    }
}
```

#### 6.4 原生Logger功能保持
```java
// Slf4j原生使用 - 完全不受影响
@RestController
@RequestMapping("/api/test")
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    @GetMapping("/log")
    public CommonResult testLog() {
        // 原生日志功能完全保留
        log.info("这是一条原生info日志");
        log.debug("这是一条原生debug日志");
        log.warn("这是一条原生warn日志");
        log.error("这是一条原生error日志");

        // MDC使用支持
        MDC.put("traceId", UUID.randomUUID().toString());
        MDC.put("userId", "123456");
        log.info("带MDC上下文的日志");

        return CommonResult.success("日志记录完成");
    }
}

// logback.xml原生配置 - 完全支持
<configuration>
    <!-- 原生Console appender -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level [%X{traceId}] %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- 原生File appender -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/application.log</file>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level [%X{traceId}] %logger{36} - %msg%n</pattern>
        </encoder>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/application-%d{yyyy-MM-dd}.%i.log</fileNamePattern>
            <maxFileSize>100MB</maxFileSize>
            <maxHistory>30</maxHistory>
            <totalSizeCap>10GB</totalSizeCap>
        </rollingPolicy>
    </appender>

    <!-- 同时支持框架AOP日志和原生日志 -->
    <root level="INFO">
        <appender-ref ref="CONSOLE" />
        <appender-ref ref="FILE" />
    </root>
</configuration>
```

#### 6.5 高性能AOP日志优化
```java
// 异步日志处理 - 不影响业务性能
@Component
@Aspect
public class AsyncWebLogAspect {

    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(
        Runtime.getRuntime().availableProcessors(),
        Runtime.getRuntime().availableProcessors() * 2,
        60L, TimeUnit.SECONDS,
        new LinkedBlockingQueue<>(1000),
        new ThreadPoolExecutor.CallerRunsPolicy()
    );

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || " +
              "@annotation(org.springframework.web.bind.annotation.GetMapping) || " +
              "@annotation(org.springframework.web.bind.annotation.PostMapping) || " +
              "@annotation(org.springframework.web.bind.annotation.PutMapping) || " +
              "@annotation(org.springframework.web.bind.annotation.DeleteMapping)")
    public void webLog() {}

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = joinPoint.proceed();  // 先执行主业务
        long endTime = System.currentTimeMillis();

        // 异步记录日志，不影响主业务
        executor.execute(() -> {
            try {
                saveWebLog(joinPoint, result, endTime - startTime);
            } catch (Exception e) {
                // 日志记录失败不影响业务
                log.error("日志记录失败", e);
            }
        });

        return result;
    }

    private void saveWebLog(ProceedingJoinPoint joinPoint, Object result, long costTime) {
        // 日志保存逻辑
    }
}
```

#### 6.6 扩展点总结
| 扩展类型 | 实现方式 | 性能影响 |
|----------|----------|----------|
| **格式扩展** | 自定义`JSONLogLayout` | 低 |
| **存储扩展** | 自定义日志存储策略 | 中（可异步） |
| **AOP扩展** | 自定义Aspect切面 | 低（建议异步） |
| **原生扩展** | 标准Logback配置 | 无 |
| **采样扩展** | 日志采样算法 | 低 |

---

## 7. structure-rpc-starter

### 🌟 核心功能

#### 7.1 声明式RPC客户端
**基于接口代理的RPC调用**:
```java
// 1. 定义RPC接口
@RpcClient(
    value = "user-service",           // 服务名
    host = "localhost",               // 主机名
    port = 8080,                      // 端口
    path = "/api",                    // 基础路径
    protocol = HttpProtocol.HTTP      // 协议类型
)
public interface UserServiceClient extends IRpcHandler {

    // GET请求
    @GetMapping("/user/{id}")
    CommonResult<User> getUserById(@PathVariable("id") Long id);

    // POST请求
    @PostMapping("/user/save")
    CommonResult<User> saveUser(@RequestBody User user);

    // PUT请求
    @PutMapping("/user/update")
    CommonResult<User> updateUser(@RequestBody User user);

    // DELETE请求
    @DeleteMapping("/user/delete/{id}")
    CommonResult<Void> deleteUser(@PathVariable("id") Long id);

    // 复杂查询
    @PostMapping("/user/search")
    CommonResult<IPage<User>> searchUsers(@RequestBody UserQuery query);
}
```

**服务发现支持**:
```yaml
# 多环境服务配置 - 支持运行时切换
structure:
  rpc:
    serviceList:
      user-service:
        host: ${USER_SERVICE_HOST:localhost}
        port: ${USER_SERVICE_PORT:8080}
        health-check-url: /actuator/health
        timeout: 5000                    # 超时时间
        retry-times: 3                   # 重试次数
        load-balance: round-robin        # 负载均衡策略

      # 开发环境
      dev-user-service:
        host: dev-user.internal.com
        port: 8080

      # 生产环境集群
      prod-user-service-1:
        host: prod-user-1.internal.com
        port: 8080
        weight: 3  # 权重
      prod-user-service-2:
        host: prod-user-2.internal.com
        port: 8080
        weight: 2
      prod-user-service-3:
        host: prod-user-3.internal.com
        port: 8080
        weight: 1
```

#### 7.2 OAuth2安全集成
**内置Token管理**:
```java
// 框架自动处理OAuth2流程
@RpcClient(
    value = "secure-service",
    host = "secure.example.com",
    port = 443,
    protocol = HttpProtocol.HTTPS,
    oauth2 = @OAuth2Config(
        tokenUrl = "https://auth.example.com/oauth/token",        // Token获取地址
        clientId = "your-client-id",                              // 客户端ID
        clientSecret = "${OAUTH_CLIENT_SECRET}",                  // 客户端密钥
        grantType = GrantType.CLIENT_CREDENTIALS,                 // 授权类型
        scope = {"read", "write"},                                // 作用域
        tokenTimeout = 3600                                       // Token超时时间
    )
)
public interface SecureServiceClient {

    @GetMapping("/api/secure-data")
    CommonResult<SecureData> getSecureData();
}

// Token自动刷新和缓存 - 完全透明
@Service
public class TokenRefreshManager {

    @Scheduled(fixedDelay = 300000) // 每5分钟检查
    public void refreshTokens() {
        // 框架自动获取新的access_token
        // 自动处理token失效和刷新逻辑
    }
}
```

#### 7.3 高级RPC功能扩展
##### 7.3.1 断路器和容错
```java
// 基于Resilience4j的断路器集成
@Component
public class RpcCircuitBreakerDecorator implements IRpcHandlerDecorator {

    @Autowired
    private CircuitBreakerRegistry circuitBreakerRegistry;

    @Override
    public Object invoke(IRpcHandler rpcHandler, Method method, Object[] args) throws Throwable {
        String serviceName = rpcHandler.getClass().getAnnotation(RpcClient.class).value();
        CircuitBreaker circuitBreaker = circuitBreakerRegistry.circuitBreaker(serviceName);

        return Try.ofSupplier(
                CircuitBreaker.decorateSupplier(circuitBreaker, () -> {
                    try {
                        return method.invoke(rpcHandler, args);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }))
                .recover(throwable -> handleFallback(method, args, throwable))
                .get();
    }

    private Object handleFallback(Method method, Object[] args, Throwable throwable) {
        // 断路器开启时的降级处理
        log.warn("服务调用降级: {}", throwable.getMessage());
        return CommonResult.failed("服务暂时不可用，请稍后重试");
    }
}

// 超时配置
structure:
  rpc:
    serviceList:
      user-service:
        timeout: 3000                    # 调用超时
        connect-timeout: 1000            # 连接超时
        read-timeout: 2000               # 读取超时
        retry:
          enabled: true                  # 重试启用
          max-attempts: 3                # 最大重试次数
          backoff-interval: 1000         # 退避间隔
          max-backoff-interval: 5000     # 最大退避时间
```

##### 7.3.2 请求拦截器和监控
```java
// 自定义请求拦截器
@Component
public class RpcRequestInterceptor implements RequestInterceptor {

    @Override
    public void intercept(RequestTemplate template) {
        // 添加全局头信息
        template.header("X-Request-ID", UUID.randomUUID().toString());
        template.header("X-Application-Name", applicationName);

        // 添加认证信息
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null) {
            template.header("X-User-Name", authentication.getName());
        }

        // 添加trace信息
        Span span = tracer.currentSpan();
        if (span != null) {
            template.header("X-B3-TraceId", span.context().traceIdString());
            template.header("X-B3-SpanId", span.context().spanIdString());
        }
    }
}

// 响应监控器
@Component
public class RpcResponseMonitor implements ResponseInterceptor {

    private final MeterRegistry meterRegistry;

    @Override
    public void intercept(Response response) {
        // 记录响应时间
        Timer.Sample sample = Timer.start(meterRegistry);

        if (response.status() >= 200 && response.status() < 300) {
            meterRegistry.counter("rpc.calls.success", "service", getServiceName(response)).increment();
        } else {
            meterRegistry.counter("rpc.calls.error",
                "service", getServiceName(response),
                "status", String.valueOf(response.status())
            ).increment();
        }

        // 记录响应时间分布
        meterRegistry.timer("rpc.response.time", "service", getServiceName(response))
                     .record(sample.stop(TIMER));
    }
}
```

##### 7.3.3 完全自定义的HTTP客户端
```java
// 完全替换HTTP实现 - 支持其他协议
@Configuration
public class CustomRpcConfig {

    // 方式1: 使用OkHttp替代默认HttpURLConnection
    @Bean
    @ConditionalOnMissingBean(IRpcClientFactory.class)
    public IRpcClientFactory okHttpClientFactory() {
        return new IRpcClientFactory() {

            private final OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .followRedirects(true)
                .followSslRedirects(true)
                .connectionPool(new ConnectionPool(10, 5, TimeUnit.MINUTES))
                .addInterceptor(new HttpLoggingInterceptor().setLevel(Level.BODY))
                .build();

            @Override
            public <T> T createClient(Class<T> clientClass, RemoteService service) {
                // 使用动态代理创建客户端实现
                return new OkHttpRpcClient<>(clientClass, service, client);
            }
        };
    }

    // 方式2: 完全重写RPC处理器
    @Component
    public class CustomFeignRpcHandler implements IRpcHandler, ApplicationContextAware {

        private ApplicationContext applicationContext;
        private FeignContext feignContext;

        @Override
        public Object invoke(String service, Method method, Object[] args) throws Throwable {
            // 使用Feign替代原生HTTP调用
            FeignClientFactory factory = feignContext.getInstance(service, FeignClientFactory.class);
            T target = factory.create(method.getDeclaringClass());
            return method.invoke(target, args);
        }

        @Override
        public void setApplicationContext(ApplicationContext applicationContext) {
            this.applicationContext = applicationContext;
            this.feignContext = applicationContext.getBean(FeignContext.class);
        }
    }
}
```

#### 7.4 服务发现集成示例
```java
// 对接Eureka服务发现
@Configuration
public class EurekaRpcDiscovery {

    @Autowired
    private DiscoveryClient discoveryClient;

    @EventListener(ApplicationReadyEvent.class)
    public void refreshServiceInstances() {
        // 从注册中心获取服务实例
        List<ServiceInstance> instances = discoveryClient.getInstances("user-service");

        Set<RemoteService> services = instances.stream()
            .map(instance -> new RemoteService(
                instance.getServiceId(),
                instance.getHost(),
                instance.getPort(),
                instance.getUri().getPath()
            ))
            .collect(Collectors.toSet());

        // 动态更新RPC客户端配置
        RpcClientRegistry.registerServices(services);
    }
}
```

#### 7.5 扩展点总结
| 扩展层级 | 实现方式 | 复杂度 |
|----------|----------|--------|
| **配置级扩展** | YAML服务配置 | ⭐ |
| **注解级扩展** | @RpcClient注解配置 | ⭐ |
| **拦截器扩展** | Request/ResponseInterceptor | ⭐⭐ |
| **工厂级扩展** | IRpcClientFactory实现 | ⭐⭐⭐ |
| **协议级扩展** | 完全自定义IRpcHandler | ⭐⭐⭐⭐ |

---

## 🔧 统一扩展设计模式总结

### 1. **条件化配置模式**
```java
// 避免Bean冲突，支持用户自定义
@ConditionalOnMissingBean(IDistributedLock.class)
@ConditionalOnProperty(name = "structure.redis.lock.enable", havingValue = "true")
public IDistributedLock distributedLock() {
    return new RedisDistributedLock();
}
```

### 2. **三层次扩展体系**
| 扩展层次 | 框架角色 | 用户控制度 | 侵入性 |
|----------|----------|------------|--------|
| **配置层** | 提供默认配置 | 属性配置控制 | 零侵入 |
| **实现层** | 提供默认实现 | Bean替换控制 | 低侵入 |
| **协议层** | 定义抽象接口 | 完全重写实现 | 无侵入 |

### 3. **原生兼容性保证**
- ✅ 所有Starter都只是**增强**，不是**替代**
- ✅ 支持**渐进式采用**，可随时移除
- ✅ **100%兼容**原有Spring Boot配置
- ✅ 提供**多层退出机制**：配置关闭 → Bean替换 → 完全自建

### 4. **最佳实践建议**

#### 4.1 渐进采用策略
```
第一步: 引入依赖，使用默认配置
第二步: 调整配置参数满足业务需求
第三步: 扩展Bean实现业务个性化
第四步: 按需完全自定义核心组件
```

#### 4.2 配置优先级原则
```
框架默认配置 < 用户属性配置 < 用户Bean配置 < 用户完全重写
```

#### 4.3 重构点识别
- **配置重复**: 多处相同配置 → 提取到application.yml
- **工具类重复**: 多处相同工具类 → 下沉到structure-common
- **业务模板**: 通用业务逻辑 → 抽象到框架层
- **定制需求**: 特殊业务需求 → 提供扩展点

Structure Boot的核心价值：**提供企业级基础能力的同时，保持了对原生Spring Boot生态的100%兼容和自由扩展能力**。