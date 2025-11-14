package com.carpcap.sc;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author CarpCap
 * @Title:
 * @Description:
 * @date 2020/11/20 14:35
 */
@SpringBootApplication
public class SeleniumApplication {
    public static ConfigurableApplicationContext applicationContext;
    public static void main(String[] args) {
        applicationContext= SpringApplication.run(SeleniumApplication.class, args);

    }

}
