package com.singhand.seleniumcrawler.selenoium;

import com.singhand.seleniumcrawler.feign.ProxyDispatchFeign;
import jdk.nashorn.internal.runtime.regexp.joni.Config;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.concurrent.Callable;

/**
 * @author Kwon
 * @Title: selenium执行策略
 * @Description:
 * @date 2020/11/20 15:27
 */
@Component
@Scope("request")
public class  SeleniumCallable implements Callable<String> {
    private static ThreadLocal<WebDriver> seleniumThreadLocal = new ThreadLocal<>();
    /**
     * 某一个浏览器实例请求次数.
     */
    private static ThreadLocal<Integer> requestSum = ThreadLocal.withInitial(() -> 0);
    /**
     * 浏览器实例最大请求次数.
     */
    private static final Integer REOPEN = 10;
    private String url;
    private String css;

    @Autowired
    ProxyDispatchFeign proxyDispatchFeign;


    private WebDriver getWebDriver() {
        //判断本线程是否由WebDriver实例
        if (seleniumThreadLocal.get() == null) {
            seleniumThreadLocal.set(newWebDriver());
            return seleniumThreadLocal.get();
        }
        //判断是否需要重新new WebDriver
        if (requestSum.get() > REOPEN) {
            requestSum.set(0);
            closeWebDriver(seleniumThreadLocal.get());
            seleniumThreadLocal.set(newWebDriver());
            return seleniumThreadLocal.get();
        }
        //requestSum+1
        requestSum.set(requestSum.get() + 1);
        //return
        return seleniumThreadLocal.get();
    }


    private WebDriver newWebDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("-headless");

        //todo 代理 加入 申请代理
        // 因为目前调研结果来看 只有在new WebDriver时才能设置代理
        // 一个代理只用 REOPEN 次
//        DataPackage<Proxy> domesticProxy = proxyDispatchFeign.getDomesticProxy();
//        System.out.println(domesticProxy.toString());

        //开启webDriver进程
        WebDriver webDriver = new ChromeDriver(chromeOptions);
        return webDriver;
    }

    public void closeWebDriver(WebDriver webDriver) {
        seleniumThreadLocal.get().close();
        seleniumThreadLocal.get().quit();
    }

    @Override
    public String call() {
        //get WebDriver
        ChromeDriver webDriver = (ChromeDriver) getWebDriver();
        //http Request
        webDriver.get(url);
        //wait ajax load
        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        ExpectedCondition<WebElement> webElementExpectedCondition = ExpectedConditions.presenceOfElementLocated(By.cssSelector(css));
        wait.until(webElementExpectedCondition);
        //return
        return webDriver.getPageSource();
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setCss(String css) {
        this.css = css;
    }


}
