# Structure Restful Web Starter

Structure Restful Web Starter 是 Structure Boot 框架的 RESTful Web 开发模块，提供统一的异常处理、响应封装和跨域配置等功能。

## 功能特性

- **统一异常处理**: 全局异常捕获和统一响应格式
- **统一响应封装**: 统一的 API 响应格式
- **跨域配置**: 自动配置 CORS
- **参数校验**: 支持 JSR-303 参数校验
- **Web配置**: 自动配置 Web 相关组件

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-restful-web-starter</artifactId>
    <version>${version}</version>
</dependency>
```

### 2. 配置跨域

在 `application.yml` 中添加配置：

```yaml
structure:
  web:
    cors:
      enabled: true
      allowed-origins:
        - http://localhost:8080
        - http://localhost:3000
      allowed-methods:
        - GET
        - POST
        - PUT
        - DELETE
        - OPTIONS
      allowed-headers:
        - Content-Type
        - Authorization
      max-age: 3600
```

### 3. 创建Controller

```java
@RestController
@RequestMapping("/api/users")
public class UserController {
    
    @GetMapping("/{id}")
    public ResultVO<User> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        return ResultVO.success(user);
    }
    
    @PostMapping
    public ResultVO<User> createUser(@Valid @RequestBody UserCreateRequest request) {
        User user = userService.createUser(request);
        return ResultVO.success("创建成功", user);
    }
}
```

## 目录结构

```
structure-restful-web-starter/
├── src/main/resources/
│   ├── application.properties
│   └── META-INF/
│       ├── spring/
│       │   └── org.springframework.boot.autoconfigure.AutoConfiguration.imports
│       └── spring.factories
```

## 统一响应格式

项目提供两种返回结果 VO，适用于不同场景：

### 1. ResultVO<T> - 微服务友好

对微服务比较友好，支持系统级和业务级两级状态码。

**成功响应：**
```json
{
  "code": "SUCCESS",
  "msg": "成功！",
  "subCode": "SUCCESS",
  "subMsg": "成功！",
  "success": true,
  "data": {
    "id": 1,
    "name": "John"
  },
  "timestamp": 112345644446
}
```

**失败响应：**
```json
{
  "code": "FAIL",
  "msg": "系统错误！",
  "subCode": "PARAM_ERROR",
  "subMsg": "参数校验失败",
  "success": false,
  "data": null,
  "timestamp": 112345644446
}
```

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| `code` | String | 系统级别的CODE码 |
| `msg` | String | 系统级别的消息内容 |
| `subCode` | String | 业务级别的code码 |
| `subMsg` | String | 业务级别的消息内容 |
| `success` | Boolean | 业务是否成功 |
| `data` | T | 响应数据（泛型） |
| `timestamp` | Long | 系统响应的时间戳 |

### 2. ResResultVO<T> - 单体应用友好

对单体应用比较友好，结构更简洁。

**成功响应：**
```json
{
  "code": "SUCCESS",
  "message": "成功！",
  "success": true,
  "data": {
    "id": 1,
    "name": "John"
  },
  "timestamp": 112345644446
}
```

**失败响应：**
```json
{
  "code": "FAIL",
  "message": "参数错误！",
  "success": false,
  "data": null,
  "timestamp": 112345644446
}
```

| 字段 | 类型 | 说明 |
| :--- | :--- | :--- |
| `code` | String | 业务状态码 |
| `message` | String | 返回的消息内容 |
| `success` | Boolean | 业务是否成功 |
| `data` | T | 响应数据（泛型） |
| `timestamp` | Long | 系统响应的时间戳 |

## 统一异常处理

### 全局异常处理器

模块内置了全局异常处理器，自动捕获以下异常：

| 异常类型 | HTTP状态码 | 说明 |
| :--- | :--- | :--- |
| `CommonException` | 自定义状态码 | 业务异常 |
| `MethodArgumentNotValidException` | 400 | 参数校验失败 |
| `HttpMessageNotReadableException` | 400 | 请求体解析失败 |
| `ResourceNotFoundException` | 404 | 资源未找到 |
| `Exception` | 500 | 未知异常 |

### 自定义异常

```java
throw new CommonException(ErrorCodeEnum.PARAM_ERROR, "参数错误");
```

## 参数校验

### 使用注解

```java
public class UserCreateRequest {
    
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 50, message = "用户名长度必须在2-50之间")
    private String username;
    
    @Email(message = "邮箱格式不正确")
    private String email;
    
    @NotNull(message = "年龄不能为空")
    @Min(value = 18, message = "年龄必须大于等于18")
    private Integer age;
}
```

## 配置项说明

### CORS配置

| 配置项 | 类型 | 说明 | 默认值 |
| :--- | :--- | :--- | :--- |
| `structure.web.cors.enabled` | Boolean | 是否启用CORS | `true` |
| `structure.web.cors.allowed-origins` | List | 允许的源 | `*` |
| `structure.web.cors.allowed-methods` | List | 允许的HTTP方法 | `*` |
| `structure.web.cors.allowed-headers` | List | 允许的请求头 | `*` |
| `structure.web.cors.max-age` | Integer | 预检请求缓存时间（秒） | `3600` |

## 许可证

Apache License 2.0