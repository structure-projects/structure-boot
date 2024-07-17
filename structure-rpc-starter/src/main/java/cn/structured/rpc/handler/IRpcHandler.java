package cn.structured.rpc.handler;

import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;


/**
 * 远程调用处理接口
 *
 * @author chuck
 * @version 2024/07/17 下午4:03
 * @since 1.8
 */
public interface IRpcHandler {

    /**
     * 前置处理方法 通常用户处理请求前的token
     */
    default void preposition(HttpRequest httpRequest) {
    }

    /**
     * 处理器
     *
     * @param httpRequest 入参
     * @return HttpResponse
     */
    HttpResponse handler(HttpRequest httpRequest);

    /**
     * 后置处理方法 通常用户验证token
     */
    default void postposition(HttpResponse httpResponse) {
    }
}
