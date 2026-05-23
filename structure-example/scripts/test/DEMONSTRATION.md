# Test Scripts Demonstration

本文档展示测试脚本的实际使用示例和预期输出。

## 示例 1: 快速启动

### 命令
```bash
cd /Users/chuck/projects/structure-projects/structure-boot/structure-example/scripts/test
./quickstart.sh
```

### 预期输出
```
=========================================
  Structure Boot - Quick Start
  Build & Test Everything
=========================================

[INFO] Step 1: Building all examples...

[SUCCESS] ✓ Build completed successfully

[INFO] Step 2: Running all tests...

=========================================
  Testing: Redis Example
=========================================
[INFO] Starting Redis Example Application...
[SUCCESS] Redis Example started successfully on port 16007

[INFO] Testing Redis operations...
[INFO] Test 1: Set and get key-value
[SUCCESS] ✓ Redis set/get operation successful: testkey

[INFO] Test 2: Multiple key-value operations
[SUCCESS] ✓ Operation 1 successful
[SUCCESS] ✓ Operation 2 successful
[SUCCESS] ✓ Operation 3 successful

[INFO] Testing Redis distributed lock...
[INFO] Test 1: Simple distributed lock
[SUCCESS] ✓ Simple lock acquired: this key is lock1

[INFO] Test 2: Lock with custom object
[SUCCESS] ✓ Object lock acquired: this key is lock2

[INFO] Testing: RESTful Web Example
=========================================
[INFO] Starting RESTful Web Example...
[SUCCESS] RESTful Web Example started successfully

[INFO] Test 1: API Health Check
[SUCCESS] ✓ API is responding (HTTP 200)

[INFO] Test 2: Swagger UI
[WARNING] Swagger UI not accessible

=========================================
  Test Summary
=========================================
[INFO] Total tests: 2
[SUCCESS] Passed: 2
[ERROR] Failed: 0

=========================================
  🎉 Congratulations!
  All examples built and tested successfully!
=========================================
```

## 示例 2: 单独测试 Redis

### 命令
```bash
./test-redis.sh start    # 启动应用
./test-redis.sh test     # 运行测试
./test-redis.sh stop     # 停止应用
```

### 启动输出
```
[INFO] Starting Redis Example Application...
[INFO] Application started with PID: 12345
[INFO] Waiting for startup (10 seconds)...
[SUCCESS] Redis Example started successfully on port 16007
```

### 测试输出
```
=========================================
  Redis Example - All Tests
=========================================
[INFO] Test 1: Set and get key-value
[SUCCESS] ✓ Redis set/get operation successful: testkey

[INFO] Test 2: Multiple key-value operations
[SUCCESS] ✓ Operation 1 successful
[SUCCESS] ✓ Operation 2 successful
[SUCCESS] ✓ Operation 3 successful

[INFO] Test 1: Simple distributed lock
[SUCCESS] ✓ Simple lock acquired: this key is lock1

[INFO] Test 2: Lock with custom object
[SUCCESS] ✓ Object lock acquired: this key is lock2

=========================================
All tests completed!
```

## 示例 3: 构建特定模块

### 命令
```bash
./build-examples.sh structure-redis-example
```

### 预期输出
```
[INFO] Building module: structure-redis-example
[SUCCESS] Module structure-redis-example built successfully!
```

## 示例 4: 列出可用测试

### 命令
```bash
./run-tests.sh list
```

### 预期输出
```
=========================================
  Structure Example Test Runner
=========================================

[INFO] Available test modules:

  - redis
  - restful-web
```

## 测试 API 端点

测试脚本会自动调用以下 API 端点进行验证：

### Redis Example (端口 16007)

| 端点 | 方法 | 参数 | 功能 |
|------|------|------|------|
| `/redis/getKey` | GET | `key` | 设置并获取 Redis 键值 |
| `/redis/redisLock` | GET | `key` | 测试简单分布式锁 |
| `/redis/redisLock1` | GET | `key` | 测试对象分布式锁 |

### RESTful Web Example (端口 16009)

| 端点 | 方法 | 功能 |
|------|------|------|
| `/` | GET | 应用根路径 |
| `/swagger-ui.html` | GET | Swagger UI 界面 |

## 日志文件

所有日志文件位于 `logs/` 目录：

```bash
# 构建日志
cat logs/build-examples.log

# Redis 测试日志
cat logs/redis-example.log

# RESTful Web 测试日志
cat logs/restful-web.log
```

## 故障排除

### 问题 1: Redis 连接失败

**错误信息:**
```
[ERROR] Failed to start Redis Example
```

**解决方案:**
1. 检查 Redis 服务器是否运行：
   ```bash
   redis-cli -h 172.24.20.15 -p 6379 -a 123456 ping
   ```

2. 如果 Redis 未运行，启动 Redis：
   ```bash
   redis-server --daemonize yes
   ```

3. 或修改 `application.yml` 中的 Redis 配置

### 问题 2: 端口被占用

**错误信息:**
```
[WARNING] Application is already running on port 16007
```

**解决方案:**
1. 停止占用端口的进程：
   ```bash
   # 查找进程
   lsof -i:16007
   
   # 停止进程
   kill <PID>
   ```

2. 或使用测试脚本的 stop 命令：
   ```bash
   ./test-redis.sh stop
   ```

### 问题 3: JAR 文件未找到

**错误信息:**
```
[ERROR] JAR file not found: .../structure-redis-example/target/...
```

**解决方案:**
1. 运行构建脚本：
   ```bash
   ./build-examples.sh all
   ```

## 扩展测试

### 添加新的测试模块

1. 创建测试脚本 `test-{module}.sh`
2. 实现 `start`, `stop`, `test` 函数
3. 更新 `run-tests.sh` 以包含新测试

**模板:**
```bash
#!/bin/bash

MODULE_NAME="your-module"
APP_PORT=160XX

start_module() {
    # 启动应用的逻辑
}

stop_module() {
    # 停止应用的逻辑
}

test_module() {
    # 测试逻辑
}

case "$1" in
    start) start_module ;;
    stop) stop_module ;;
    test) test_module ;;
esac
```

## 持续集成

这些脚本可以集成到 CI/CD 流程中：

```yaml
# .github/workflows/test.yml
name: Example Tests

on: [push, pull_request]

jobs:
  test:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 17
        uses: actions/setup-java@v2
        with:
          java-version: '17'
      - name: Run tests
        run: |
          cd structure-example/scripts/test
          ./quickstart.sh
```

## 支持

如有问题，请：
1. 查看日志文件
2. 检查依赖服务状态
3. 确认 Java 版本 (需要 JDK 17)
4. 联系项目维护者

---

**更新时间:** 2025-05-24  
**版本:** 1.0.0
