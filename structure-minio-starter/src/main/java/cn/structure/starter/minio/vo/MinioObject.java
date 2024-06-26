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
 * Author:
 */

package cn.structure.starter.minio.vo;

import io.minio.ObjectStat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * 存储对象的元数据
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/17 14:06
 */

@Data
@AllArgsConstructor
public class MinioObject {
	/**
	 * 存储桶名
	 */
	private String bucketName;
	/**
	 * 名称
	 */
	private String name;
	/**
	 * 创建时间
	 */
	private Date createdTime;
	/**
	 * 长度
	 */
	private Long length;
	/**
	 * 标签
	 */
	private String etag;
	/**
	 * 内容类型
	 */
	private String contentType;
	/**
	 * 请求头信息
	 */
	private Map<String, List<String>> httpHeaders;

	public MinioObject(ObjectStat os) {
		this.bucketName = os.bucketName();
		this.name = os.name();
		this.createdTime = os.createdTime();
		this.length = os.length();
		this.etag = os.etag();
		this.contentType = os.contentType();
		this.httpHeaders = os.httpHeaders();
	}
}
