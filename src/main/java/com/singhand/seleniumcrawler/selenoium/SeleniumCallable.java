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
    private String css;


    @Autowired
    ProxyDispatchFeign proxyDispatchFeign;

    @Autowired
    SeleniumThreadLoad seleniumThreadLoad;


    @Override
    public String call() {
        String pageSource=null;


        //get WebDriver
        Selenium selenium = seleniumThreadLoad.getSelenium(true);
        selenium.reentrantLock.lock();
        try{
            WebDriver webDriver =  selenium.getWebDriver();
            //http Request
            webDriver.get(url);
            selenium.setTime(System.currentTimeMillis());
            //wait ajax load
            WebDriverWait wait = new WebDriverWait(webDriver, 30,1);
            ExpectedCondition<WebElement> webElementExpectedCondition = ExpectedConditions.presenceOfElementLocated(By.cssSelector(css));
            wait.until(webElementExpectedCondition);

            //return
            pageSource= webDriver.getPageSource();
        }finally {
            selenium.reentrantLock.unlock();
        }
        return pageSource;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setCss(String css) {
        this.css = css;
    }


}
