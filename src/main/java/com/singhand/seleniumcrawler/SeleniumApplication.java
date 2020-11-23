package com.singhand.seleniumcrawler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

//    @Value("${webdriver.chrome.driver}")
//    private String fileName;

//    @Autowired
//    public void test(){
//        System.getProperties().setProperty("webdriver.chrome.driver", "C:\\Users\\aa3\\Downloads\\chromedriver_win32\\chromedriver.exe");
//        System.out.println("测试注入次数"+fileName);
//    }

}
