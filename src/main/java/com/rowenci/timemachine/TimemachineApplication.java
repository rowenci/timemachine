package com.rowenci.timemachine;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.rowenci.timemachine.mapper")
public class TimemachineApplication {

    public static void main(String[] args) {
        SpringApplication.run(TimemachineApplication.class, args);
    }

}
