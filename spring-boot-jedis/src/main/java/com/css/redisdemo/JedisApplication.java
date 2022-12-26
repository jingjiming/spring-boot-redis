package com.css.redisdemo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * Created by jiming.jing on 2022/12/26
 */
@SpringBootApplication
public class JedisApplication extends SpringBootServletInitializer implements CommandLineRunner {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(JedisApplication.class);
    }

    @Override
    public void run(String... args) {

        System.out.println(">>>>>>>>>>>>>>>>>>>> 服务启动成功！ >>>>>>>>>>>>>>>>>");
    }

    public static void main(String[] args) {

        SpringApplication.run(JedisApplication.class, args);
    }
}
