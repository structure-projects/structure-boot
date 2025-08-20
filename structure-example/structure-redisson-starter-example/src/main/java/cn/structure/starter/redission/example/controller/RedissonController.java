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
package cn.structure.starter.redission.example.controller;

import org.redisson.api.RBucket;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * <p>
 * redisson使用的控制器
 * </p>
 *
 * @author chuck
 * @version V1.0.0
 * @since 2020/12/26 11:26
 */
@RestController
public class RedissonController {

    @Resource
    private RedissonClient redissonClient;

    @RequestMapping("/setKey")
    public void setKey() {
        RBucket<String> test = redissonClient.getBucket("test");
        test.set("test redissonClient");
        System.out.println("test = " + test.get());
    }

    @RequestMapping("/getKey")
    public void getKey() {
        RBucket<String> test = redissonClient.getBucket("test");
        System.out.println("test = " + test.get());
    }
}
