package com.example.mynews;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan("com.example.mynews.mapper")
public class MyNewsApplication {

    public static void main(String[] args) {
        SpringApplication.run(MyNewsApplication.class, args);
    }

}
