package com.example.diancan2;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.example.diancan2.mapper")
public class Diancan2Application {

    public static void main(String[] args) {
        SpringApplication.run(Diancan2Application.class, args);
    }

}
