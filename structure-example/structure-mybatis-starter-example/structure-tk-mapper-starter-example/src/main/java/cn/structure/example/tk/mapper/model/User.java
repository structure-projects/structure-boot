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
package cn.structure.example.tk.mapper.model;

import cn.structure.starter.mybatis.annotation.CreateTime;
import cn.structure.starter.mybatis.annotation.UpdateTime;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;
import java.util.Date;

@Data
public class User {
    /**
     * 主键ID （会员信息表的ID相同）
     */
    @Id
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 加密后的密码
     */
    private String password;

    /**
     * 是否过期 0：过期 1：未过期
     */
    @Column(name = "is_unexpired")
    private Boolean unexpired;

    /**
     * 是否启用 1:  启用 0:未启用
     */
    @Column(name = "is_enabled")
    private Boolean enabled;

    /**
     * 是否锁定 0:  锁定 1:未锁定
     */
    @Column(name = "is_unlocked")
    private Boolean unlocked;

    /**
     * 是否删除 0：未删除 1：删除
     */
    @Column(name = "is_deleted")
    private Boolean deleted;

    /**
     * 创建时间
     */
    @CreateTime
    @Column(name = "create_time")
    private Date createTime;

    @UpdateTime
    @Column(name = "update_time")
    private Date updateTime;
}