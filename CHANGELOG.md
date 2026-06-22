# Changelog

所有重要的项目变更都会记录在此文档中。本文档是变更日志索引，详细变更内容请查看对应的版本文档。

## 文档说明

**文档结构：**

```
docs/
├── v1.4.2/                      # v1.4.2 版本文档 (当前最新)
│   ├── README.md               # 版本概述
│   ├── COMPONENT_GUIDE.md      # 组件使用指南
│   └── CHANGELOG.md            # 详细变更日志
├── v1.4.1/                      # v1.4.1 版本文档
│   ├── README.md               # 版本概述
│   └── CHANGELOG.md            # 详细变更日志
├── v1.4.0/                      # v1.4.0 版本文档
│   ├── README.md               # 版本概述
│   └── CHANGELOG.md            # 详细变更日志
└── archive/
    ├── 1.3.X.md               # 1.3.X 版本指南
    ├── historical-summary.md   # 历史版本汇总 (1.0.x - 1.2.x)
    └── README.md               # 版本文档索引
```

---

## 版本格式

版本号遵循语义化版本控制（Semantic Versioning）：

- **主版本号（MAJOR）**：不兼容的 API 更改
- **次版本号（MINOR）**：向后兼容的功能新增
- **修订号（PATCH）**：向后兼容的问题修复

---

## 最新版本

### [1.4.2] - 2026-06-23

**版本状态：** 当前最新版本

**变更类型：** 兼容性修复 + 安全修复

**变更摘要：**

- Spring Boot 4.x 兼容性修复
- 安全漏洞修复 (CVE-2026-0636, CVE-2025-48976)

**[查看详细变更](docs/v1.4.2/CHANGELOG.md)**

---

## 版本索引

| 版本 | 日期 | 类型 | 变更摘要 |
| :--- | :--- | :--- | :--- |
| **[1.4.2](docs/v1.4.2/)** | 2026-06-23 | 兼容性修复 | Spring Boot 4.x 兼容性修复、安全漏洞修复 |
| **[1.4.1](docs/v1.4.1/)** | 2026-06-15 | 依赖升级 | Spring Boot 4.0.6、Knife4j 4.5.0 |
| **[1.4.0](docs/v1.4.0/)** | 2026-06-05 | 文档完善 | README 文档体系完善、代码注释补充 |
| **[1.3.X](docs/archive/1.3.X.md)** | 2026-05-01 | 重大升级 | Spring Boot 3.2.x / JDK 21 |
| **[历史版本汇总](docs/archive/historical-summary.md)** | - | 历史 | 1.0.x - 1.2.x 版本汇总 |

---

## 变更类型索引

### Bug 修复

- [1.4.2](docs/v1.4.2/CHANGELOG.md) - Spring Boot 4.x 兼容性修复
- [1.4.1](docs/v1.4.1/CHANGELOG.md) - 兼容性问题修复
- [1.3.7](docs/archive/1.3.X.md#137---2026-05-28) - 打包依赖和自动装配问题
- [1.3.4](docs/archive/1.3.X.md#134---2026-05-20) - SpEL 表达式和 Redis Lock 修复

### 功能新增

- [1.4.0](docs/v1.4.0/CHANGELOG.md) - 文档体系完善
- [1.3.1](docs/archive/1.3.X.md#131---2026-05-10) - 多租户支持和 RPC 调用
- [1.2.0](docs/archive/historical-summary.md#120---2026-01-01) - MinIO、Redisson、日志模块
- [1.1.0](docs/archive/historical-summary.md#110---2025-06-01) - Redis 缓存和 MyBatis Plus

### 依赖升级

- [1.4.2](docs/v1.4.2/CHANGELOG.md) - springdoc 3.0.3、testcontainers 1.20.6 等
- [1.4.1](docs/v1.4.1/CHANGELOG.md) - Spring Boot 4.0.6、Knife4j 4.5.0
- [1.3.6](docs/archive/1.3.X.md#136---2026-05-23) - Spring Boot 4.0.6
- [1.3.0](docs/archive/1.3.X.md#130---2026-05-01) - Spring Boot 3.2.x / JDK 21

### 重大升级

- [1.3.0](docs/archive/1.3.X.md#130---2026-05-01) - Spring Boot 3.2.x / JDK 21 / Spring Security 6.2.x
- [1.2.0](docs/archive/historical-summary.md#120---2026-01-01) - Spring Boot 2.7.x

---

## 模块变更索引

### structure-common

- [1.4.0](docs/v1.4.0/CHANGELOG.md) - 代码注释补充

### structure-restful-web-starter

- [1.4.2](docs/v1.4.2/CHANGELOG.md) - GlobalControllerAdvice 修复、FastJsonHttpMessageConverters 修复

### structure-mybatis-starter

- [1.4.2](docs/v1.4.2/CHANGELOG.md) - MybatisProperties 配置优化
- [1.3.7](docs/archive/1.3.X.md#137---2026-05-28) - 自动装配问题修复

### structure-rpc-starter

- [1.4.2](docs/v1.4.2/CHANGELOG.md) - RpcProperties 配置优化

### structure-redis-starter

- [1.3.4](docs/archive/1.3.X.md#134---2026-05-20) - SpEL 表达式和 Redis Lock 修复

### structure-redisson-starter

- [1.3.4](docs/archive/1.3.X.md#134---2026-05-20) - SpEL 表达式修复

---

## 快速导航

### 按类型查找

| 类型 | 可查看版本 |
| :--- | :--- |
| 安全修复 | [1.4.2](docs/v1.4.2/CHANGELOG.md#4-安全漏洞修复) |
| 兼容性修复 | [1.4.2](docs/v1.4.2/CHANGELOG.md#1-spring-boot-4x-兼容性修复) |
| 依赖升级 | [1.4.1](docs/v1.4.1/CHANGELOG.md#1-依赖版本更新) |
| 重大升级 | [1.3.0](docs/archive/1.3.X.md#130---2026-05-01) |

### 按组件查找

| 组件 | 可查看版本 |
| :--- | :--- |
| structure-restful-web-starter | [v1.4.2](docs/v1.4.2/COMPONENT_GUIDE.md) |
| structure-mybatis-starter | [v1.4.2](docs/v1.4.2/COMPONENT_GUIDE.md) |
| structure-mybatis-plus-starter | [v1.4.2](docs/v1.4.2/COMPONENT_GUIDE.md) |
| structure-redis-starter | [v1.4.2](docs/v1.4.2/COMPONENT_GUIDE.md) |
| structure-redisson-starter | [v1.4.2](docs/v1.4.2/COMPONENT_GUIDE.md) |
| structure-minio-starter | [v1.4.2](docs/v1.4.2/COMPONENT_GUIDE.md) |
| structure-log-starter | [v1.4.2](docs/v1.4.2/COMPONENT_GUIDE.md) |
| structure-rpc-starter | [v1.4.2](docs/v1.4.2/COMPONENT_GUIDE.md) |

---

## 版本升级指南

### 从 1.4.x 升级到 1.4.2

```xml
<parent>
    <groupId>cn.structured</groupId>
    <artifactId>structure-dependencies</artifactId>
    <version>1.4.2</version>
</parent>
```

### 从 1.3.x 升级到 1.4.2

```xml
<parent>
    <groupId>cn.structured</groupId>
    <artifactId>structure-dependencies</artifactId>
    <version>1.4.2</version>
</parent>
```

**注意：** 1.4.x 系列无破坏性更改，1.3.x 用户可直接升级。

### 从 1.2.x 升级到 1.3.x

**主要变更：**

- Spring Boot 2.x → 3.2.x
- JDK 8/11 → 17/21
- Jakarta EE 8 → Jakarta EE 9+

**注意事项：**

1. 数据库驱动可能需要更换（如 `mysql-connector-java` → `mysql-connector-j`）
2. `spring.redis` 配置迁移至 `spring.data.redis`
3. 部分 SpEL 表达式语法可能有变化
4. 需要使用 `jakarta.*` 包名替代 `javax.*`

---

## 相关文档

- [用户开发指南](./USER_GUIDE.md) - 快速开始和开发指南
- [组件指南](./docs/v1.4.2/COMPONENT_GUIDE.md) - 各组件详细说明

---

## 更新日志

| 日期 | 更新内容 |
| :--- | :--- |
| 2026-06-23 | 优化文档结构，整合 CHANGELOG 和 VERSION_UPDATE |
| 2026-06-23 | 新增 1.4.2 版本记录 |
| 2026-06-23 | 优化文档结构，创建 docs/archive/ 目录 |
| 2026-06-15 | 新增 1.4.1 版本记录 |
| 2026-06-05 | 新增 1.4.0 版本记录 |
