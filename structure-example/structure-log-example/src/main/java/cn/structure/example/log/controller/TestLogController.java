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
package cn.structure.example.log.controller;

import cn.structure.example.log.pojo.vo.ReqTestVO;
import cn.structure.example.log.pojo.vo.ResTestVO;
import cn.structure.starter.log.anno.AspectParamLog;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * controller日志测试
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/1/3 14:14
 */
@RequestMapping("/test")
@RestController
public class TestLogController {

    /**
     * 使用 AspectParamLog 对方法的日志进行记录
     *
     * @param reqTestVO
     * @return
     */
    @AspectParamLog()
    @RequestMapping(value = "test")
    public ResTestVO testLog(@RequestBody ReqTestVO reqTestVO) {
        ResTestVO resTestVO = new ResTestVO();
        resTestVO.setId(reqTestVO.getId());
        resTestVO.setName(reqTestVO.getName());
        return resTestVO;
    }

}
