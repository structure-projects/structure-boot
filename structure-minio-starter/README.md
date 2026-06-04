# Structure MinIO Starter

Structure MinIO Starter 是 Structure Boot 框架的 MinIO 对象存储模块，提供便捷的文件上传、下载、删除等操作。

## 功能特性

- **文件上传**: 支持单文件和多文件上传
- **文件下载**: 支持文件下载和预览
- **文件管理**: 支持文件删除、列表查询
- **HTTP端点**: 提供REST API端点用于文件操作
- **灵活配置**: 支持多种MinIO配置选项

## 快速开始

### 1. 添加依赖

```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-minio-starter</artifactId>
    <version>${version}</version>
</dependency>
```

### 2. 配置MinIO

在 `application.yml` 中添加配置：

```yaml
structure:
  minio:
    enabled: true
    # MinIO服务地址
    endpoint: http://localhost:9000
    # 访问密钥
    access-key: minioadmin
    # 秘密密钥
    secret-key: minioadmin
    # 默认存储桶
    default-bucket: mybucket
    # 是否创建默认存储桶
    create-bucket: true
```

### 3. 使用MinioTemplate

```java
@Autowired
private MinioTemplate minioTemplate;

// 上传文件
minioTemplate.upload("mybucket", "test.txt", inputStream);

// 下载文件
InputStream stream = minioTemplate.download("mybucket", "test.txt");

// 删除文件
minioTemplate.delete("mybucket", "test.txt");

// 获取文件列表
List<MinioItem> items = minioTemplate.list("mybucket");
```

## 目录结构

```
structure-minio-starter/
├── src/main/java/cn/structure/starter/minio/
│   ├── configuration/      # 配置类
│   │   └── MinioAutoConfiguration.java
│   ├── http/               # HTTP端点
│   │   └── MinioEndpoint.java
│   ├── properties/         # 配置属性
│   │   └── MinioProperties.java
│   ├── service/            # 服务类
│   │   └── MinioTemplate.java
│   └── vo/                 # 视图对象
│       ├── MinioItem.java
│       └── MinioObject.java
```

## HTTP端点

| 端点 | 方法 | 说明 |
| :--- | :--- | :--- |
| `/api/minio/upload` | POST | 上传文件 |
| `/api/minio/download/{bucket}/{path}` | GET | 下载文件 |
| `/api/minio/delete/{bucket}/{path}` | DELETE | 删除文件 |
| `/api/minio/list/{bucket}` | GET | 列出文件 |
| `/api/minio/preview/{bucket}/{path}` | GET | 预览文件 |

## 配置项说明

| 配置项 | 类型 | 说明 | 默认值 |
| :--- | :--- | :--- | :--- |
| `structure.minio.enabled` | Boolean | 是否启用MinIO | `true` |
| `structure.minio.endpoint` | String | MinIO服务地址 | - |
| `structure.minio.access-key` | String | 访问密钥 | - |
| `structure.minio.secret-key` | String | 秘密密钥 | - |
| `structure.minio.default-bucket` | String | 默认存储桶 | - |
| `structure.minio.create-bucket` | Boolean | 是否创建默认存储桶 | `false` |

## MinioTemplate API

| 方法 | 说明 |
| :--- | :--- |
| `upload(String bucket, String path, InputStream stream)` | 上传文件 |
| `upload(String bucket, String path, InputStream stream, String contentType)` | 上传文件（指定类型） |
| `download(String bucket, String path)` | 下载文件 |
| `delete(String bucket, String path)` | 删除文件 |
| `list(String bucket)` | 列出文件 |
| `list(String bucket, String prefix)` | 列出指定前缀的文件 |
| `exists(String bucket, String path)` | 检查文件是否存在 |
| `getObjectInfo(String bucket, String path)` | 获取文件信息 |
| `copy(String sourceBucket, String sourcePath, String targetBucket, String targetPath)` | 复制文件 |
| `move(String sourceBucket, String sourcePath, String targetBucket, String targetPath)` | 移动文件 |

## 使用示例

### 文件上传

```java
@RestController
public class FileController {
    
    @Autowired
    private MinioTemplate minioTemplate;
    
    @PostMapping("/upload")
    public Result upload(@RequestParam("file") MultipartFile file) throws IOException {
        String path = "uploads/" + file.getOriginalFilename();
        minioTemplate.upload("mybucket", path, file.getInputStream());
        return Result.success("上传成功");
    }
}
```

### 文件下载

```java
@GetMapping("/download")
public ResponseEntity<Resource> download(@RequestParam String path) {
    InputStream stream = minioTemplate.download("mybucket", path);
    Resource resource = new InputStreamResource(stream);
    return ResponseEntity.ok()
            .contentType(MediaType.APPLICATION_OCTET_STREAM)
            .body(resource);
}
```

## 许可证

Apache License 2.0