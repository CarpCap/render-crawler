package com.kwon.seleniumcrawler.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author Kwon
 * @Title: selenium驱动配置
 * @Description:
 * @date 2020/11/23 14:27
 */
@Component
@Configuration
public class SeleniumConfiguration {
    @Value("${webdriver.chrome.driver}")
    private String fileName;

    @Autowired
    public void init() {
        System.getProperties().setProperty("webdriver.chrome.driver", fileName);
    }

}
