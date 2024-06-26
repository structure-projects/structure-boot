# structure-minio
对minio的封装 文件存储

## 功能介绍 
对minio的封装 文件存储
## 如何使用
### pom引用 ###
引用最新文档版本的依赖
```xml
<dependency>
    <groupId>cn.structured</groupId>
    <artifactId>structure-minio-starter</artifactId>
    <version>${last.version}</version>
</dependency>
```
### 如何配置 ###
```yaml
structure:
  minio:
    url: http://${yourServerAddress}:${yourPort}
    access-key: ${yourAccessKey}
    secret-key: ${yourSecretKey}
    endpoint-enable: true # 是否开启endpoint
```


#### MinioEndpoint ###

endpoint-enable: true 为开启状态，开启后会享有对minio的基本操作开启后则不需要在实现以下代码如需自定义操作请忽略该功能
```java
@RestController
@AllArgsConstructor
@RequestMapping("/minio")
public class MinioEndpoint {


	private final MinioTemplate template;

	/**
	 * Bucket Endpoints
	 */
	@SneakyThrows
	@PostMapping("/bucket/{bucketName}")
	public Bucket createBucker(@PathVariable String bucketName) {

		template.createBucket(bucketName);
		return template.getBucket(bucketName).get();

	}

	@SneakyThrows
	@GetMapping("/bucket")
	public List<Bucket> getBuckets() {
		return template.getAllBuckets();
	}

	@SneakyThrows
	@GetMapping("/bucket/{bucketName}")
	public Bucket getBucket(@PathVariable String bucketName) {
		return template.getBucket(bucketName).orElseThrow(() -> new IllegalArgumentException("Bucket Name not found!"));
	}

	@SneakyThrows
	@DeleteMapping("/bucket/{bucketName}")
	@ResponseStatus(HttpStatus.ACCEPTED)
	public void deleteBucket(@PathVariable String bucketName) {
		template.removeBucket(bucketName);
	}

	/**
	 * Object Endpoints
	 */
	@SneakyThrows
	@PostMapping("/object/{bucketName}")
	public MinioObject createObject(@RequestBody MultipartFile object, @PathVariable String bucketName) {
		String name = object.getOriginalFilename();
		template.putObject(bucketName, name, object.getInputStream(), object.getSize(), object.getContentType());
		return new MinioObject(template.getObjectInfo(bucketName, name));

	}

	@SneakyThrows
	@PostMapping("/object/{bucketName}/{objectName}")
	public MinioObject createObject(@RequestBody MultipartFile object, @PathVariable String bucketName, @PathVariable String objectName) {
		template.putObject(bucketName, objectName, object.getInputStream(), object.getSize(), object.getContentType());
		return new MinioObject(template.getObjectInfo(bucketName, objectName));

	}

	@SneakyThrows
	@GetMapping("/object/{bucketName}/{objectName}")
	public List<MinioItem> filterObject(@PathVariable String bucketName, @PathVariable String objectName) {

		return template.getAllObjectsByPrefix(bucketName, objectName, true);

	}

	@SneakyThrows
	@GetMapping("/object/{bucketName}/{objectName}/{expires}")
	public Map<String, Object> getObject(@PathVariable String bucketName, @PathVariable String objectName, @PathVariable Integer expires) {
		Map<String, Object> responseBody = new HashMap<>(8);
		// Put Object info
		responseBody.put("bucket", bucketName);
		responseBody.put("object", objectName);
		responseBody.put("url", template.getObjectURL(bucketName, objectName, expires));
		responseBody.put("expires", expires);
		return responseBody;
	}

	@SneakyThrows
	@ResponseStatus(HttpStatus.ACCEPTED)
	@DeleteMapping("/object/{bucketName}/{objectName}/")
	public void deleteObject(@PathVariable String bucketName, @PathVariable String objectName) {

		template.removeObject(bucketName, objectName);
	}
}
```