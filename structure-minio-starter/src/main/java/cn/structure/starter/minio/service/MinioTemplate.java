/*
 *    Copyright (c) 2018-2025, freelance All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the freelance.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: chuck
 */

package cn.structure.starter.minio.service;

import cn.structure.starter.minio.vo.MinioItem;
import io.minio.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * <p>
 * minio 交互类
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/17 14:06
 */
@Slf4j
@AllArgsConstructor
public class MinioTemplate {

    private final MinioClient minioClient;

    /**
     * 创建bucket
     *
     * @param bucketName bucket名称
     */
    public void createBucket(String bucketName) {
        log.info("[MinioTemplate] 创建Bucket - bucketName: {}", bucketName);
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (found) {
                log.info("[MinioTemplate] Bucket已存在，跳过创建 - bucketName: {}", bucketName);
                return;
            }
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            log.info("[MinioTemplate] Bucket创建成功 - bucketName: {}", bucketName);
        } catch (Exception e) {
            log.error("[MinioTemplate] 创建Bucket失败 - bucketName: {}", bucketName, e);
            throw new RuntimeException("创建Bucket失败", e);
        }
    }

    /**
     * 获取全部bucket
     * <p>
     * https://docs.minio.io/cn/java-client-api-reference.html#listBuckets
     * </p>
     */
    public List<Bucket> getAllBuckets() {
        log.debug("[MinioTemplate] 获取所有Bucket列表");
        try {
            List<Bucket> buckets = minioClient.listBuckets();
            log.debug("[MinioTemplate] 获取所有Bucket列表成功 - count: {}", buckets.size());
            return buckets;
        } catch (Exception e) {
            log.error("[MinioTemplate] 获取所有Bucket列表失败", e);
            throw new RuntimeException("获取所有Bucket列表失败", e);
        }
    }

    /**
     * @param bucketName bucket名称
     */
    public Optional<Bucket> getBucket(String bucketName) {
        log.debug("[MinioTemplate] 获取Bucket - bucketName: {}", bucketName);
        try {
            Optional<Bucket> bucket = minioClient.listBuckets().stream().filter(b -> b.name().equals(bucketName)).findFirst();
            log.debug("[MinioTemplate] 获取Bucket结果 - bucketName: {}, exists: {}", bucketName, bucket.isPresent());
            return bucket;
        } catch (Exception e) {
            log.error("[MinioTemplate] 获取Bucket失败 - bucketName: {}", bucketName, e);
            throw new RuntimeException("获取Bucket失败", e);
        }
    }

    /**
     * @param bucketName bucket名称
     */
    public void removeBucket(String bucketName) {
        log.info("[MinioTemplate] 删除Bucket - bucketName: {}", bucketName);
        try {
            minioClient.removeBucket(RemoveBucketArgs.builder().bucket(bucketName).build());
            log.info("[MinioTemplate] Bucket删除成功 - bucketName: {}", bucketName);
        } catch (Exception e) {
            log.error("[MinioTemplate] 删除Bucket失败 - bucketName: {}", bucketName, e);
            throw new RuntimeException("删除Bucket失败", e);
        }
    }

    /**
     * 根据文件前置查询文件
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return MinioItem 列表
     */
    public List<MinioItem> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) {
        log.debug("[MinioTemplate] 查询文件列表 - bucketName: {}, prefix: {}, recursive: {}", bucketName, prefix, recursive);
        try {
            List<MinioItem> objectList = new ArrayList<>();
            Iterable<Result<Item>> objectsIterator = minioClient.listObjects(
                ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).recursive(recursive).build()
            );
            for (Result<Item> itemResult : objectsIterator) {
                Item item = itemResult.get();
                objectList.add(new MinioItem(item));
            }
            log.debug("[MinioTemplate] 查询文件列表成功 - bucketName: {}, prefix: {}, count: {}", bucketName, prefix, objectList.size());
            return objectList;
        } catch (Exception e) {
            log.error("[MinioTemplate] 查询文件列表失败 - bucketName: {}, prefix: {}", bucketName, prefix, e);
            throw new RuntimeException("查询文件列表失败", e);
        }
    }

    /**
     * 获取文件外链
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param expires    失效时间（以秒为单位），默认是7天，不得大于七天。
     * @return url
     */
    public String getObjectURL(String bucketName, String objectName, Integer expires) {
        log.debug("[MinioTemplate] 获取文件外链 - bucketName: {}, objectName: {}, expires: {}s", bucketName, objectName, expires);
        try {
            String url = minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .expiry(expires)
                    .build()
            );
            log.debug("[MinioTemplate] 获取文件外链成功 - bucketName: {}, objectName: {}, url: {}", bucketName, objectName, url);
            return url;
        } catch (Exception e) {
            log.error("[MinioTemplate] 获取文件外链失败 - bucketName: {}, objectName: {}", bucketName, objectName, e);
            throw new RuntimeException("获取文件外链失败", e);
        }
    }

    /**
     * 获取文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @return 二进制流
     */
    public InputStream getObject(String bucketName, String objectName) {
        log.debug("[MinioTemplate] 获取文件 - bucketName: {}, objectName: {}", bucketName, objectName);
        try {
            InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build()
            );
            log.debug("[MinioTemplate] 获取文件成功 - bucketName: {}, objectName: {}", bucketName, objectName);
            return stream;
        } catch (Exception e) {
            log.error("[MinioTemplate] 获取文件失败 - bucketName: {}, objectName: {}", bucketName, objectName, e);
            throw new RuntimeException("获取文件失败", e);
        }
    }

    /**
     * 上传文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @param stream     文件流
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#putObject
     */
    public void putObject(String bucketName, String objectName, InputStream stream) throws Exception {
        log.info("[MinioTemplate] 上传文件 - bucketName: {}, objectName: {}", bucketName, objectName);
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .object(objectName)
                    .bucket(bucketName)
                    .stream(stream, stream.available(), -1)
                    .build()
            );
            log.info("[MinioTemplate] 上传文件成功 - bucketName: {}, objectName: {}", bucketName, objectName);
        } catch (Exception e) {
            log.error("[MinioTemplate] 上传文件失败 - bucketName: {}, objectName: {}", bucketName, objectName, e);
            throw e;
        }
    }

    /**
     * 上传文件
     *
     * @param bucketName  bucket名称
     * @param objectName 文件名称
     * @param stream      文件流
     * @param size        大小
     * @param contextType 类型
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#putObject
     */
    public void putObject(String bucketName, String objectName, InputStream stream, long size, String contextType) throws Exception {
        log.info("[MinioTemplate] 上传文件 - bucketName: {}, objectName: {}, size: {}, contentType: {}", 
            bucketName, objectName, size, contextType);
        try {
            minioClient.putObject(
                PutObjectArgs.builder()
                    .object(objectName)
                    .bucket(bucketName)
                    .stream(stream, stream.available(), -1)
                    .build()
            );
            log.info("[MinioTemplate] 上传文件成功 - bucketName: {}, objectName: {}, size: {}, contentType: {}", 
                bucketName, objectName, size, contextType);
        } catch (Exception e) {
            log.error("[MinioTemplate] 上传文件失败 - bucketName: {}, objectName: {}", bucketName, objectName, e);
            throw e;
        }
    }

    /**
     * 获取文件信息
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#statObject
     */
    public StatObjectResponse getObjectInfo(String bucketName, String objectName) throws Exception {
        log.debug("[MinioTemplate] 获取文件信息 - bucketName: {}, objectName: {}", bucketName, objectName);
        try {
            StatObjectResponse stat = minioClient.statObject(
                StatObjectArgs.builder().bucket(bucketName).object(objectName).build()
            );
            log.debug("[MinioTemplate] 获取文件信息成功 - bucketName: {}, objectName: {}, size: {}, lastModified: {}", 
                bucketName, objectName, stat.size(), stat.lastModified());
            return stat;
        } catch (Exception e) {
            log.error("[MinioTemplate] 获取文件信息失败 - bucketName: {}, objectName: {}", bucketName, objectName, e);
            throw e;
        }
    }

    /**
     * 删除文件
     *
     * @param bucketName bucket名称
     * @param objectName 文件名称
     * @throws Exception https://docs.minio.io/cn/java-client-api-reference.html#removeObject
     * @since createTime 2021/7/17 14:06
     */
    public void removeObject(String bucketName, String objectName) throws Exception {
        log.info("[MinioTemplate] 删除文件 - bucketName: {}, objectName: {}", bucketName, objectName);
        try {
            minioClient.removeObject(
                RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build()
            );
            log.info("[MinioTemplate] 删除文件成功 - bucketName: {}, objectName: {}", bucketName, objectName);
        } catch (Exception e) {
            log.error("[MinioTemplate] 删除文件失败 - bucketName: {}, objectName: {}", bucketName, objectName, e);
            throw e;
        }
    }
}
