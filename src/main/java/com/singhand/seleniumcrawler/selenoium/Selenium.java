package com.singhand.seleniumcrawler.selenoium;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;


/**
 * @author Kwon
 * @Title:
 * @Description:
 * @date 2020/11/25 17:36
 */
@Getter
@Setter
@Log4j2
public class Selenium extends SeleniumAbstract {
    /**
     * 浏览器实例请求次数.
     */
    private Integer requestSum = 0;
    /**
     * 锁
     */
    public ReentrantLock reentrantLock = new ReentrantLock();
    /**
     * 浏览器实例
     */
    private WebDriver webDriver = null;
    /**
     * 代理类型
     */
    private ProxyType proxyType;

    /**
     * 最后一次执行任务的时间
     */
    private Long time = System.currentTimeMillis();

    /**
     * 浏览器状态
     * True代表正在被使用 如 正在爬取网页，正在关闭
     */
    private volatile AtomicBoolean status = new AtomicBoolean(false);

    private String host;
    private Integer port;


    /**
     * 策略工厂
     *
     * @param observerSeleniumList
     * @return
     * @author Kwon
     * @date 2020/12/1 10:41
     */
    public Selenium(List<ObserverSelenium> observerSeleniumList,ProxyType proxyType, String host, Integer port) {
        this.proxyType=proxyType;
        this.observerSeleniumList = observerSeleniumList;
        this.host=host;
        this.port=port;
        init();
    }


    public void init() {
        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("-headless");
        Proxy proxy = new Proxy();
        log.info("创建浏览器 代理信息为："+host+":"+port);
        proxy.setHttpProxy(host + ":" + port);
        proxy.setSslProxy(host + ":" + port);
        chromeOptions.setProxy(proxy);
        //new webDriver
        webDriver = new ChromeDriver(chromeOptions);
        //notify
        notifyObserverSeleniumCreate();
    }

    public void closeSelenium() {
        if (!status.get()) {
            reentrantLock.lock();
            try {
                if (!status.get()) {
                    status.set(true);
                    webDriver.quit();
                    webDriver = null;
                    notifyObserverSeleniumClose();
                    log.info("关闭浏览器实例：" + webDriver);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                reentrantLock.unlock();
                status.set(false);
            }
        }

    }


    /**
     * 请求数据
     * css选择器
     *
     * @author Kwon
     * @date 2020/12/2 10:53
     * @param url 请求地址
     * @param css 选择器
     * @return
     */
    public String css(String url,String css){
        Selenium selenium=this;
        String pageSource;
        reentrantLock.lock();
        try{
            //如果为空代表
            if (selenium.webDriver==null){
                log.warn("webDriver deleted reCreate");
                selenium = new Selenium(observerSeleniumList,proxyType,host,port);
                return selenium.css(url,css);
            }
            selenium.status.set(true);
            //http Request
            selenium.webDriver.get(url);
            selenium.setTime(System.currentTimeMillis());
            //wait ajax load
            WebDriverWait wait = new WebDriverWait(webDriver, 30,1);
            ExpectedCondition<WebElement> webElementExpectedCondition = ExpectedConditions.presenceOfElementLocated(By.cssSelector(css));
            wait.until(webElementExpectedCondition);

            //return
            pageSource= selenium.webDriver.getPageSource();
        }finally {
            reentrantLock.unlock();
            requestSum++;
            status.set(false);
        }
        return pageSource;
    }


    private void notifyObserverSeleniumCreate() {
        observerSeleniumList.forEach(os -> {
            os.seleniumCreated(this);
        });
    }

    private void notifyObserverSeleniumClose() {
        observerSeleniumList.forEach(os -> {
            os.seleniumClosed(this);
        });
    }



}
