package com.carpcap.rc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * @author CarpCap
 * @Title: selenium驱动配置
 * @Description:
 * @date 2020/11/23 14:27
 */
@Component
@Configuration
public class SeleniumConfiguration {
    @Value("${webdriver.chrome.driver}")
    private String fileName;

    @Value("${webdriver.type}")
    private String type;

    @Autowired
    public void init() {
        if ("local".equals(type)){
            System.getProperties().setProperty("webdriver.chrome.driver", fileName);

        }
    }

}
