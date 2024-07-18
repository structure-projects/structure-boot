package cn.structured.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @author chuck
 * @version 2024/07/17 下午3:55
 * @since 1.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Component
public @interface RpcClient {


    /**
     * 默认为服务名
     */
    String value() default "";

    /**
     * 远程调用的地址
     *
     * @return host
     */
    String host() default "";

    /**
     * 端口
     *
     * @return port
     */
    int port() default 80;

}
