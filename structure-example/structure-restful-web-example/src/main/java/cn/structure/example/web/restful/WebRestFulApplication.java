package cn.structure.example.web.restful;

import cn.structure.starter.web.restful.annotation.EnableFastJsonHttpConverters;
import cn.structure.starter.web.restful.annotation.EnableSwagger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * webRestful 启动器
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/1/3 21:25
 */
@EnableSwagger
@EnableFastJsonHttpConverters
@SpringBootApplication
public class WebRestFulApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebRestFulApplication.class, args);
    }
}
