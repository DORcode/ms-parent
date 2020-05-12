package com.coin.msdict;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients
@ComponentScan(basePackages = {"com.coin"})
public class MsDictApplication {

    private static String[] args;
    private static ConfigurableApplicationContext context;

    public static void main(String[] args) {
        MsDictApplication.args = args;
        context = SpringApplication.run(MsDictApplication.class, args);
    }

    public static void restart() {
        context.close();
        context = SpringApplication.run(MsDictApplication.class, args);

    }

}
