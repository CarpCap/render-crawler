package com.singhand.seleniumcrawler.selenoium.webdriver;

import org.openqa.selenium.WebDriver;

/**
 * html抓取
 * @author Kwon
 * @Title:
 * @Description:
 * @date 2020/12/10 14:56
 */
public abstract class Html {
    WebDriver webDriver;
    String locateValue;
    Integer pageLoadTimeout;

    public Html(WebDriver webDriver,
                String locateValue,
                Integer pageLoadTimeout) {
        this.webDriver=webDriver;
        this.locateValue=locateValue;
        this.pageLoadTimeout=pageLoadTimeout;

    }

    /**
     * 获得html页面
     */
    public abstract String getPageSource() ;
}
