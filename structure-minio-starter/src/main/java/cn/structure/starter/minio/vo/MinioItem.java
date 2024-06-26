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

import io.minio.messages.Item;
import io.minio.messages.Owner;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Date;

/**
 * <p>
 * minio 桶中的对象信息
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/7/17 14:06
 */
@Data
@AllArgsConstructor
public class MinioItem {

	/**
	 * 对象名
	 */
	private String objectName;
	/**
	 * 上次修改时间
	 */
	private Date lastModified;
	/**
	 * 标签
	 */
	private String etag;
	/**
	 * 存储类别
	 */
	private Long size;
	/**
	 * 存储类
	 */
	private String storageClass;
	/**
	 * 所有者
	 */
	private Owner owner;
	/**
	 * 类型
	 */
	private String type;

	public MinioItem(Item item) {
		this.objectName = item.objectName();
		this.lastModified = item.lastModified();
		this.etag = item.etag();
		this.size = (long) item.size();
		this.storageClass = item.storageClass();
		this.owner = item.owner();
		this.type = item.isDir() ? "directory" : "file";
	}
}
