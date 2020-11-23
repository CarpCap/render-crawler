package com.singhand.seleniumcrawler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author Kwon
 * @Title:
 * @Description:
 * @date 2020/11/20 14:35
 */
@SpringBootApplication
@EnableFeignClients
public class SeleniumApplication {
    public static void main(String[] args) {
        SpringApplication.run(SeleniumApplication.class, args);
    }
}
