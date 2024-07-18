package cn.structure.oauth;

import cn.structure.starter.web.restful.annotation.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * 启动程序
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/3/10 21:35
 */
@EnableSwagger
@SpringBootApplication
public class RpcExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(RpcExampleApplication.class, args);
    }
}
