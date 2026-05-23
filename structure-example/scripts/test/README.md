# Structure Example Test Scripts

本目录包含用于测试 structure-boot 所有示例模块的脚本。

## 📁 目录结构

```
scripts/
└── test/
    ├── build-examples.sh       # 构建所有示例模块
    ├── test-all.sh            # 统一测试脚本（推荐）
    ├── test-boot.sh           # Boot 示例测试
    ├── test-log.sh            # Log 示例测试
    ├── test-redis.sh          # Redis 示例测试
    ├── test-restful-web.sh    # RESTful Web 示例测试
    ├── quickstart.sh          # 快速启动脚本
    ├── run-tests.sh           # 测试运行器
    ├── README.md              # 本文档
    ├── DEMONSTRATION.md       # 演示文档
    └── logs/                  # 测试日志目录
```

## 🚀 快速开始

### 方式 1: 一键测试所有模块（推荐）

```bash
cd scripts/test
./test-all.sh all
```

这将自动测试所有示例模块：
- ✅ Boot Example (端口 16001)
- ✅ Log Example (端口 16002)
- ✅ Minio Example (端口 16003)
- ✅ Redis Example (端口 16007)
- ✅ Redisson Example (端口 16008)
- ✅ RESTful Web Example (端口 16009)
- ✅ Tenant Example (端口 8080)

### 方式 2: 测试单个模块

```bash
# 测试 Boot 示例
./test-all.sh boot

# 测试 Log 示例
./test-all.sh log

# 测试 Redis 示例
./test-all.sh redis

# ... 其他模块类似
```

### 方式 3: 使用独立测试脚本

```bash
# Redis 测试
./test-redis.sh test

# RESTful Web 测试
./test-restful-web.sh test
```

## 📋 测试模块列表

| 模块 | 端口 | 测试内容 | 测试接口 |
|------|------|----------|----------|
| structure-boot-example | 16001 | 基本启动测试 | `/test` |
| structure-log-example | 16002 | AOP日志测试 | `/test/test` |
| structure-minio-example | 16003 | Minio存储测试 | `/my/minio/bucket` |
| structure-redis-example | 16007 | Redis操作和分布式锁 | `/redis/getKey` |
| structure-redisson-starter-example | 16008 | Redisson分布式对象 | `/redisson/health` |
| structure-restful-web-example | 16009 | RESTful API测试 | `/` |
| structure-tenant-example | 8080 | 多租户功能测试 | `/tenant/info` |

## 🔧 使用说明

### test-all.sh - 统一测试脚本

**推荐使用此脚本进行测试**

```bash
# 运行所有测试
./test-all.sh all

# 测试单个模块
./test-all.sh <module-name>
```

**可用模块名称：**
- `all` - 运行所有测试
- `boot` - Boot 示例
- `log` - Log 示例
- `minio` - Minio 示例
- `redis` - Redis 示例
- `redisson` - Redisson 示例
- `restful-web` - RESTful Web 示例
- `tenant` - Tenant 示例

### build-examples.sh - 构建脚本

```bash
# 构建所有示例
./build-examples.sh all

# 构建单个模块
./build-examples.sh structure-redis-example
```

### quickstart.sh - 快速启动

```bash
# 一键构建并测试
./quickstart.sh
```

## 📊 测试详情

### 1. Boot Example (端口 16001)

**测试内容：**
- 基本启动验证
- 健康检查接口

**测试接口：**
```bash
GET http://localhost:16001/test
```

**预期响应：**
```
ok
```

### 2. Log Example (端口 16002)

**测试内容：**
- AOP 日志记录
- 参数日志输出

**测试接口：**
```bash
POST http://localhost:16002/test/test
Content-Type: application/json

{
  "id": "test123",
  "name": "TestUser"
}
```

### 3. Minio Example (端口 16003)

**测试内容：**
- Minio 连接测试
- 存储桶操作

**测试接口：**
```bash
GET http://localhost:16003/my/minio/bucket
```

**注意：** 需要 Minio 服务器运行

### 4. Redis Example (端口 16007)

**测试内容：**
- Redis 基本操作（set/get）
- 分布式锁测试

**测试接口：**
```bash
# 基本操作
GET http://localhost:16007/redis/getKey?key=test

# 分布式锁
GET http://localhost:16007/redis/redisLock?key=lock1
```

**注意：** 需要 Redis 服务器运行

### 5. Redisson Example (端口 16008)

**测试内容：**
- Redisson 客户端连接
- 分布式对象操作

**测试接口：**
```bash
# 健康检查
GET http://localhost:16008/redisson/health

# 键值操作
GET http://localhost:16008/redisson/bucket/testkey/testvalue
GET http://localhost:16008/redisson/bucket/testkey
```

**注意：** 需要 Redis 服务器运行

### 6. RESTful Web Example (端口 16009)

**测试内容：**
- RESTful API 健康检查
- JSON 序列化

**测试接口：**
```bash
GET http://localhost:16009/
```

### 7. Tenant Example (端口 8080)

**测试内容：**
- 多租户上下文
- 租户识别

**测试接口：**
```bash
# 通过请求头传递租户ID
GET http://localhost:8080/tenant/info
Header: X-Tenant-Id: test-tenant

# 通过请求参数传递租户ID
GET http://localhost:8080/tenant/info?tenantId=test-tenant
```

## ⚙️ 前置条件

### 必需环境

1. **Java 17**
   ```bash
   export JAVA_HOME=/path/to/jdk-17
   java -version
   ```

2. **Maven 3.6+**
   ```bash
   mvn --version
   ```

### 依赖服务

部分测试需要以下服务运行：

1. **Redis** (用于 Redis 和 Redisson 测试)
   ```bash
   # 检查 Redis 状态
   redis-cli -h 172.24.20.15 -p 6379 -a 123456 ping
   
   # 或启动本地 Redis
   redis-server --daemonize yes
   ```

2. **Minio** (用于 Minio 测试)
   ```bash
   # 检查 Minio 状态
   curl http://10.16.105.146:9010/minio/health/live
   ```

## 📝 查看日志

所有测试日志位于 `logs/` 目录：

```bash
# 查看构建日志
cat logs/build-examples.log

# 查看各模块运行日志
cat logs/structure-boot-example.log
cat logs/structure-log-example.log
cat logs/structure-minio-example.log
cat logs/structure-redis-example.log
cat logs/structure-redisson-starter-example.log
cat logs/structure-restful-web-example.log
cat logs/structure-tenant-example.log
```

## 🔍 故障排除

### 问题 1: JAR 文件未找到

**错误信息：**
```
[ERROR] JAR file not found: .../target/...
```

**解决方案：**
```bash
# 先构建项目
./build-examples.sh all
```

### 问题 2: 端口被占用

**错误信息：**
```
[WARNING] Application is already running on port 16007
```

**解决方案：**
```bash
# 查找并停止占用端口的进程
lsof -i:16007
kill <PID>

# 或使用测试脚本的停止功能
./test-redis.sh stop
```

### 问题 3: Redis 连接失败

**错误信息：**
```
[ERROR] Failed to start Redis Example
```

**解决方案：**
1. 检查 Redis 是否运行
2. 检查 `application.yml` 中的 Redis 配置
3. 确保网络连接正常

### 问题 4: Java 版本不正确

**错误信息：**
```
[ERROR] Java 17 not found
```

**解决方案：**
```bash
# 设置 JAVA_HOME
export JAVA_HOME=/Users/chuck/.sdkman/candidates/java/17.0.12-oracle
export PATH=$JAVA_HOME/bin:$PATH
```

## 🎯 测试输出示例

```
=========================================
  Running All Example Tests
=========================================

[INFO] =========================================
[INFO] Testing: Boot Example (Port 16001)
[INFO] =========================================
[SUCCESS] Boot Example started successfully
[INFO] Test: Basic endpoint
[SUCCESS] ✓ Boot test passed: ok

[INFO] =========================================
[INFO] Testing: Redis Example (Port 16007)
[INFO] =========================================
[SUCCESS] Redis Example started successfully
[INFO] Test: Redis set/get
[SUCCESS] ✓ Redis test passed

=========================================
  Test Summary
=========================================
[INFO] Total tests: 7
[SUCCESS] Passed: 7
[SUCCESS] 🎉 All tests passed!
```

## 📚 相关文档

- **DEMONSTRATION.md** - 详细演示和示例
- **项目根目录 README.md** - 项目整体说明

## 🤝 贡献

欢迎添加新的测试模块或改进现有测试！

**添加新测试步骤：**
1. 在 `scripts/test/` 创建测试脚本
2. 实现测试逻辑
3. 更新 `test-all.sh` 包含新测试
4. 更新本文档

---

**更新时间：** 2025-05-24  
**版本：** 2.0.0  
**维护者：** Structure Boot Team
