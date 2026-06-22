# 版本文档索引

本文档是 Structure Boot 项目的版本文档索引，提供各版本详细文档的导航。

---

## 版本文档结构

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
└── versions/
    ├── 1.3.X.md               # 1.3.X 版本指南 (Spring Boot 3.x)
    ├── historical-summary.md   # 历史版本汇总 (1.0.x - 1.2.x)
    └── README.md               # 版本文档索引
```

---

## 当前版本

### [v1.4.2](./v1.4.2/README.md)

**版本状态**: ✅ 当前最新版本

**主要内容**:
- Spring Boot 4.x 兼容性修复
- 安全漏洞修复 (CVE-2026-0636, CVE-2025-48976)
- 组件使用指南

**[查看版本文档](./v1.4.2/README.md)**

---

## 版本索引

| 版本 | 发布日期 | 类型 | 文档位置 |
| :--- | :--- | :--- | :--- |
| **[v1.4.2](./v1.4.2/)** | 2026-06-23 | 兼容性修复 | Spring Boot 4.x 兼容性修复、安全漏洞修复 |
| **[v1.4.1](./v1.4.1/)** | 2026-06-15 | 依赖升级 | Spring Boot 4.0.6、Knife4j 4.5.0 |
| **[v1.4.0](./v1.4.0/)** | 2026-06-05 | 文档完善 | README 文档体系完善、代码注释补充 |
| **[1.3.X](./1.3.X.md)** | 2026-05-01 | 重大升级 | Spring Boot 3.2.x / JDK 21 |
| **[历史版本](./historical-summary.md)** | - | 历史 | 1.0.x - 1.2.x 版本汇总 |

---

## 快速导航

### 按类型查找

| 类型 | 可查看版本 |
| :--- | :--- |
| 安全修复 | [v1.4.2](./v1.4.2/CHANGELOG.md#4-安全漏洞修复) |
| 兼容性修复 | [v1.4.2](./v1.4.2/CHANGELOG.md#1-spring-boot-4x-兼容性修复) |
| 依赖升级 | [v1.4.1](./v1.4.1/CHANGELOG.md#1-依赖版本更新) |
| 重大升级 | [1.3.X](./1.3.X.md#130---2026-05-01) |

### 按组件查找

| 组件 | 可查看版本 |
| :--- | :--- |
| structure-restful-web-starter | [v1.4.2](./v1.4.2/COMPONENT_GUIDE.md#structure-restful-web-starter) |
| structure-mybatis-starter | [v1.4.2](./v1.4.2/COMPONENT_GUIDE.md#structure-mybatis-starter) |
| structure-mybatis-plus-starter | [v1.4.2](./v1.4.2/COMPONENT_GUIDE.md#structure-mybatis-plus-starter) |
| structure-redis-starter | [v1.4.2](./v1.4.2/COMPONENT_GUIDE.md#structure-redis-starter) |
| structure-redisson-starter | [v1.4.2](./v1.4.2/COMPONENT_GUIDE.md#structure-redisson-starter) |
| structure-minio-starter | [v1.4.2](./v1.4.2/COMPONENT_GUIDE.md#structure-minio-starter) |
| structure-log-starter | [v1.4.2](./v1.4.2/COMPONENT_GUIDE.md#日志组件) |
| structure-rpc-starter | [v1.4.2](./v1.4.2/COMPONENT_GUIDE.md#structure-rpc-starter) |

---

## 相关文档

| 文档 | 说明 |
| :--- | :--- |
| [变更日志](../../CHANGELOG.md) | 项目变更日志索引 |
| [用户开发指南](../../USER_GUIDE.md) | 快速开始和开发指南 |

---

## 更新日志

| 日期 | 更新内容 |
| :--- | :--- |
| 2026-06-23 | 优化文档结构，创建 v1.4.x 版本文档夹 |
| 2026-06-23 | 新增 v1.4.2 版本文档 |
| 2026-06-23 | 新增 1.3.X 版本指南 |
| 2026-06-23 | 更新历史版本汇总为 1.0.x - 1.2.x |
