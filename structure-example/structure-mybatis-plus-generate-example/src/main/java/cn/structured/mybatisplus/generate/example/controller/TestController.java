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
package cn.structured.mybatisplus.generate.example.controller;

import cn.structured.mybatisplus.generate.example.dao.OrgPostMapper;
import cn.structured.mybatisplus.generate.example.entity.OrgPost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 测试
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2022/5/13 20:33
 */
@RestController
@RequestMapping(value = "/test")
public class TestController {

    @Autowired
    private OrgPostMapper orgPostMapper;

    @RequestMapping("/save")
    public void insertList() {

        List<OrgPost> orgPostList = new ArrayList<>();

        OrgPost orgPost = new OrgPost();
        orgPost.setId(11);
        orgPost.setPostName("orgPost");
        orgPost.setParentId(0);
        orgPost.setSort(0);
        orgPostList.add(orgPost);

        OrgPost orgPost2 = new OrgPost();
        orgPost2.setId(12);
        orgPost.setPostName("orgPost2");
        orgPost.setParentId(0);
        orgPost.setSort(0);
        orgPostList.add(orgPost2);

        orgPostMapper.insertList(orgPostList);
    }

    @RequestMapping("/rm")
    public void rm() {
        orgPostMapper.deleteById(1);
    }

}
