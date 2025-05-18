package org.example.bws;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan
public class BwsApplication {

    public static void main(String[] args) {
        SpringApplication.run(BwsApplication.class, args);
    }

}
