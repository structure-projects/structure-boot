# 版本更新说明

## [1.4.0] - 2026-06-05

### 主要更新

本次版本更新主要完善了项目的文档体系，为所有子模块添加了详细的 README 文档，并补充了代码注释。

### 详细变更

#### 1. 新增 README 文档

为以下 10 个子模块创建或更新了 README 文档：

| 模块 | 说明 |
| :--- | :--- |
| `structure-common` | 通用工具模块，包含工具类、枚举、实体类等 |
| `structure-log-starter` | 日志处理模块，支持 AOP 日志和 JSON 格式日志 |
| `structure-minio-starter` | MinIO 对象存储模块 |
| `structure-redisson-starter` | Redisson 分布式锁和缓存模块 |
| `structure-mybatis-starter` | MyBatis 集成模块，支持分表 |
| `structure-mybatis-plus-starter` | MyBatis Plus 集成模块 |
| `structure-mybatis-plus-generate` | MyBatis Plus 代码生成器 |
| `structure-restful-web-starter` | RESTful Web 开发模块 |
| `structure-redis-starter` | Redis 集成模块，支持分布式锁 |
| `structure-rpc-starter` | RPC 远程服务调用模块 |

#### 2. README 文档内容

每个 README 文档包含以下内容：

- **功能特性**: 模块的核心功能介绍
- **快速开始**: 添加依赖、配置和基本使用示例
- **目录结构**: 模块的代码组织结构
- **核心组件**: 主要类和接口的说明
- **配置项说明**: 详细的配置参数说明
- **使用示例**: 具体的代码使用示例
- **返回结果格式**: 统一的 API 响应格式说明
- **许可证**: Apache License 2.0

#### 3. 代码注释补充

为以下文件添加了完整的注释和版权声明：

- `AuthType.java` - 认证类型枚举
- `TokenManager.java` - Token 管理器接口
- `TokenProvider.java` - Token 提供者接口
- `TokenInfo.java` - Token 信息实体类

#### 4. 统一响应格式说明

完善了返回结果格式的文档说明，包含：

**ResultVO<T>** - 微服务友好
- 支持系统级和业务级两级状态码
- 字段：`code`, `msg`, `subCode`, `subMsg`, `success`, `data`, `timestamp`

**ResResultVO<T>** - 单体应用友好
- 结构更简洁
- 字段：`code`, `message`, `success`, `data`, `timestamp`

**简单响应结果**
- `ResCountVO` - 计数响应（count 字段）
- `ResObjectIdVO` - ID响应（id 字段）

---

## 版本升级建议

### 升级到 1.4.0

1.4.0 版本主要涉及文档和代码注释的完善，不包含任何 API 变更或破坏性更改。

**升级步骤：**

```xml
<!-- 更新父模块版本 -->
<parent>
    <groupId>cn.structured</groupId>
    <artifactId>structure-dependencies</artifactId>
    <version>1.4.0</version>
</parent>
```

**兼容性：**
- ✅ 向后兼容 1.3.x 系列
- ✅ 无 API 变更
- ✅ 无破坏性更改

---

## 历史版本

- [1.3.7](./CHANGELOG.md#137---2026-05-28) - Bug 修复版本
- [1.3.6](./CHANGELOG.md#136---2026-05-23) - 依赖版本更新
- [1.3.5](./CHANGELOG.md#135---2026-05-23) - JDK 21 降级至 17
- [1.3.4](./CHANGELOG.md#134---2026-05-20) - Spring Boot 3 兼容性修复
- [1.3.3](./CHANGELOG.md#133---2026-05-18) - SpEL 表达式修复
- [1.3.0](./CHANGELOG.md#130---2026-05-01) - Spring Boot 3.2.x 升级
