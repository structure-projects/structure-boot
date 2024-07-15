package cn.structure.example.log;

import cn.structure.starter.log.anno.EnableWebAopLog;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * <p>
 * 日志例子启动器
 * </p>
 *
 * @author chuck
 * @version 1.0.1
 * @since 2021/1/3 14:12
 */
@EnableWebAopLog
@SpringBootApplication
public class LogApplication {
    public static void main(String[] args) {
        SpringApplication.run(LogApplication.class, args);
    }
}
