# Structure Boot 用户开发指南

## 一、项目概述

Structure Boot 是一个基于 Spring Boot 的快速开发框架，提供一系列开箱即用的 Starter 组件，帮助开发者快速构建企业级应用。

**核心特性：**
- RESTful API 支持、统一异常处理、参数校验、Swagger 文档
- MyBatis 增强、MyBatis-Plus 集成、代码生成器
- Redis 分布式锁、Redisson 高级缓存
- MinIO 对象存储集成
- 统一日志配置、AOP 日志记录

---

## 二、环境要求

| 依赖 | 版本要求 | 说明 |
|-----|---------|------|
| JDK | 17+ | 推荐使用 OpenJDK 17 LTS |
| Maven | 3.6+ | 项目构建工具 |
| Spring Boot | 3.2.x+ | 基础框架版本 |
| MySQL | 8.0+ | 关系型数据库（可选） |
| Redis | 6.0+ | 缓存数据库（可选） |
| MinIO | 8.0+ | 对象存储（可选） |

---

## 三、快速开始

### 3.1 创建项目

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 http://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.example</groupId>
    <artifactId>demo</artifactId>
    <version>1.0.0</version>

    <properties>
        <spring.boot.version>4.0.6</spring.boot.version>
        <structure.version>1.4.2</structure.version>
        <maven.compiler.source>17</maven.compiler.source>
        <maven.compiler.target>17</maven.compiler.target>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-parent</artifactId>
                <version>${spring.boot.version}</version>
                <scope>import</scope>
                <type>pom</type>
            </dependency>
            <dependency>
                <groupId>cn.structured</groupId>
                <artifactId>structure-boot-parent</artifactId>
                <version>${structure.version}</version>
                <scope>import</scope>
                <type>pom</type>
            </dependency>
        </dependencies>
    </dependencyManagement>
</project>
```

### 3.2 添加依赖

| Starter | 说明 | Maven 依赖 |
|---------|-----|-----------|
| Web 开发 | RESTful API 支持 | `structure-restful-web-starter` |
| MyBatis | ORM 框架支持 | `structure-mybatis-starter` |
| MyBatis-Plus | MyBatis 增强 | `structure-mybatis-plus-starter` |
| Redis | 缓存支持 | `structure-redis-starter` |
| Redisson | 分布式锁 | `structure-redisson-starter` |
| MinIO | 对象存储 | `structure-minio-starter` |
| Log | 日志记录 | `structure-log-starter` |

```xml
<!-- Web 开发 -->
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-restful-web-starter</artifactId>
</dependency>
```

---

## 四、组件配置规范

### 4.1 Web 组件配置

| 配置项 | 数据类型 | 默认值 | 允许范围 | 使用场景 |
|-------|---------|-------|---------|---------|
| structure.web.cors.enable | Boolean | true | true/false | 是否开启跨域 |
| structure.web.cors.allowed-origins | List\<String\> | * | 域名列表 | 允许的跨域来源 |
| structure.web.cors.allowed-methods | List\<String\> | GET,POST,PUT,DELETE | HTTP 方法 | 允许的请求方法 |
| structure.web.swagger.enable | Boolean | true | true/false | 是否开启 Swagger |
| structure.web.swagger.title | String | Structure Boot API | 字符串 | API 文档标题 |
| structure.web.swagger.description | String | API Documentation | 字符串 | API 文档描述 |
| structure.web.swagger.version | String | 1.0.0 | 版本号 | API 版本 |

**配置示例：**

```yaml
structure:
  web:
    cors:
      enable: true
      allowed-origins:
        - http://localhost:8080
        - https://example.com
      allowed-methods:
        - GET
        - POST
        - PUT
        - DELETE
    swagger:
      enable: true
      title: 示例项目 API
      description: 示例项目接口文档
      version: 1.0.0
```

### 4.2 MyBatis 组件配置

| 配置项 | 数据类型 | 默认值 | 允许范围 | 使用场景 |
|-------|---------|-------|---------|---------|
| structure.mybatis.plugin.generate-id-type | String | none | none, uuid, snowflake | ID 生成策略 |
| structure.mybatis.plugin.data-center | Integer | 0 | 0-31 | 雪花算法数据中心 ID |
| structure.mybatis.plugin.machine | Integer | 0 | 0-31 | 雪花算法机器 ID |
| structure.mybatis.plugin.log-sql | Boolean | false | true/false | 是否打印 SQL |

**配置示例：**

```yaml
structure:
  mybatis:
    plugin:
      generate-id-type: snowflake
      data-center: 0
      machine: 1
      log-sql: false
```

### 4.3 Redis 组件配置

| 配置项 | 数据类型 | 默认值 | 允许范围 | 使用场景 |
|-------|---------|-------|---------|---------|
| spring.data.redis.host | String | localhost | 字符串 | Redis 主机地址 |
| spring.data.redis.port | Integer | 6379 | 1-65535 | Redis 端口 |
| spring.data.redis.database | Integer | 0 | 0-15 | 数据库索引 |
| spring.data.redis.password | String | 空 | 字符串 | 密码（可选） |
| spring.data.redis.timeout | Duration | 60s | Duration | 连接超时时间 |
| spring.data.redis.lettuce.pool.max-active | Integer | 8 | 1-100 | 最大连接数 |
| spring.data.redis.lettuce.pool.max-idle | Integer | 8 | 1-100 | 最大空闲连接 |
| spring.data.redis.lettuce.pool.min-idle | Integer | 0 | 0-100 | 最小空闲连接 |

**配置示例：**

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      database: 0
      password: password
      timeout: 60s
      lettuce:
        pool:
          max-active: 8
          max-idle: 8
          min-idle: 2
```

### 4.4 Redisson 组件配置

| 配置项 | 数据类型 | 默认值 | 允许范围 | 使用场景 |
|-------|---------|-------|---------|---------|
| structure.redisson.address | String | redis://localhost:6379 | 连接字符串 | Redis 地址 |
| structure.redisson.password | String | 空 | 字符串 | 密码（可选） |
| structure.redisson.database | Integer | 0 | 0-15 | 数据库索引 |
| structure.redisson.connection-pool-size | Integer | 64 | 1-200 | 连接池大小 |
| structure.redisson.idle-connection-timeout | Integer | 10000 | 1000-60000 | 空闲连接超时(ms) |
| structure.redisson.connect-timeout | Integer | 10000 | 1000-60000 | 连接超时(ms) |

**配置示例：**

```yaml
structure:
  redisson:
    address: redis://localhost:6379
    database: 1
    connection-pool-size: 64
    idle-connection-timeout: 10000
    connect-timeout: 10000
```

### 4.5 MinIO 组件配置

| 配置项 | 数据类型 | 默认值 | 允许范围 | 使用场景 |
|-------|---------|-------|---------|---------|
| structure.minio.url | String | http://localhost:9000 | URL | MinIO 服务地址 |
| structure.minio.access-key | String | 空 | 字符串 | 访问密钥 |
| structure.minio.secret-key | String | 空 | 字符串 | 秘密密钥 |
| structure.minio.bucket | String | default | 字符串 | 默认存储桶 |
| structure.minio.endpoint-enable | Boolean | true | true/false | 是否开启端点 |
| structure.minio.connect-timeout | Integer | 60000 | 1000-300000 | 连接超时(ms) |
| structure.minio.write-timeout | Integer | 60000 | 1000-300000 | 写入超时(ms) |

**配置示例：**

```yaml
structure:
  minio:
    url: http://localhost:9000
    access-key: minioadmin
    secret-key: minioadmin
    bucket: example-bucket
    endpoint-enable: true
    connect-timeout: 60000
    write-timeout: 60000
```

### 4.6 Log 组件配置

| 配置项 | 数据类型 | 默认值 | 允许范围 | 使用场景 |
|-------|---------|-------|---------|---------|
| structure.log.aop.enable | Boolean | false | true/false | 是否开启 AOP 日志 |
| structure.log.aop.expression | String | execution(*..controller..*.*(..)) | SpEL | 切点表达式 |
| structure.log.aop.log-params | Boolean | true | true/false | 是否记录参数 |
| structure.log.aop.log-result | Boolean | true | true/false | 是否记录返回值 |
| structure.log.aop.log-execution-time | Boolean | true | true/false | 是否记录执行时间 |

**配置示例：**

```yaml
structure:
  log:
    aop:
      enable: true
      expression: execution(* com.example..controller..*.*(..))
      log-params: true
      log-result: true
      log-execution-time: true
```

---

## 五、开发规范

### 5.1 包结构规范

```
com.example.demo
├── controller          # 控制层（REST API）
│   └── UserController.java
├── service             # 业务层接口
│   └── UserService.java
├── service/impl        # 业务层实现
│   └── UserServiceImpl.java
├── mapper              # 数据访问层
│   └── UserMapper.java
├── entity              # 数据库实体
│   └── User.java
├── dto                 # 数据传输对象
│   ├── UserCreateDTO.java
│   └── UserUpdateDTO.java
├── vo                  # 视图对象
│   └── UserVO.java
├── config              # 配置类
│   └── WebConfig.java
├── util                # 工具类
│   └── DateUtils.java
├── exception           # 自定义异常
│   ├── BusinessException.java
│   └── GlobalExceptionHandler.java
└── Application.java    # 启动类
```

### 5.2 命名规范

| 类型 | 命名规则 | 示例 |
|-----|---------|------|
| Controller | PascalCase + Controller | UserController |
| Service | PascalCase + Service | UserService |
| ServiceImpl | PascalCase + ServiceImpl | UserServiceImpl |
| Mapper | PascalCase + Mapper | UserMapper |
| Entity | PascalCase | User |
| DTO | PascalCase + DTO | UserCreateDTO |
| VO | PascalCase + VO | UserVO |
| Config | PascalCase + Config | WebConfig |
| Exception | PascalCase + Exception | BusinessException |

### 5.3 方法命名规范

| 操作类型 | 前缀 | 示例 |
|---------|-----|------|
| 查询单个 | get | getUserById(Long id) |
| 查询列表 | list | listUsers(UserQueryDTO query) |
| 查询分页 | page | pageUsers(PageQueryDTO query) |
| 新增 | save/create/add | saveUser(UserCreateDTO dto) |
| 更新 | update | updateUser(Long id, UserUpdateDTO dto) |
| 删除 | delete/remove | deleteUser(Long id) |
| 批量操作 | batch | batchDelete(List\<Long\> ids) |
| 计数 | count | countUsers(UserQueryDTO query) |
| 检查存在 | exists | existsByUsername(String username) |

---

## 六、日志规范

### 6.1 日志级别划分标准

| 级别 | 严重程度 | 使用场景 | 是否需要处理 |
|-----|---------|---------|------------|
| ERROR | 严重 | 系统错误、异常、服务不可用 | 是，立即处理 |
| WARN | 警告 | 配置缺失、降级处理、潜在问题 | 建议处理 |
| INFO | 信息 | 业务流程、关键操作、状态变更 | 记录即可 |
| DEBUG | 调试 | 详细信息、参数、返回值 | 开发调试 |
| TRACE | 追踪 | 最详细的调用链路 | 深度调试 |

### 6.2 日志必选字段

| 字段 | 类型 | 说明 | 示例 |
|-----|-----|------|------|
| timestamp | DateTime | 日志时间 | 2024-01-15 10:30:45.123 |
| level | String | 日志级别 | INFO |
| module | String | 模块名称 | user-service |
| className | String | 类名 | UserService |
| methodName | String | 方法名 | getUserById |
| traceId | String | 请求追踪ID | a1b2c3d4-e5f6-7890 |
| message | String | 日志消息 | 查询用户成功 |
| params | Object | 请求参数 | {"id": 1} |
| result | Object | 返回结果 | {"id": 1, "name": "test"} |
| exception | String | 异常信息 | NullPointerException |
| executionTime | Long | 执行时间(ms) | 15 |

### 6.3 日志输出模板

**控制台输出格式：**
```
{timestamp} [{level}] [{traceId}] {module} - {className}.{methodName}() - {message}
```

**文件输出格式（JSON）：**
```json
{
    "timestamp": "2024-01-15 10:30:45.123",
    "level": "INFO",
    "module": "user-service",
    "className": "com.example.service.UserService",
    "methodName": "getUserById",
    "traceId": "a1b2c3d4-e5f6-7890",
    "message": "查询用户成功",
    "params": {"id": 1},
    "result": {"id": 1, "username": "test"},
    "executionTime": 15
}
```

### 6.4 日志使用示例

```java
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Override
    public UserVO getUserById(Long id) {
        // 记录请求参数
        log.info("开始查询用户, userId: {}", id);
        
        try {
            // 设置追踪ID
            String traceId = MDC.get("traceId");
            
            // 业务逻辑
            User user = userMapper.selectById(id);
            
            if (user == null) {
                log.warn("用户不存在, userId: {}, traceId: {}", id, traceId);
                throw new BusinessException(ErrorCode.USER_NOT_FOUND);
            }
            
            UserVO result = convertToVO(user);
            
            // 记录成功结果
            log.info("查询用户成功, userId: {}, result: {}, traceId: {}", 
                     id, result, traceId);
            
            return result;
        } catch (BusinessException e) {
            // 业务异常只记录 WARN
            log.warn("业务异常: {}, userId: {}", e.getMessage(), id);
            throw e;
        } catch (Exception e) {
            // 系统异常记录 ERROR
            log.error("查询用户失败, userId: {}", id, e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
    }
}
```

---

## 七、错误码规范

### 7.1 错误码结构

错误码采用 **5 位数字**编码：

| 位数 | 含义 | 示例 |
|-----|------|------|
| 第1位 | 错误级别 | 1-系统错误，2-业务错误，3-参数错误 |
| 第2-3位 | 模块编号 | 01-用户模块，02-订单模块，03-商品模块 |
| 第4-5位 | 错误序号 | 01-000 |

### 7.2 错误码定义

```java
/**
 * 错误码枚举类
 */
public enum ErrorCode {
    
    // ========== 系统错误 (1xxx) ==========
    SUCCESS(200, "操作成功"),
    SYSTEM_ERROR(10000, "系统繁忙，请稍后重试"),
    SERVICE_UNAVAILABLE(10001, "服务暂不可用"),
    DB_ERROR(10002, "数据库操作异常"),
    NETWORK_ERROR(10003, "网络连接异常"),
    
    // ========== 参数错误 (3xxx) ==========
    PARAM_ERROR(30000, "参数校验失败"),
    PARAM_NULL(30001, "参数不能为空"),
    PARAM_INVALID(30002, "参数格式无效"),
    PARAM_OUT_OF_RANGE(30003, "参数超出范围"),
    
    // ========== 用户模块 (201xx) ==========
    USER_NOT_FOUND(20100, "用户不存在"),
    USER_EXISTS(20101, "用户已存在"),
    USER_PASSWORD_ERROR(20102, "密码错误"),
    USER_LOCKED(20103, "用户已被锁定"),
    USER_NOT_LOGIN(20104, "用户未登录"),
    
    // ========== 订单模块 (202xx) ==========
    ORDER_NOT_FOUND(20200, "订单不存在"),
    ORDER_STATUS_ERROR(20201, "订单状态错误"),
    ORDER_CREATE_FAILED(20202, "创建订单失败"),
    
    // ========== 商品模块 (203xx) ==========
    PRODUCT_NOT_FOUND(20300, "商品不存在"),
    PRODUCT_STOCK_INSUFFICIENT(20301, "商品库存不足"),
    
    // ========== 权限模块 (204xx) ==========
    PERMISSION_DENIED(20400, "权限不足"),
    ACCESS_DENIED(20401, "访问被拒绝");
    
    private final int code;
    private final String message;
    
    ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }
    
    public int getCode() {
        return code;
    }
    
    public String getMessage() {
        return message;
    }
}
```

### 7.3 错误响应格式

```json
{
    "code": 20100,
    "success": false,
    "message": "用户不存在",
    "data": null,
    "timestamp": 1705307445123,
    "traceId": "a1b2c3d4-e5f6-7890"
}
```

---

## 八、实体类规范

### 8.1 MyBatis-Plus 实体类规范

```java
package com.example.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 用户实体类
 * 
 * @author author
 * @since 2024-01-01
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@TableName("t_user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     * 主键，自增策略
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     * 非空，长度2-50
     */
    @TableField("username")
    private String username;

    /**
     * 邮箱
     * 非空，邮箱格式
     */
    @TableField("email")
    private String email;

    /**
     * 密码
     * 非空，加密存储
     */
    @TableField("password")
    private String password;

    /**
     * 年龄
     * 可选，范围1-150
     */
    @TableField("age")
    private Integer age;

    /**
     * 状态
     * 0-禁用，1-启用，默认1
     */
    @TableField("status")
    @TableLogic(value = "1", delval = "0")
    private Integer status;

    /**
     * 创建时间
     * 自动填充（插入时）
     */
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     * 自动填充（插入和更新时）
     */
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    /**
     * 版本号
     * 乐观锁
     */
    @Version
    @TableField("version")
    private Integer version;
}
```

### 8.2 字段命名规范

| 字段类型 | 命名规则 | 示例 |
|---------|---------|------|
| 主键 | id | id |
| 创建时间 | create_time | create_time |
| 更新时间 | update_time | update_time |
| 逻辑删除 | deleted | deleted |
| 版本号 | version | version |
| 状态 | status | status |
| 外键 | xxx_id | user_id, order_id |

### 8.3 自动填充配置

```java
package com.example.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * MyBatis-Plus 自动填充处理器
 */
@Component
public class MyMetaObjectHandler implements MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.strictInsertFill(metaObject, "createTime", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
        this.strictInsertFill(metaObject, "version", () -> 1, Integer.class);
        this.strictInsertFill(metaObject, "status", () -> 1, Integer.class);
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        this.strictUpdateFill(metaObject, "updateTime", LocalDateTime::now, LocalDateTime.class);
    }
}
```

---

## 九、Service 层规范

### 9.1 接口定义

```java
package com.example.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.dto.UserCreateDTO;
import com.example.dto.UserUpdateDTO;
import com.example.entity.User;
import com.example.vo.UserVO;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 用户服务接口
 * 
 * @author author
 * @since 2024-01-01
 */
public interface UserService extends IService<User> {

    /**
     * 根据ID查询用户
     * 
     * @param id 用户ID，不能为空
     * @return 用户信息
     * @throws com.example.exception.BusinessException 当用户不存在时抛出
     */
    UserVO getUserById(@NotNull(message = "用户ID不能为空") Long id);

    /**
     * 查询所有用户
     * 
     * @return 用户列表
     */
    List<UserVO> listUsers();

    /**
     * 分页查询用户
     * 
     * @param pageNum 页码，从1开始
     * @param pageSize 每页大小，范围1-100
     * @return 分页结果
     */
    IPage<UserVO> pageUsers(
            @NotNull(message = "页码不能为空") Integer pageNum,
            @NotNull(message = "每页大小不能为空") Integer pageSize);

    /**
     * 创建用户
     * 
     * @param dto 用户创建请求，不能为空且需通过校验
     * @return 创建后的用户信息
     * @throws com.example.exception.BusinessException 当用户已存在时抛出
     */
    UserVO createUser(@Valid UserCreateDTO dto);

    /**
     * 更新用户
     * 
     * @param id 用户ID，不能为空
     * @param dto 用户更新请求，不能为空且需通过校验
     * @return 更新后的用户信息
     * @throws com.example.exception.BusinessException 当用户不存在时抛出
     */
    UserVO updateUser(
            @NotNull(message = "用户ID不能为空") Long id,
            @Valid UserUpdateDTO dto);

    /**
     * 删除用户
     * 
     * @param id 用户ID，不能为空
     * @throws com.example.exception.BusinessException 当用户不存在时抛出
     */
    void deleteUser(@NotNull(message = "用户ID不能为空") Long id);

    /**
     * 批量删除用户
     * 
     * @param ids 用户ID列表，不能为空且至少包含一个元素
     */
    void batchDeleteUsers(@NotNull(message = "用户ID列表不能为空") List<Long> ids);

    /**
     * 检查用户名是否存在
     * 
     * @param username 用户名，不能为空
     * @return 是否存在
     */
    boolean existsByUsername(@NotNull(message = "用户名不能为空") String username);
}
```

### 9.2 实现类规范

```java
package com.example.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.dto.UserCreateDTO;
import com.example.dto.UserUpdateDTO;
import com.example.entity.User;
import com.example.exception.BusinessException;
import com.example.exception.ErrorCode;
import com.example.mapper.UserMapper;
import com.example.service.UserService;
import com.example.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户服务实现类
 * 
 * @author author
 * @since 2024-01-01
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final UserMapper userMapper;

    @Override
    public UserVO getUserById(Long id) {
        log.info("查询用户, id: {}", id);
        
        User user = userMapper.selectById(id);
        if (user == null) {
            log.warn("用户不存在, id: {}", id);
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        
        return convertToVO(user);
    }

    @Override
    public List<UserVO> listUsers() {
        log.info("查询所有用户");
        
        List<User> users = userMapper.selectList(null);
        return users.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
    }

    @Override
    public IPage<UserVO> pageUsers(Integer pageNum, Integer pageSize) {
        log.info("分页查询用户, pageNum: {}, pageSize: {}", pageNum, pageSize);
        
        Page<User> page = new Page<>(pageNum, pageSize);
        IPage<User> userPage = userMapper.selectPage(page, null);
        
        return userPage.convert(this::convertToVO);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO createUser(UserCreateDTO dto) {
        log.info("创建用户, username: {}", dto.getUsername());
        
        // 检查用户是否已存在
        if (existsByUsername(dto.getUsername())) {
            log.warn("用户已存在, username: {}", dto.getUsername());
            throw new BusinessException(ErrorCode.USER_EXISTS);
        }
        
        // 转换并保存
        User user = convertToEntity(dto);
        userMapper.insert(user);
        
        log.info("用户创建成功, id: {}", user.getId());
        return convertToVO(user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO updateUser(Long id, UserUpdateDTO dto) {
        log.info("更新用户, id: {}", id);
        
        // 检查用户是否存在
        User existingUser = userMapper.selectById(id);
        if (existingUser == null) {
            log.warn("用户不存在, id: {}", id);
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        
        // 更新字段
        if (dto.getEmail() != null) {
            existingUser.setEmail(dto.getEmail());
        }
        if (dto.getAge() != null) {
            existingUser.setAge(dto.getAge());
        }
        
        userMapper.updateById(existingUser);
        
        log.info("用户更新成功, id: {}", id);
        return convertToVO(existingUser);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        log.info("删除用户, id: {}", id);
        
        if (!userMapper.existsById(id)) {
            log.warn("用户不存在, id: {}", id);
            throw new BusinessException(ErrorCode.USER_NOT_FOUND);
        }
        
        userMapper.deleteById(id);
        log.info("用户删除成功, id: {}", id);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteUsers(List<Long> ids) {
        log.info("批量删除用户, ids: {}", ids);
        
        if (CollectionUtils.isEmpty(ids)) {
            throw new BusinessException(ErrorCode.PARAM_NULL);
        }
        
        userMapper.deleteBatchIds(ids);
        log.info("批量删除用户成功, count: {}", ids.size());
    }

    @Override
    public boolean existsByUsername(String username) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(User::getUsername, username);
        return userMapper.exists(wrapper);
    }

    /**
     * 转换实体为VO
     */
    private UserVO convertToVO(User user) {
        if (user == null) {
            return null;
        }
        UserVO vo = new UserVO();
        vo.setId(user.getId());
        vo.setUsername(user.getUsername());
        vo.setEmail(user.getEmail());
        vo.setAge(user.getAge());
        vo.setCreateTime(user.getCreateTime());
        return vo;
    }

    /**
     * 转换DTO为实体
     */
    private User convertToEntity(UserCreateDTO dto) {
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(dto.getPassword()); // 实际应加密
        user.setAge(dto.getAge());
        return user;
    }
}
```

### 9.3 事务管理策略

| 场景 | 事务注解 | 说明 |
|-----|---------|------|
| 单表操作 | @Transactional(rollbackFor = Exception.class) | 默认传播行为 |
| 多表操作 | @Transactional(rollbackFor = Exception.class) | 确保事务一致性 |
| 只读操作 | @Transactional(readOnly = true) | 优化性能 |
| 需要新事务 | @Transactional(propagation = Propagation.REQUIRES_NEW) | 独立事务 |
| 嵌套事务 | @Transactional(propagation = Propagation.NESTED) | 嵌套事务 |

**事务配置示例：**

```java
@Configuration
public class TransactionConfig {

    @Bean
    public PlatformTransactionManager transactionManager(DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean
    public TransactionTemplate transactionTemplate(PlatformTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }
}
```

---

## 十、API 设计规范

### 10.1 HTTP 方法规范

| HTTP 方法 | CRUD 操作 | 幂等性 | 示例 |
|---------|---------|-------|------|
| GET | 读取（Read） | 是 | GET /api/users |
| POST | 创建（Create） | 否 | POST /api/users |
| PUT | 更新（Update） | 是 | PUT /api/users/{id} |
| DELETE | 删除（Delete） | 是 | DELETE /api/users/{id} |
| PATCH | 部分更新 | 是 | PATCH /api/users/{id} |

### 10.2 路径命名规范

| 资源类型 | 路径格式 | 示例 |
|---------|---------|------|
| 集合 | /api/{resource} | GET /api/users |
| 单个资源 | /api/{resource}/{id} | GET /api/users/1 |
| 子资源 | /api/{parent}/{parentId}/{child} | GET /api/users/1/orders |
| 操作 | /api/{resource}/{id}/{action} | POST /api/users/1/lock |

### 10.3 请求参数规范

| 参数位置 | 使用场景 | 示例 |
|---------|---------|------|
| Path | 资源标识 | /api/users/{id} |
| Query | 筛选、分页、排序 | /api/users?page=1&size=10 |
| Body | 复杂数据 | POST /api/users {JSON} |
| Header | 元数据、认证 | Authorization: Bearer token |

### 10.4 响应格式规范

**成功响应：**
```json
{
    "code": 200,
    "success": true,
    "message": "操作成功",
    "data": {
        "id": 1,
        "username": "test",
        "email": "test@example.com"
    },
    "timestamp": 1705307445123,
    "traceId": "a1b2c3d4-e5f6-7890"
}
```

**分页响应：**
```json
{
    "code": 200,
    "success": true,
    "message": "操作成功",
    "data": {
        "records": [...],
        "total": 100,
        "size": 10,
        "current": 1,
        "pages": 10
    },
    "timestamp": 1705307445123,
    "traceId": "a1b2c3d4-e5f6-7890"
}
```

**错误响应：**
```json
{
    "code": 20100,
    "success": false,
    "message": "用户不存在",
    "data": null,
    "timestamp": 1705307445123,
    "traceId": "a1b2c3d4-e5f6-7890"
}
```

---

## 十一、参数校验规范

### 11.1 DTO 校验示例

```java
package com.example.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

/**
 * 用户创建请求DTO
 */
@Data
public class UserCreateDTO {

    /**
     * 用户名
     * 必填，长度2-50字符
     */
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度必须在2-50之间")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "用户名只能包含字母、数字和下划线")
    private String username;

    /**
     * 邮箱
     * 必填，邮箱格式
     */
    @NotBlank(message = "邮箱不能为空")
    @Email(message = "邮箱格式不正确")
    private String email;

    /**
     * 密码
     * 必填，长度6-20字符
     */
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20之间")
    private String password;

    /**
     * 年龄
     * 可选，范围1-150
     */
    @Min(value = 1, message = "年龄必须大于0")
    @Max(value = 150, message = "年龄必须小于150")
    private Integer age;
}
```

### 11.2 常用校验注解

| 注解 | 说明 | 示例 |
|-----|------|------|
| @NotBlank | 非空（字符串） | @NotBlank(message = "不能为空") |
| @NotNull | 非空（对象） | @NotNull(message = "不能为空") |
| @NotEmpty | 非空（集合） | @NotEmpty(message = "列表不能为空") |
| @Size | 长度范围 | @Size(min = 2, max = 50) |
| @Min | 最小值 | @Min(value = 1) |
| @Max | 最大值 | @Max(value = 100) |
| @Email | 邮箱格式 | @Email(message = "邮箱格式错误") |
| @Pattern | 正则校验 | @Pattern(regexp = "^[a-z]+$") |
| @Valid | 嵌套校验 | @Valid UserCreateDTO dto |

### 11.3 全局异常处理

```java
package com.example.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Map<String, Object>> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", e.getErrorCode().getCode());
        response.put("success", false);
        response.put("message", e.getMessage());
        response.put("data", null);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidationException(MethodArgumentNotValidException e) {
        log.warn("参数校验失败: {}", e.getMessage());
        
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", ErrorCode.PARAM_ERROR.getCode());
        response.put("success", false);
        response.put("message", "参数校验失败");
        response.put("data", errors);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.ok(response);
    }

    /**
     * 处理系统异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        log.error("系统异常", e);
        
        Map<String, Object> response = new HashMap<>();
        response.put("code", ErrorCode.SYSTEM_ERROR.getCode());
        response.put("success", false);
        response.put("message", ErrorCode.SYSTEM_ERROR.getMessage());
        response.put("data", null);
        response.put("timestamp", System.currentTimeMillis());
        
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
```

---

## 十二、数据库规范

### 12.1 表设计规范

```sql
CREATE TABLE `t_user` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '用户ID',
    `username` VARCHAR(50) NOT NULL COMMENT '用户名',
    `email` VARCHAR(100) NOT NULL COMMENT '邮箱',
    `password` VARCHAR(255) NOT NULL COMMENT '密码（加密）',
    `age` INT NULL COMMENT '年龄',
    `status` TINYINT DEFAULT 1 COMMENT '状态（0-禁用，1-启用）',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `version` INT DEFAULT 1 COMMENT '版本号（乐观锁）',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_username` (`username`),
    KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
```

### 12.2 索引规范

| 索引类型 | 使用场景 | 示例 |
|---------|---------|------|
| 主键索引 | 主键字段 | PRIMARY KEY (id) |
| 唯一索引 | 唯一约束字段 | UNIQUE KEY (username) |
| 普通索引 | 查询条件字段 | KEY (create_time) |
| 复合索引 | 联合查询条件 | KEY (status, create_time) |

### 12.3 SQL 编写规范

```java
// 推荐：使用 MyBatis-Plus 链式 API
LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
wrapper.eq(User::getStatus, 1)
       .like(User::getUsername, keyword)
       .orderByDesc(User::getCreateTime);
List<User> users = userMapper.selectList(wrapper);

// 禁止：字符串拼接 SQL
// String sql = "SELECT * FROM t_user WHERE username = '" + username + "'";
```

---

## 十三、安全规范

### 13.1 密码安全

```java
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
public class PasswordService {
    
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    
    /**
     * 加密密码
     */
    public String encode(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
    
    /**
     * 验证密码
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
```

### 13.2 敏感数据脱敏

```java
public class DataMaskUtil {
    
    /**
     * 脱敏邮箱
     * test@example.com -> t***t@example.com
     */
    public static String maskEmail(String email) {
        if (email == null || !email.contains("@")) {
            return email;
        }
        String[] parts = email.split("@");
        if (parts[0].length() <= 2) {
            return email;
        }
        String prefix = parts[0].charAt(0) + "***" + parts[0].charAt(parts[0].length() - 1);
        return prefix + "@" + parts[1];
    }
    
    /**
     * 脱敏手机号
     * 13812345678 -> 138****5678
     */
    public static String maskPhone(String phone) {
        if (phone == null || phone.length() != 11) {
            return phone;
        }
        return phone.substring(0, 3) + "****" + phone.substring(7);
    }
}
```

---

## 十四、部署规范

### 14.1 配置分离

```bash
# application.yml - 通用配置
# application-dev.yml - 开发环境
# application-test.yml - 测试环境
# application-prod.yml - 生产环境
```

### 14.2 启动命令

```bash
# 开发环境
java -jar demo-1.0.0.jar --spring.profiles.active=dev

# 测试环境
java -jar demo-1.0.0.jar --spring.profiles.active=test

# 生产环境
java -jar demo-1.0.0.jar --spring.profiles.active=prod \
     --server.port=8080 \
     --spring.datasource.password=${DB_PASSWORD}
```

### 14.3 JVM 参数

```bash
java -jar demo-1.0.0.jar \
     -Xms512m \
     -Xmx1024m \
     -XX:+UseG1GC \
     -XX:MaxGCPauseMillis=200 \
     -XX:+HeapDumpOnOutOfMemoryError \
     -XX:HeapDumpPath=/var/log/demo/heapdump.hprof \
     -Dlogging.path=/var/log/demo
```

---

## 附录：常用命令

```bash
# 编译项目
mvn clean compile

# 运行测试
mvn test

# 打包项目（跳过测试）
mvn clean package -Dmaven.test.skip=true

# 运行应用
java -jar target/demo-1.0.0.jar

# 查看依赖树
mvn dependency:tree

# 代码格式化
mvn spotless:apply

# 检查代码风格
mvn spotless:check
```

---

**Structure Boot** - 让 Spring Boot 开发更简单、更高效！

[项目地址](https://github.com/structure-projects/structure-boot) | [文档中心](https://docs.structured.cn)
