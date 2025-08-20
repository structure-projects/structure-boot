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
import cn.structure.common.entity.VerificationFailedMsg;
import cn.structure.common.enums.ResultCodeEnum;
import cn.structure.common.utils.IResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 参数校验异常
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021-01-03
 */
@Slf4j
@RestControllerAdvice
public class GlobalBadRequestExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalBadRequestExceptionHandler.class);

    @Resource
    private IResultUtil iResultUtil;

    /**
     * 校验错误拦截处理
     *
     * @param exception 错误信息集合
     * @return 错误信息
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public IResult validationBodyException(MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        List<VerificationFailedMsg> verificationFailedMsgArrayList = new ArrayList<>();
        if (result.hasErrors()) {
            List<ObjectError> errors = result.getAllErrors();
            errors.forEach(p -> {
                FieldError fieldError = (FieldError) p;
                logger.error(String.format("Data check failure : object{%s},field{%s},errorMessage{%s}", fieldError.getObjectName(), fieldError.getField(), fieldError.getDefaultMessage()));
                VerificationFailedMsg vfs = new VerificationFailedMsg();
                vfs.setField(fieldError.getField());
                vfs.setErrorMessage(fieldError.getDefaultMessage());
                verificationFailedMsgArrayList.add(vfs);
            });
        }
        return iResultUtil.verificationFailed(verificationFailedMsgArrayList);
    }

    /**
     * 参数类型转换错误
     *
     * @param exception 错误
     * @return 错误信息
     */
    @ExceptionHandler(HttpMessageConversionException.class)
    public IResult parameterTypeException(HttpMessageConversionException exception) {
        logger.error(exception.getCause().getLocalizedMessage());
        return iResultUtil.fail(ResultCodeEnum.CONVERT_FAILED.getCode(), ResultCodeEnum.CONVERT_FAILED.getMsg());
    }

}
