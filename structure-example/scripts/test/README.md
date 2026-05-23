# Structure Example Test Scripts

本目录包含用于测试 structure-boot 示例模块的脚本。所有示例模块均已实现**完整的功能测试**，不仅测试接口可达，还深入验证组件的核心功能。

## 📁 目录结构

```
scripts/
└── test/
    ├── build-examples.sh       - 构建所有示例模块
    ├── test-all.sh            - 统一测试脚本（推荐使用）
    ├── quickstart.sh          - 快速启动脚本
    ├── README.md              - 本文档
    ├── DEMONSTRATION.md       - 演示文档
    ├── TEST_IMPLEMENTATION_SUMMARY.md - 测试用例实现总结
    └── logs/                  - 测试日志目录
```

## 🚀 快速开始

### 方式 1: 一键测试所有模块（推荐）

```bash
cd scripts/test
./test-all.sh all
```

### 方式 2: 测试单个模块

```bash
./test-all.sh boot          - 测试 Boot Example
./test-all.sh log           - 测试 Log Example
./test-all.sh minio         - 测试 Minio Example
./test-all.sh mybatis       - 测试 MyBatis Example
./test-all.sh mybatis-plus  - 测试 MyBatis Plus Example
./test-all.sh tk-mapper     - 测试 Tk Mapper Example
./test-all.sh redis         - 测试 Redis Example
./test-all.sh redisson      - 测试 Redisson Example
./test-all.sh restful-web   - 测试 RESTful Web Example
./test-all.sh tenant        - 测试 Tenant Example
./test-all.sh rpc           - 测试 RPC Example
```

### 方式 3: 手动测试接口

每个模块都提供了丰富的测试接口，可以直接通过HTTP请求进行手动测试：

```bash
# 测试 Tk Mapper 的完整CRUD
curl http://localhost:16006/tk/test/fullCrud

# 测试 MyBatis 的所有功能
curl http://localhost:16004/mybatis/test/all

# 测试 MyBatis Plus 的完整CRUD
curl http://localhost:16005/mybatis-plus/test/fullCrud

# 测试 Redis 的所有功能
curl http://localhost:16007/redis/test/all

# 测试 Redisson 的所有功能
curl http://localhost:16008/redisson/test/all

# 测试 RPC 的所有功能
curl http://localhost:16010/rpc/test/all
```

### 方式 4: 查看所有可用测试

```bash
./test-all.sh
```

## 📋 测试模块详细信息

| 模块 | 端口 | 测试内容 | 测试用例数 | 测试控制器 |
|------|------|----------|----------|----------|
| structure-boot-example | 16001 | 基本启动验证 | 2 | [BootTestController](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-boot-example/src/main/java/cn/structure/example/controller/TestController.java) |
| structure-log-example | 16002 | AOP日志功能测试 | 3 | [LogTestController](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-log-example/src/main/java/cn/structure/example/controller/LogTestController.java) |
| structure-minio-example | 16003 | Minio存储功能测试 | 2 | [MinioTestController](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-minio-example/src/main/java/cn/structure/example/controller/MinioTestController.java) |
| structure-mybatis-example | 16004 | MyBatis数据库操作测试 - 完整CRUD流程 | 9 | [MyBatisTestController](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-mybatis-starter-example/structure-mybatis-example/src/main/java/cn/structure/example/mybatis/controller/MyBatisTestController.java) |
| structure-mybatis-plus-starter-example | 16005 | MyBatis Plus功能测试 - 完整CRUD流程 | 9 | [MyBatisPlusTestController](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-mybatis-starter-example/structure-mybatis-plus-starter-example/src/main/java/cn/structure/example/mybatisplus/controller/MyBatisPlusTestController.java) |
| structure-tk-mapper-starter-example | 16006 | Tk Mapper功能测试 - 完整CRUD流程 | 9 | [TkMapperTestController](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-mybatis-starter-example/structure-tk-mapper-starter-example/src/main/java/cn/structure/example/tk/mapper/controller/TkMapperTestController.java) |
| structure-redis-example | 16007 | Redis操作和分布式锁测试 - 完整数据类型测试 | 8 | [RedisTestController](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-redis-starter-example/src/main/java/cn/structure/starter/redis/example/controller/RedisTestController.java) |
| structure-redisson-starter-example | 16008 | Redisson分布式对象测试 - 完整功能测试 | 10 | [RedissonTestController](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-redisson-starter-example/src/main/java/cn/structure/starter/redission/example/controller/RedissonTestController.java) |
| structure-restful-web-example | 16009 | RESTful API测试 | 5 | [RestfulTestController](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-restful-web-example/src/main/java/cn/structure/example/restful/controller/RestfulTestController.java) |
| structure-tenant-example | 8080 | 多租户功能测试 | 3 | [TenantTestController](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-tenant-example/src/main/java/cn/structure/example/tenant/controller/TenantTestController.java) |
| structure-rpc-example | 16010 | RPC功能测试 - 完整自我调用测试 | 8 | [RpcTestController](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-rpc-example/src/main/java/cn/structure/oauth/controller/RpcTestController.java) |

**总计：53个测试用例**

## 📝 完整测试用例说明

### 1. 数据库操作模块（MyBatis系列）

每个数据库模块都实现了以下**完整的CRUD流程测试**：

#### Tk Mapper 测试接口 ([TkMapperTestController.java](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-mybatis-starter-example/structure-tk-mapper-starter-example/src/main/java/cn/structure/example/tk/mapper/controller/TkMapperTestController.java))

| 方法 | 接口路径 | 功能说明 |
|------|---------|---------|
| GET | `/tk/health` | 健康检查 |
| GET | `/tk/test/getUserById` | 按ID查询用户 |
| GET | `/tk/test/getUserByUsername` | 按用户名查询用户 |
| POST | `/tk/test/insertUser` | 插入用户 |
| PUT | `/tk/test/updateUser` | 更新用户 |
| DELETE | `/tk/test/deleteUser` | 删除用户 |
| GET | `/tk/test/listUserPage` | 分页查询用户 |
| GET | `/tk/test/fullCrud` | **完整CRUD流程测试**（推荐） |
| GET | `/tk/test/all` | **所有功能综合测试**（推荐） |

#### MyBatis 测试接口 ([MyBatisTestController.java](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-mybatis-starter-example/structure-mybatis-example/src/main/java/cn/structure/example/mybatis/controller/MyBatisTestController.java))

| 方法 | 接口路径 | 功能说明 |
|------|---------|---------|
| GET | `/mybatis/health` | 健康检查 |
| GET | `/mybatis/test/getUserById` | 按ID查询用户 |
| GET | `/mybatis/test/getUserByUsername` | 按用户名查询用户 |
| POST | `/mybatis/test/insertUser` | 插入用户 |
| PUT | `/mybatis/test/updateUser` | 更新用户 |
| DELETE | `/mybatis/test/deleteUser` | 删除用户 |
| GET | `/mybatis/test/listUserPage` | 分页查询用户 |
| GET | `/mybatis/test/fullCrud` | **完整CRUD流程测试**（推荐） |
| GET | `/mybatis/test/all` | **所有功能综合测试**（推荐） |

#### MyBatis Plus 测试接口 ([MyBatisPlusTestController.java](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-mybatis-starter-example/structure-mybatis-plus-starter-example/src/main/java/cn/structure/example/mybatisplus/controller/MyBatisPlusTestController.java))

| 方法 | 接口路径 | 功能说明 |
|------|---------|---------|
| GET | `/mybatis-plus/health` | 健康检查 |
| GET | `/mybatis-plus/test/getUserById` | 按ID查询用户（getById） |
| GET | `/mybatis-plus/test/getUserByUsername` | 按用户名查询用户 |
| POST | `/mybatis-plus/test/insertUser` | 插入用户（save） |
| PUT | `/mybatis-plus/test/updateUser` | 更新用户（updateById） |
| DELETE | `/mybatis-plus/test/deleteUser` | 删除用户（removeById） |
| GET | `/mybatis-plus/test/listUserPage` | 分页查询用户 |
| GET | `/mybatis-plus/test/fullCrud` | **完整CRUD流程测试**（推荐） |
| GET | `/mybatis-plus/test/all` | **所有功能综合测试**（推荐） |

### 2. 缓存模块（Redis/Redisson）

#### Redis 测试接口 ([RedisTestController.java](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-redis-starter-example/src/main/java/cn/structure/starter/redis/example/controller/RedisTestController.java))

| 方法 | 接口路径 | 功能说明 |
|------|---------|---------|
| GET | `/redis/health` | 健康检查 |
| GET | `/redis/test/setGet` | SET/GET操作测试 |
| GET | `/redis/test/hash` | Hash操作测试 |
| GET | `/redis/test/list` | List操作测试 |
| GET | `/redis/test/set` | Set操作测试 |
| GET | `/redis/test/expire` | 过期时间测试 |
| GET | `/redis/test/delete` | 删除键测试 |
| GET | `/redis/test/all` | **所有功能综合测试**（推荐） |

#### Redisson 测试接口 ([RedissonTestController.java](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-redisson-starter-example/src/main/java/cn/structure/starter/redission/example/controller/RedissonTestController.java))

| 方法 | 接口路径 | 功能说明 |
|------|---------|---------|
| GET | `/redisson/health` | 健康检查 |
| POST | `/redisson/test/bucket` | String Bucket存储测试 |
| GET | `/redisson/test/bucket/get` | Bucket读取测试 |
| POST | `/redisson/test/map` | Map操作测试 |
| GET | `/redisson/test/map/get` | Map读取测试 |
| POST | `/redisson/test/object` | Object对象存储测试 |
| GET | `/redisson/test/keys` | 键查询测试 |
| DELETE | `/redisson/test/delete` | 删除键测试 |
| GET | `/redisson/test/all` | **所有功能综合测试**（推荐） |
| POST | `/redisson/test/batch` | **批量操作性能测试** |

### 3. RPC 模块

#### RPC 测试接口 ([RpcTestController.java](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-rpc-example/src/main/java/cn/structure/oauth/controller/RpcTestController.java))

| 方法 | 接口路径 | 功能说明 |
|------|---------|---------|
| GET | `/rpc/health` | 健康检查 |
| GET | `/rpc/test` | 自我调用测试 |
| GET | `/rpc/user/getById/{id}` | 获取用户（自我调用） |
| GET | `/rpc/user/getByUsername` | 按用户名获取用户 |
| POST | `/rpc/user/create` | 创建用户（POST请求） |
| PUT | `/rpc/user/update/{id}` | 更新用户（PUT请求） |
| DELETE | `/rpc/user/delete/{id}` | 删除用户（DELETE请求） |
| GET | `/rpc/test/all` | **所有功能综合测试**（推荐） |

**服务端点** ([UserController.java](file:///Users/chuck/projects/structure-projects/structure-boot/structure-example/structure-rpc-example/src/main/java/cn/structure/oauth/server/controller/UserController.java))：
- GET `/api/user/getById/{id}` - 获取用户
- GET `/api/user/getByUsername` - 按用户名获取
- POST `/api/user/create` - 创建用户
- PUT `/api/user/update/{id}` - 更新用户
- DELETE `/api/user/delete/{id}` - 删除用户

## 📝 脚本说明

### test-all.sh（推荐）
统一测试脚本，包含所有模块的测试。

**使用方式：**
```bash
./test-all.sh {all|boot|log|minio|mybatis|mybatis-plus|tk-mapper|redis|redisson|restful-web|tenant|rpc}
```

**示例：**
```bash
./test-all.sh all                # 运行所有测试
./test-all.sh redis              # 测试Redis示例
./test-all.sh mybatis-plus       # 测试MyBatis Plus示例
./test-all.sh redisson           # 测试Redisson示例
```

### build-examples.sh
用于构建示例模块。

**使用方式：**
```bash
./build-examples.sh {all|module-name}
```

### quickstart.sh
快速启动脚本，自动构建并测试所有模块。

```bash
./quickstart.sh
```

## ⚙️ 前置条件

1. **Java 17** - 必须安装 JDK 17
```bash
export JAVA_HOME=/path/to/jdk-17
```

2. **Maven 3.6+** - 用于构建项目
```bash
mvn --version
```

3. **Redis** - Redis/Redisson 测试需要 Redis 服务器运行

4. **Minio** - Minio 测试需要 Minio 服务器运行

5. **MySQL** - MyBatis 相关测试需要 MySQL 数据库

## 📊 查看日志

所有测试日志位于 `logs/` 目录：

```bash
# 查看所有日志文件
ls -la logs/

# 查看特定模块的日志
cat logs/structure-redis-example.log
```

## 🔧 故障排除

### 问题 1: JAR 文件未找到

**错误信息：**
```
[ERROR] JAR file not found for module: xxx
```

**解决方案：**
```bash
./build-examples.sh all
```

### 问题 2: 端口被占用

**错误信息：**
```
[WARNING] xxx is already running on port xxxx
```

**解决方案：**
```bash
# 查找并停止占用端口的进程
lsof -ti:16007 | xargs kill -9
```

### 问题 3: Java 版本不正确

**错误信息：**
```
[ERROR] Java 17 not found
```

**解决方案：**
```bash
# 设置正确的 JAVA_HOME
export JAVA_HOME=/Users/chuck/.sdkman/candidates/java/17.0.12-oracle
export PATH=$JAVA_HOME/bin:$PATH
```

## 🎯 测试设计理念

每个测试控制器都实现了**真实的功能测试**：

### 数据库组件测试
- ✅ 插入数据并验证返回的ID
- ✅ 查询刚插入的数据验证一致性
- ✅ 更新数据并重新查询验证更新成功
- ✅ 删除数据并验证数据不再存在
- ✅ 分页查询验证

### 缓存组件测试
- ✅ SET/GET数据并验证值匹配
- ✅ 测试不同数据类型（String/Hash/List/Map）
- ✅ 测试过期时间
- ✅ 测试批量操作性能

### RPC组件测试
- ✅ 自我调用（RestTemplate）
- ✅ 启动服务端点
- ✅ 通过客户端调用
- ✅ 验证返回数据的正确性
- ✅ 测试所有HTTP方法

## 📚 学习资源

- **TEST_IMPLEMENTATION_SUMMARY.md** - 详细的测试用例实现总结
- **DEMONSTRATION.md** - 详细的演示文档和示例输出
- 各模块的源码 - 查看具体实现

---

**更新时间：** 2025-05-24  
**版本：** 3.0.0
