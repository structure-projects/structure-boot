package cn.structure.common.utils;

import cn.structure.common.entity.IResult;
import cn.structure.common.entity.VerificationFailedMsg;
import cn.structure.common.exception.CommonException;

import java.util.List;

/**
 * Result工具类
 *
 * @author cqliut
 * @version 2023.0620
 * @since 1.0.1
 */
public interface IResultUtil {

    /**
     * 请求失败的接口
     *
     * @param code    失败后的错误码
     * @param message 错误消息
     * @return IResult
     */
    IResult fail(String code, String message);

    /**
     * 构建一个已知的业务异常
     *
     * @param ce 公共异常类
     * @return IResult
     */
    IResult exception(CommonException ce);

    /**
     * 构建一个未知的异常
     *
     * @return IResult
     */
    IResult exception();

    /**
     * 构建一个未知异常输出消息内容
     *
     * @param message 消息内容
     * @return IResult
     */
    IResult exception(String message);

    /**
     * 构建一个已知异常带有二级错误码和错误描述
     *
     * @param msg     一级描述
     * @param subCode 二级code
     * @param subMsg  二级描述
     * @return IResult
     */
    IResult exception(String msg, String subCode, String subMsg);

    /**
     * 触发服务降级的构建
     *
     * @param subCode 错误码
     * @param subMsg  消息
     * @return IResult
     */
    IResult fallback(String subCode, String subMsg);

    /**
     * 无权限的构建
     *
     * @return IResult
     */
    IResult unauthorized();

    /**
     * 无权限输出二级错误码和消息
     *
     * @param subCode 二级code
     * @param subMsg  二级消息
     * @return IResult
     */
    IResult unauthorized(String subCode, String subMsg);

    /**
     * 输出参数校验失败的消息体
     *
     * @param verificationFailedMsgList 参数校验失败的结果
     * @return IResult
     */
    IResult verificationFailed(List<VerificationFailedMsg> verificationFailedMsgList);

    /**
     * 输出无资源的消息
     *
     * @return IResult
     */
    IResult notfound();

    /**
     * 构建一个转换失败的异常 不符合的数据类型导致转换失败通常在统一异常或者网关上配置拦击并统一输出
     *
     * @return IResult
     */
    IResult convertFailed();

}
