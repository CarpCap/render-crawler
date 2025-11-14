package com.carpcap.rc.selenoium.locate;

import org.openqa.selenium.WebDriver;

/**
 * @author CarpCap
 * @Title: html抓取方式 抽象
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
     *
     * @author CarpCap
     * @date 2021/2/28 17:13
     * @param
     * @return
     */
    public abstract String getPageSource();
}
