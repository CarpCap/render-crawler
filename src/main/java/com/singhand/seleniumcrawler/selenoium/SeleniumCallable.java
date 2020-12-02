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
    private String proxyType;

    @Autowired
    ProxyDispatchFeign proxyDispatchFeign;

    @Autowired
    SeleniumThreadLoad seleniumThreadLoad;


    @Override
    public String call() throws Exception {
        String pageSource=null;
        //get WebDriver
        //todo 是否存在空指针异常
        Selenium selenium = seleniumThreadLoad.getSelenium(proxyType);

        selenium.reentrantLock.lock();
        try{
            selenium.getStatus().set(true);
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
            selenium.setRequestSum(selenium.getRequestSum()+1);
            selenium.getStatus().set(false);
        }
        return pageSource;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public void setProxyType(String proxyType) {
        this.proxyType = proxyType;
    }


}
