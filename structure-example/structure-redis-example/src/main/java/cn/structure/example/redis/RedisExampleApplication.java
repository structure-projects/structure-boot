package cn.structure.example.redis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 *     redis例子 - 启动入口
 * </p>
 * @author chuck
 * @date 2020-12-18
 * @version 1.0.1
 */
@SpringBootApplication
public class RedisExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(RedisExampleApplication.class,args);
    }

}
