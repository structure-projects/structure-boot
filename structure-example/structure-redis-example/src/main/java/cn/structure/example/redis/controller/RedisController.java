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
package cn.structure.example.redis.controller;

import cn.structure.example.redis.service.RedisLockService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author chuck
 * @version 1.0.1
 * @date 2020-12-18
 */
@Controller
@RequestMapping(value = "/redis")
public class RedisController {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Autowired
    private RedisLockService redisLockService;

    @GetMapping(value = "/getKey")
    public void getKey(@RequestParam(value = "key") String key, HttpServletResponse response) throws IOException {
        //你对redis的操作
        redisTemplate.opsForValue().set(key, key);
        String value = redisTemplate.opsForValue().get(key);
        value = (null == value) ? "this value is null" : value;
        System.out.println("value = " + value);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(value.getBytes());
        outputStream.close();
    }

    @GetMapping(value = "/redisLock")
    public void redisLock(@RequestParam(value = "key") String key, HttpServletResponse response) throws IOException {
        redisLockService.redisLock(key);
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(("this key is " + key).getBytes());
        outputStream.close();
    }
}
