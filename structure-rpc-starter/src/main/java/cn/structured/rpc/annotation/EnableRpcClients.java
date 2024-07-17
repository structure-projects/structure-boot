package cn.structured.rpc.annotation;

import cn.structured.rpc.configuration.RpcClientsScan;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 后续用于动态代理的实现
 *
 * @author chuck
 * @version 2024/07/17 下午6:00
 * @since 1.8
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RpcClientsScan.class)
public @interface EnableRpcClients {
}
