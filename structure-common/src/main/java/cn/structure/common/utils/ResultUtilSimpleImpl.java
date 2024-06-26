package cn.structure.common.utils;

import cn.structure.common.entity.IResult;
import cn.structure.common.entity.ResResultVO;
import cn.structure.common.entity.VerificationFailedMsg;
import cn.structure.common.enums.ResultCodeEnum;
import cn.structure.common.exception.CommonException;

import java.io.Serializable;
import java.util.List;

/**
 * 简易实现
 *
 * @author cqliut
 * @version 2023.0620
 * @since 1.0.1
 */
public class ResultUtilSimpleImpl implements IResultUtil {

    public static <T> ResResultVO<T> success(T t) {
        return ResResultVO.<T>builder()
                .success(true)
                .code(ResultCodeEnum.SUCCESS.getCode())
                .message(ResultCodeEnum.SUCCESS.getMsg())
                .data(t)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> ResResultVO<T> fail(String code, String message, T data) {
        return ResResultVO.<T>builder()
                .code(code)
                .message(message)
                .success(false)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult fail(String code, String message) {
        return ResResultVO.builder()
                .code(code)
                .message(message)
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult exception(CommonException ce) {
        return ResResultVO.builder()
                .code(ce.getCode())
                .message(ce.getMessage())
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult exception() {
        return ResResultVO.builder()
                .code(ResultCodeEnum.ERR.getCode())
                .message(ResultCodeEnum.ERR.getMsg())
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult exception(String message) {
        return ResResultVO.builder()
                .code(ResultCodeEnum.ERR.getCode())
                .message(message)
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult exception(String msg, String subCode, String subMsg) {
        return ResResultVO.builder()
                .code(subCode)
                .message(subMsg)
                .success(false)
                .data(msg)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult fallback(String subCode, String subMsg) {
        return ResResultVO.builder()
                .code(subCode)
                .message(subMsg)
                .success(false)
                .data(ResultCodeEnum.FALLBACK)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult unauthorized() {
        return ResResultVO.builder()
                .code(ResultCodeEnum.UNAUTHORIZED.getCode())
                .message(ResultCodeEnum.UNAUTHORIZED.getMsg())
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult unauthorized(String subCode, String subMsg) {
        return ResResultVO.builder()
                .code(ResultCodeEnum.UNAUTHORIZED.getCode())
                .message(ResultCodeEnum.UNAUTHORIZED.getMsg())
                .success(false)
                .data(subCode)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult verificationFailed(List<VerificationFailedMsg> verificationFailedMsgList) {
        return ResResultVO.builder()
                .code(ResultCodeEnum.VERIFICATION_FAILED.getCode())
                .message(ResultCodeEnum.VERIFICATION_FAILED.getMsg())
                .success(false)
                .data(verificationFailedMsgList)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult notfound() {
        return ResResultVO.builder()
                .code(ResultCodeEnum.NOT_FOUND.getCode())
                .message(ResultCodeEnum.NOT_FOUND.getMsg())
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult convertFailed() {
        return ResResultVO.builder()
                .code(ResultCodeEnum.CONVERT_FAILED.getCode())
                .message(ResultCodeEnum.CONVERT_FAILED.getMsg())
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
