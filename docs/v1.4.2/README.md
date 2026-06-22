# Structure Boot v1.4.2

## 版本信息

- **版本号**: 1.4.2
- **发布日期**: 2026-06-23
- **父版本**: 1.4.1

---

## 文档目录

| 文档 | 说明 |
| :--- | :--- |
| [README.md](./README.md) | 版本概述、快速开始、环境要求 |
| [COMPONENT_GUIDE.md](./COMPONENT_GUIDE.md) | 各组件详细使用说明 |
| [CHANGELOG.md](./CHANGELOG.md) | 版本详细变更记录 |

---

## 版本概述

v1.4.2 是针对 Spring Boot 4.x 的重大兼容性修复版本，主要解决框架在 Spring Boot 4.x 环境下的多个兼容性问题，并包含重要的安全漏洞修复。

### 主要更新

- **Spring Boot 4.x 兼容性修复**
  - GlobalControllerAdvice 注解修复
  - ExceptionHandler 处理顺序修复
  - FastJsonHttpMessageConverters ImportSelector 修复
  - MybatisProperties/RpcProperties 配置优化

- **安全漏洞修复**
  - CVE-2026-0636 (BouncyCastle 1.84)
  - CVE-2025-48976 (commons-fileupload 1.6.0)

- **依赖升级**
  - springdoc-openapi: 2.3.0 → 3.0.3
  - knife4j-openapi3-jakarta: 4.5.0
  - testcontainers: 1.20.6
  - h2: 2.2.224

---

## 快速开始

### 环境要求

| 要求 | 版本 |
| :--- | :--- |
| JDK | 17+ |
| Maven | 3.6+ |
| Spring Boot | 4.0.6 |

### 基础配置

在 `pom.xml` 中添加依赖管理：

```xml
<parent>
    <groupId>cn.structured</groupId>
    <artifactId>structure-dependencies</artifactId>
    <version>1.4.2</version>
</parent>
```

---

## 相关文档

| 文档 | 说明 |
| :--- | :--- |
| [变更日志](../../CHANGELOG.md) | 项目变更日志索引 |
| [用户开发指南](../../USER_GUIDE.md) | 快速开始和开发指南 |
| [历史版本](../../docs/versions/) | 早期版本文档 |

---

## 更新日志

| 日期 | 更新内容 |
| :--- | :--- |
| 2026-06-23 | 发布 v1.4.2 版本 |
