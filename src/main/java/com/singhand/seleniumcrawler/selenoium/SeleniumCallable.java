package com.singhand.seleniumcrawler.selenoium;

import com.singhand.seleniumcrawler.feign.ProxyDispatchFeign;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Callable;

/**
 *
 *
 * 生命周期策略 request
 * @author Kwon
 * @Title: selenium执行策略
 * @Description:
 * @date 2020/11/20 15:27
 */
@Component
@Scope("request")
public class  SeleniumCallable implements Callable<String> {
    private String url;
    private String locateValue;
    private LocateType locateType;
    private ProxyType proxyType;
    private Integer pageLoadTimeout = 5;

    @Autowired
    ProxyDispatchFeign proxyDispatchFeign;

    @Autowired
    SeleniumThreadLoad seleniumThreadLoad;


    @Override
    public String call() throws Exception {
        //get WebDriver
        Selenium selenium = seleniumThreadLoad.getSelenium(proxyType);

        return selenium.getPageSource(url,locateValue,locateType,pageLoadTimeout);
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setLocateValue(String locateValue) {
        this.locateValue = locateValue;
    }

    public void setProxyType(ProxyType proxyType) {
        this.proxyType = proxyType;
    }

    public void setPageLoadTimeout(Integer pageLoadTimeout) {
        this.pageLoadTimeout = pageLoadTimeout;
    }

    public void setLocateType(LocateType locateType) {
        this.locateType = locateType;
    }
}
