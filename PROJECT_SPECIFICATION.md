# Structure Boot 项目技术与开发规约

## 项目概述

Structure Boot是一个基于Spring Boot 2.7.x的企业级快速开发框架，提供一系列开箱即用的Starter组件，旨在简化企业级应用开发流程，提升开发效率。

## 技术栈

### 核心技术框架
- **Spring Boot**: 2.7.18 - 微服务快速开发框架
- **Spring Security**: 5.7.10 - 安全管理框架
- **Java**: 1.8+ - 开发语言
- **Maven**: 3.6+ - 项目构建工具

### 数据持久层
- **MyBatis**: 3.5.5 - ORM框架
- **MyBatis-Plus**: 3.4.0 - MyBatis增强工具
- **MySQL**: 8.0.28 - 关系型数据库
- **PageHelper**: 5.1.8 - 物理分页插件
- **动态数据源**: 3.1.0 - 多数据源管理

### 缓存与消息
- **Redis**: Spring Boot Data Redis - 缓存数据库
- **Redisson**: 3.10.7 - 分布式锁和缓存

### 常用组件
- **Hutool**: 5.1.0 - Java工具类库
- **FastJSON**: 1.2.83 - JSON处理
- **Guava**: 29.0-jre - Google核心库
- **Lombok**: 1.18.24 - 代码简化工具

### API文档
- **Swagger2**: 2.9.2 - API文档生成
- **Swagger Bootstrap UI**: 1.9.6 - 美化Swagger界面

### 文件存储
- **MinIO**: 8.3.3 - 对象存储服务

## 核心模块功能

### 1. structure-restful-web-starter
- **功能**: Web开发核心启动器
- **特性**:
  - 统一的RESTful API响应封装
  - 全局异常处理
  - FastJSON自动配置
  - Swagger文档生成
  - 跨域处理配置

### 2. structure-mybatis-plus-starter
- **功能**: MyBatis-Plus增强启动器
- **特性**:
  - 零配置集成MyBatis-Plus
  - 逻辑删除自动处理
  - 自动填充功能
  - 分页插件配置
  - 性能分析插件
  - 分表插件支持

### 3. structure-redis-starter
- **功能**: Redis缓存启动器
- **特性**:
  - 连接池自动配置
  - 序列化优化
  - 缓存注解支持
  - Redis工具类封装

### 4. structure-redisson-starter
- **功能**: 分布式锁启动器
- **特性**:
  - Redisson客户端配置
  - 分布式锁实现
  - 公平锁支持
  - 读写锁支持
  - 信号量实现

### 5. structure-minio-starter
- **功能**: 对象存储启动器
- **特性**:
  - MinIO客户端配置
  - 文件上传下载封装
  - 存储桶管理
  - 临时链接生成

### 6. structure-log-starter
- **功能**: 日志记录启动器
- **特性**:
  - AOP方法调用日志
  - 参数和返回值记录
  - 执行时间统计
  - 可配置化控制

### 7. structure-rpc-starter
- **功能**: RPC调用支持
- **特性**:
  - OAuth2客户端配置
  - 服务间调用封装
  - Token自动传递

## 开发规约

### 1. 代码规范

#### 包命名规范
```
cn.structure.{模块名}.{层名}
- controller: 控制层
- service: 业务层
- mapper: 数据访问层
- entity: 实体类
- dto: 数据传输对象
- vo: 视图对象
- config: 配置类
- util: 工具类
```

#### 类命名规范
- **Controller**: XxxController
- **Service**: XxxService, XxxServiceImpl
- **Mapper**: XxxMapper
- **Entity**: Xxx (数据库实体)
- **DTO**: XxxDTO (数据传输对象)
- **VO**: XxxVO (视图对象)
- **Config**: XxxConfig (配置类)

#### 方法命名规范
- **查询**: getXxx, findXxx, queryXxx
- **新增**: saveXxx, addXxx, createXxx
- **修改**: updateXxx, modifyXxx
- **删除**: deleteXxx, removeXxx
- **分页查询**: pageXxx, listXxx

### 2. 配置管理

#### 配置文件优先级
```
bootstrap.yml → application.yml → application-{profile}.yml
```

#### 主要配置结构
```yaml
spring:
  # 数据源配置
  datasource:
    url: jdbc:mysql://localhost:3306/test?useSSL=false&useUnicode=true&characterEncoding=UTF-8
    username: root
    password: 123456
    driver-class-name: com.mysql.cj.jdbc.Driver

  # Redis配置
  redis:
    host: localhost
    port: 6379
    password:
    database: 0

# 自定义配置
structure:
  # 日志配置
  log:
    aop:
      enable: true
      expression: execution(* cn.structure..controller..*.*(..))

  # MinIO配置
  minio:
    endpoint: http://localhost:9000
    accessKey: minioadmin
    secretKey: minioadmin
    bucket: default
```

### 3. 数据库设计规范

#### 表字段规范
- **主键**: id, BIGINT, 自增
- **逻辑删除**: deleted, TINYINT, 默认值0
- **创建时间**: create_time, DATETIME
- **更新时间**: update_time, DATETIME
- **版本号**: version, INT (乐观锁)

#### 实体类规范
```java
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String name;

    private Integer age;

    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    @TableLogic
    private Integer deleted;
}
```

### 4. API设计规范

#### 响应格式统一
```json
{
    "code": 200,
    "success": true,
    "message": "操作成功",
    "data": {},
    "timestamp": 1640995200000
}
```

#### 请求映射规范
- **GET**: /api/{resource} - 查询列表
- **GET**: /api/{resource}/{id} - 查询详情
- **POST**: /api/{resource} - 新增
- **PUT**: /api/{resource}/{id} - 修改
- **DELETE**: /api/{resource}/{id} - 删除

### 5. 异常处理规范

#### 统一异常处理
```java
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public CommonResult handleBusinessException(BusinessException e) {
        log.error("业务异常: {}", e.getMessage(), e);
        return CommonResult.failed(e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public CommonResult handleException(Exception e) {
        log.error("系统异常: {}", e.getMessage(), e);
        return CommonResult.failed("系统繁忙，请稍后重试");
    }
}
```

### 6. 日志规范

#### 日志级别使用
- **ERROR**: 系统错误，需要人工处理
- **WARN**: 警告信息，不影响系统运行
- **INFO**: 关键业务信息
- **DEBUG**: 调试信息

#### 日志格式
```java
log.info("用户登录成功, userId: {}, username: {}", userId, username);
log.warn("密码错误次数过多, userId: {}", userId);
log.error("数据库连接失败", e);
```

### 7. 安全规范

#### 敏感数据处理
- 密码必须加密存储
- 个人信息需要脱敏处理
- 重要操作记录审计日志

#### SQL注入防护
- 使用MyBatis的#{}占位符
- 禁止拼接SQL语句
- 参数校验和过滤

## 模块依赖关系

```
structure-dependencies (依赖版本管理)
    ↓
structure-common (公共模块)
    ↓
structure-boot-parent (父POM)
    ↓
各功能模块 (starters)
```

## 使用示例

### 快速开始
1. **添加依赖**
```xml
<dependency>
    <groupId>cn.structure</groupId>
    <artifactId>structure-restful-web-starter</artifactId>
    <version>${structure.boot.version}</version>
</dependency>
```

2. **配置应用**
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/test
    username: root
    password: 123456

mybatis-plus:
  mapper-locations: classpath:mapper/*.xml
```

3. **创建实体**
```java
@Data
@TableName("t_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private Integer age;
}
```

4. **创建Mapper**
```java
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
```

5. **创建Service**
```java
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Override
    public IPage<User> pageUser(Page<User> page, QueryWrapper<User> wrapper) {
        return this.page(page, wrapper);
    }
}
```

6. **创建Controller**
```java
@RestController
@RequestMapping("/api/user")
@Api(value = "用户管理", tags = "用户管理")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/page")
    @ApiOperation("分页查询用户")
    public CommonResult<IPage<User>> pageUser(@RequestParam(defaultValue = "1") Integer pageNum,
                                               @RequestParam(defaultValue = "10") Integer pageSize) {
        Page<User> page = new Page<>(pageNum, pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        IPage<User> userPage = userService.pageUser(page, wrapper);
        return CommonResult.success(userPage);
    }
}
```

## 部署说明

### 环境要求
- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis 6.0+

### 部署步骤
1. 安装依赖环境
2. 初始化数据库
3. 配置应用参数
4. 打包部署

### 发布流程
```bash
mvn clean package -DskipTests
java -jar target/application.jar
```

## 版本管理

### 版本规范
- **主版本号**: 重大架构变更
- **次版本号**: 功能新增
- **修订版本号**: Bug修复

### 当前版本
```xml
<revision>1.0.0</revision>
```

## 贡献指南

### 代码提交规范
1. **提交信息格式**
```
type(scope): description

body
```

2. **提交类型**
- **feat**: 新功能
- **fix**: Bug修复
- **docs**: 文档更新
- **style**: 代码格式
- **refactor**: 重构
- **test**: 测试
- **chore**: 构建工具

### 分支管理
- **master**: 主分支，稳定版本
- **develop**: 开发分支
- **feature/*: 功能分支
- **hotfix/*: 热修复分支

---

本规约文档会根据项目发展持续更新，请开发者及时关注最新版本。