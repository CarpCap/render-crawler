package com.singhand.seleniumcrawler;

import com.singhand.seleniumcrawler.controller.AjaxController;
import com.singhand.seleniumcrawler.selenoium.Selenium;
import com.singhand.seleniumcrawler.selenoium.SeleniumSelector;
import com.singhand.seleniumcrawler.threadpool.SeleniumThreadPool;
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


    @Autowired
    public void forS(){
        AjaxController.seleniumThreadPool.execute(()->{
                while (true){
                    SeleniumSelector.removeTimeOutSelenium();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

        });

    }

}
