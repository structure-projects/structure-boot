# Structure Common

Structure Common 是 Structure Boot 框架的通用工具模块，提供常用的工具类、枚举类型、实体类和异常处理。

## 功能特性

- **工具类**: 提供日期处理、字符串处理、HTTP客户端、结果工具等常用工具
- **枚举类型**: 提供错误码、结果码、日志类型等通用枚举
- **实体类**: 提供统一的返回结果、分页对象等基础实体
- **异常处理**: 提供统一的异常类和错误处理机制

## 目录结构

```
structure-common/
├── src/main/java/cn/structure/common/
│   ├── constant/          # 常量定义
│   ├── entity/            # 实体类
│   ├── enums/             # 枚举类型
│   ├── exception/         # 异常处理
│   ├── utils/             # 工具类
│   └── vo/                # 视图对象
```

## 核心组件

### 工具类

| 工具类 | 说明 |
| :--- | :--- |
| `DateUtil` | 日期时间处理工具 |
| `StringUtil` | 字符串处理工具 |
| `HttpClientUtil` | HTTP客户端工具 |
| `IResultUtil` | 返回结果工具接口 |
| `ResultUtilSimpleImpl` | 简单结果工具实现 |
| `ResultUtilSecondLevelImpl` | 二级分类结果工具实现 |
| `BasicAuthGenerator` | Basic认证生成器 |

### 枚举类型

| 枚举类 | 说明 |
| :--- | :--- |
| `ErrorCodeEnum` | 错误码枚举 |
| `ResultCodeEnum` | 结果码枚举 |
| `LogEnums` | 日志类型枚举 |
| `GenderEnum` | 性别枚举 |
| `WeekEnum` | 星期枚举 |
| `NumberEnum` | 数字枚举 |
| `ExceptionRsType` | 异常响应类型 |

### 实体类

| 实体类 | 说明 |
| :--- | :--- |
| `ResultVO<T>` | 统一返回结果（微服务友好） |
| `ResResultVO<T>` | 通用响应结果（单体应用友好） |
| `ResCountVO` | 计数响应结果 |
| `ResObjectIdVO` | 对象ID响应结果 |
| `IResult` | 结果接口 |
| `BaseLog` | 日志基类 |
| `ControllerLog` | 控制器日志 |
| `FunctionLog` | 函数日志 |
| `VerificationFailedMsg` | 验证失败消息 |

## 返回结果格式

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
  "subCode": "BUSINESS_ERROR",
  "subMsg": "业务处理失败",
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

### 3. 简单响应结果

**ResCountVO - 计数响应：**
```json
{
  "count": 100
}
```

**ResObjectIdVO - ID响应：**
```json
{
  "id": 1
}
```

### 视图对象

| VO类 | 说明 |
| :--- | :--- |
| `ReqPage` | 分页请求对象 |
| `ResPage` | 分页响应对象 |

## 使用示例

### 返回结果工具

```java
// 成功响应
ResultVO result = ResultUtilSimpleImpl.success("操作成功");

// 成功响应（带数据）
ResultVO result = ResultUtilSimpleImpl.success("操作成功", data);

// 失败响应
ResultVO result = ResultUtilSimpleImpl.error(ErrorCodeEnum.SYSTEM_ERROR);

// 分页响应
ResPage<T> page = new ResPage<>();
page.setList(dataList);
page.setTotal(total);
```

### 日期工具

```java
// 格式化日期
String dateStr = DateUtil.formatDate(new Date());

// 解析日期
Date date = DateUtil.parseDate("2024-01-01");

// 获取当前时间戳
long timestamp = DateUtil.getCurrentTimestamp();
```

### 字符串工具

```java
// 检查字符串是否为空
boolean empty = StringUtil.isEmpty(str);

// 生成随机字符串
String random = StringUtil.randomString(10);
```

### HTTP客户端

```java
// 发送GET请求
String response = HttpClientUtil.get("http://api.example.com/data");

// 发送POST请求
String response = HttpClientUtil.post("http://api.example.com/data", body);
```

## 许可证

Apache License 2.0