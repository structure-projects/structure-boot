# Changelog

所有重要的项目变更都会记录在此文档中。

## 版本格式

版本号遵循语义化版本控制（Semantic Versioning）：
- **主版本号（MAJOR）**：不兼容的 API 更改
- **次版本号（MINOR）**：向后兼容的功能新增
- **修订号（PATCH）**：向后兼容的问题修复

---
## [1.3.6] - 2026-05-23

### 变更内容

**Bug 修复**
- 修正父项目中的依赖版本被意外移除导致子工程的项目无法使用父工程配置进行发布
---

## [1.3.5] - 2026-05-23

### 变更内容

**Bug 修复**
- JDK 21 降级至 17
- 添加版本变更说明文档
---

## [1.3.4] - 2026-05-20

### 变更内容

**Bug 修复**
- 修复 tk-mapper 支持 Spring Boot 3 的兼容性问题
- 修复 Spring Boot 3 文档和兼容性检查
- 修复 Redisson 模块：Spring Boot 3 无法解析 SpEL 表达式导致缓存和 Redis 锁失效的问题
- 修复 Redis 模块：Spring Boot 3 无法解析 SpEL 表达式
- 修复 Redis Lock 无法上锁和上锁失败的问题

---

## [1.3.5] - 2026-05-23

### 变更内容

**核心升级**
- 将项目 JDK 版本从 21 降级至 17，提升兼容性
- 统一更新所有模块的版本号至 1.3.5

**配置更新**
- 更新 `structure-dependencies/pom.xml` 中的 JDK 配置
- 更新 CI/CD 流水线配置（`.github/workflows/release.yml`）
- 更新示例项目的依赖版本

**文档更新**
- 更新 README.md 中的版本说明和环境要求
- 更新 QUICKSTART.md 中的 JDK 版本要求
- 更新 PROJECT_SPECIFICATION.md 中的技术栈说明
- 更新 COMPONENT_GUIDE.md 中的环境要求

---

## [1.3.4] - 未发布

### 变更内容

- 预留版本

---

## [1.3.3] - 2026-05-18

### 变更内容

**Bug 修复**
- 修复 SpEL 表达式参数解析问题，添加三层回退机制

**配置更新**
- 在 `structure-boot-parent/pom.xml` 中添加 `<parameters>true</parameters>` 配置
- 更新 Redis 配置从 `spring.redis` 迁移至 `spring.data.redis`

---

## [1.3.2] - 2026-05-15

### 变更内容

**功能增强**
- 优化分布式锁实现
- 增强 Redis 缓存功能

---

## [1.3.1] - 2026-05-10

### 变更内容

**功能新增**
- 添加多租户支持模块
- 添加 RPC 调用支持

**Bug 修复**
- 修复 MyBatis Plus 分页查询问题

---

## [1.3.0] - 2026-05-01

### 变更内容

**重大升级**
- 升级至 Spring Boot 3.2.x
- 升级至 Spring Security 6.2.x
- 升级至 JDK 21

**模块重构**
- 重构 Restful Web Starter
- 重构 MyBatis Starter
- 重构 Redis Starter

---

## [1.2.13] - 2026-04-25

### 变更内容

**Bug 修复**
- 修复 Redis Lock 无法上锁和上锁失败的问题
- 调整兼容性，修复部分名称使用默认名称导致 bean 名称重复的问题

---

## [1.2.11] - 2026-04-15

### 变更内容

**Bug 修复**
- 修复日志记录器的性能问题
- 修复 MinIO 存储模块的连接池问题

---

## [1.2.0] - 2026-01-01

### 变更内容

**功能新增**
- 添加 MinIO 对象存储支持
- 添加 Redisson 分布式锁支持
- 添加日志记录模块

**技术升级**
- 升级至 Spring Boot 2.7.x

---

## [1.1.0] - 2025-06-01

### 变更内容

**功能新增**
- 添加 Redis 缓存支持
- 添加 MyBatis Plus 支持

---

## [1.0.0] - 2025-01-01

### 变更内容

**初始版本**
- 项目初始化
- 基础模块结构搭建
- RESTful Web 支持
- MyBatis 支持
