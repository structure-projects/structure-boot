/*
 * Copyright (c) 2025 Structure Boot
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
//package cn.structure.example.minio.controller;
//
//import cn.structure.starter.minio.service.MinioTemplate;
//import cn.structure.starter.minio.vo.MinioItem;
//import cn.structure.starter.minio.vo.MinioObject;
//import io.minio.messages.Bucket;
//import lombok.AllArgsConstructor;
//import lombok.SneakyThrows;
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * <p>
// * Minio控制器 自定义实现可忽略 endpoint-enable
// * </p>
// *
// * @author chuck
// * @version 1.0.1
// * @since 2021/7/17 15:38
// */
//@RestController
//@RequestMapping("/my/minio")
//@AllArgsConstructor
//public class MinioController {
//
//    private final MinioTemplate template;
//
//    /**
//     * Bucket Endpoints
//     */
//    @SneakyThrows
//    @PostMapping("/bucket/{bucketName}")
//    public Bucket createBucker(@PathVariable String bucketName) {
//
//        template.createBucket(bucketName);
//        return template.getBucket(bucketName).get();
//
//    }
//
//    @SneakyThrows
//    @GetMapping("/bucket")
//    public List<Bucket> getBuckets() {
//        return template.getAllBuckets();
//    }
//
//    @SneakyThrows
//    @GetMapping("/bucket/{bucketName}")
//    public Bucket getBucket(@PathVariable String bucketName) {
//        return template.getBucket(bucketName).orElseThrow(() -> new IllegalArgumentException("Bucket Name not found!"));
//    }
//
//    @SneakyThrows
//    @DeleteMapping("/bucket/{bucketName}")
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    public void deleteBucket(@PathVariable String bucketName) {
//        template.removeBucket(bucketName);
//    }
//
//    /**
//     * Object Endpoints
//     */
//    @SneakyThrows
//    @PostMapping("/object/{bucketName}")
//    public MinioObject createObject(MultipartFile object, @PathVariable String bucketName) {
//        String name = object.getOriginalFilename();
//        template.putObject(bucketName, name, object.getInputStream(), object.getSize(), object.getContentType());
//        return new MinioObject(template.getObjectInfo(bucketName, name));
//
//    }
//
//    @SneakyThrows
//    @PostMapping("/object/{bucketName}/{objectName}")
//    public MinioObject createObject(@RequestBody MultipartFile object, @PathVariable String bucketName, @PathVariable String objectName) {
//        template.putObject(bucketName, objectName, object.getInputStream(), object.getSize(), object.getContentType());
//        return new MinioObject(template.getObjectInfo(bucketName, objectName));
//
//    }
//
//    @SneakyThrows
//    @GetMapping("/object/{bucketName}/{objectName}")
//    public List<MinioItem> filterObject(@PathVariable String bucketName, @PathVariable String objectName) {
//
//        return template.getAllObjectsByPrefix(bucketName, objectName, true);
//
//    }
//
//    @SneakyThrows
//    @GetMapping("/object/{bucketName}/{objectName}/{expires}")
//    public Map<String, Object> getObject(@PathVariable String bucketName, @PathVariable String objectName, @PathVariable Integer expires) {
//        Map<String, Object> responseBody = new HashMap<>(8);
//        // Put Object info
//        responseBody.put("bucket", bucketName);
//        responseBody.put("object", objectName);
//        responseBody.put("url", template.getObjectURL(bucketName, objectName, expires));
//        responseBody.put("expires", expires);
//        return responseBody;
//    }
//
//    @SneakyThrows
//    @ResponseStatus(HttpStatus.ACCEPTED)
//    @DeleteMapping("/object/{bucketName}/{objectName}/")
//    public void deleteObject(@PathVariable String bucketName, @PathVariable String objectName) {
//
//
//        template.removeObject(bucketName, objectName);
//    }
//
//}
