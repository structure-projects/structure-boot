package cn.structured.mybatisplus.generate.example;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan(basePackages = "cn.structured.mybatisplus.generate.example.dao.**")
@SpringBootApplication
public class StructureMybatisPlusGenerateExampleApplication {

    public static void main(String[] args) {
        SpringApplication.run(StructureMybatisPlusGenerateExampleApplication.class, args);
    }

}
