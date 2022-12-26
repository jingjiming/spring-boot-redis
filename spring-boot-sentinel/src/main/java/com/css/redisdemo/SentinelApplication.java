package com.css.redisdemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by jiming.jing on 2022/12/26
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.css"})
public class SentinelApplication extends SpringBootServletInitializer implements CommandLineRunner {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SentinelApplication.class);
    }

    @Override
    public void run(String... args) {

        System.out.println(">>>>>>>>>>>>>>>>>>>> 服务启动成功！ >>>>>>>>>>>>>>>>>");
    }

    public static void main(String[] args) {

        SpringApplication.run(SentinelApplication.class, args);
    }
}
