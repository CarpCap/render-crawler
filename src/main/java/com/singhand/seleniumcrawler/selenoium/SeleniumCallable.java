package com.singhand.seleniumcrawler.selenoium;

import com.singhand.seleniumcrawler.feign.ProxyDispatchFeign;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

/**
 * todo 需要加入删除策略，长时间没有执行任务 进行浏览器删除
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


    @Override
    public String call() {
        String pageSource=null;


        //get WebDriver
        Selenium selenium = Selenium.getSelenium();
        selenium.reentrantLock.lock();
        try{
            ChromeDriver webDriver = (ChromeDriver) selenium.getWebDriver();
            //http Request
            webDriver.get(url);

            Selenium.seleniumThreadLocal.get().setTime(System.currentTimeMillis());
            //wait ajax load
            WebDriverWait wait = new WebDriverWait(webDriver, 5);
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
