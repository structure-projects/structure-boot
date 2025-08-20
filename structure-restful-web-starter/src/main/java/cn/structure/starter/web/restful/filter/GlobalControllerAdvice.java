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
package cn.structure.starter.web.restful.filter;

import cn.structure.common.entity.IResult;
import cn.structure.common.enums.ErrorCodeEnum;
import cn.structure.common.exception.CommonException;
import cn.structure.common.utils.IResultUtil;
import cn.structure.starter.web.restful.exception.SystemException;
import cn.structure.starter.web.restful.exception.ThirdPartyException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;


/**
 * <p>
 * ResultVO 全局异常处理
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2020-01-03
 */
@Slf4j
@ControllerAdvice
public class GlobalControllerAdvice {

    @Resource
    private IResultUtil iResultUtil;

    /**
     * 全局异常捕捉处理
     */
    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public IResult exceptionHandler(Exception ex) {
        log.error("全局未知异常捕获======》", ex);
        return iResultUtil.exception(ErrorCodeEnum.SYSTEM_ERROR.getErrorType(), "500", ex.getMessage());
    }

    /**
     * 全局异常捕捉处理
     */
    @ResponseBody
    @ExceptionHandler(value = Throwable.class)
    public IResult errorHandler(Throwable ex) {
        log.error("全局未知异常捕获======》", ex);
        return iResultUtil.exception(ErrorCodeEnum.SYSTEM_ERROR.getErrorType(), "500", ex.getMessage());
    }

    /**
     * 业务自定义异常
     */
    @ResponseBody
    @ExceptionHandler(value = CommonException.class)
    public IResult structureErrorHandler(CommonException ex) {
        log.error("全局业务异常捕捉======》", ex);
        return iResultUtil.fail(ex.getCode(), ex.getMsg());
    }

    /**
     * 第三方异常
     */
    @ResponseBody
    @ExceptionHandler(value = ThirdPartyException.class)
    public IResult thirdPartyErrorHandler(ThirdPartyException ex) {
        log.error("全局第三方异常捕捉======》", ex);
        return iResultUtil.exception(ErrorCodeEnum.THIRD_PARTY_ERROR.getErrorType(), ex.getCode(), ex.getMsg());
    }

    /**
     * 系统异常
     */
    @ResponseBody
    @ExceptionHandler(value = SystemException.class)
    public IResult systemErrorHandler(SystemException ex) {
        log.error("全局系统异常捕捉======》", ex);
        return iResultUtil.exception(ErrorCodeEnum.SYSTEM_ERROR.getErrorType(), ex.getCode(), ex.getMsg());
    }
}
