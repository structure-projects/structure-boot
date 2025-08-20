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
package cn.structure.oauth.service;

import cn.hutool.core.io.IoUtil;
import cn.structure.oauth.client.UserClient;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.charset.StandardCharsets;


/**
 * 如果你的用户服务引用了structure-oauth-resource-starter则需要使用oauth-client角色调用用户中心除非您排除了对open-api的请求拦截
 * 详情可以查看 structure-oauth-sdk
 *
 * @author chuck
 * @version 2024/07/17 下午2:16
 * @since 1.8
 */
@Slf4j
@Service
public class UserManager {

    @Resource
    private UserClient userClient;

    public JSONObject getUserByUsername(String username) {
        HttpGet get = new HttpGet("/open-api/user/getUserByUsername?username=" + username);
        HttpResponse handler = userClient.handler(get);
        try {
            String jsonStr = IoUtil.read(handler.getEntity().getContent(), StandardCharsets.UTF_8);
            return JSON.parseObject(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public JSONObject getUserByPlatformUserIdAndCode(String platformUserId, String platformCode) {
        HttpGet get = new HttpGet("/open-api/user/getUserByUsername?platformUserId=" + platformUserId + "&platformCode=" + platformCode);
        HttpResponse handler = userClient.handler(get);
        try {
            String jsonStr = IoUtil.read(handler.getEntity().getContent(), StandardCharsets.UTF_8);
            return JSON.parseObject(jsonStr);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
