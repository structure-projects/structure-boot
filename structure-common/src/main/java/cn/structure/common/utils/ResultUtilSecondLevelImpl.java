package cn.structure.common.utils;

import cn.structure.common.entity.IResult;
import cn.structure.common.entity.ResultVO;
import cn.structure.common.entity.VerificationFailedMsg;
import cn.structure.common.enums.ResultCodeEnum;
import cn.structure.common.exception.CommonException;

import java.util.List;

/**
 * 二级CODE实现
 *
 * @author cqliut
 * @version 2023.0620
 * @since 1.0.1
 */
public class ResultUtilSecondLevelImpl implements IResultUtil {

    public static <T> ResultVO<T> success(T t) {
        return ResultVO.<T>builder()
                .code(ResultCodeEnum.SUCCESS.getCode())
                .msg(ResultCodeEnum.SUCCESS.getMsg())
                .subCode(ResultCodeEnum.SUCCESS.getCode())
                .subMsg(ResultCodeEnum.SUCCESS.getMsg())
                .success(true)
                .data(t)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    public static <T> ResultVO<T> fail(String code, String message, T data) {
        return ResultVO.<T>builder()
                .code(ResultCodeEnum.FAIL.getCode())
                .msg(ResultCodeEnum.FAIL.getMsg())
                .subCode(code)
                .subMsg(message)
                .success(true)
                .data(data)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult fail(String code, String message) {
        return ResultVO.builder()
                .code(ResultCodeEnum.FAIL.getCode())
                .msg(ResultCodeEnum.FAIL.getMsg())
                .subCode(code)
                .subMsg(message)
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult exception(CommonException ce) {
        return ResultVO.builder()
                .code(ResultCodeEnum.ERR.getCode())
                .msg(ResultCodeEnum.ERR.getMsg())
                .subCode(ce.getCode())
                .subMsg(ce.getMsg())
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult exception() {
        return ResultVO.builder()
                .code(ResultCodeEnum.ERR.getCode())
                .msg(ResultCodeEnum.ERR.getMsg())
                .subCode(ResultCodeEnum.ERR.getCode())
                .subMsg(ResultCodeEnum.ERR.getMsg())
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult exception(String message) {
        return ResultVO.builder()
                .code(ResultCodeEnum.ERR.getCode())
                .msg(ResultCodeEnum.ERR.getMsg())
                .subCode(ResultCodeEnum.ERR.getCode())
                .subMsg(message)
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult exception(String msg, String subCode, String subMsg) {
        return ResultVO.builder()
                .code(ResultCodeEnum.ERR.getCode())
                .msg(msg)
                .subCode(subCode)
                .subMsg(subMsg)
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult fallback(String subCode, String subMsg) {
        return ResultVO.builder()
                .code(ResultCodeEnum.FALLBACK.getCode())
                .msg(ResultCodeEnum.FALLBACK.getCode())
                .subCode(subCode)
                .subMsg(subMsg)
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult unauthorized() {
        return ResultVO.builder()
                .code(ResultCodeEnum.UNAUTHORIZED.getCode())
                .msg(ResultCodeEnum.UNAUTHORIZED.getCode())
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult unauthorized(String subCode, String subMsg) {
        return ResultVO.builder()
                .code(ResultCodeEnum.UNAUTHORIZED.getCode())
                .msg(ResultCodeEnum.UNAUTHORIZED.getCode())
                .subCode(subCode)
                .subMsg(subMsg)
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult verificationFailed(List<VerificationFailedMsg> verificationFailedMsgList) {
        return ResultVO.builder()
                .code(ResultCodeEnum.VERIFICATION_FAILED.getCode())
                .msg(ResultCodeEnum.VERIFICATION_FAILED.getCode())
                .success(false)
                .data(verificationFailedMsgList)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult notfound() {
        return ResultVO.builder()
                .code(ResultCodeEnum.NOT_FOUND.getCode())
                .msg(ResultCodeEnum.NOT_FOUND.getCode())
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }

    @Override
    public IResult convertFailed() {
        return ResultVO.builder()
                .code(ResultCodeEnum.CONVERT_FAILED.getCode())
                .msg(ResultCodeEnum.CONVERT_FAILED.getCode())
                .success(false)
                .timestamp(System.currentTimeMillis())
                .build();
    }
}
