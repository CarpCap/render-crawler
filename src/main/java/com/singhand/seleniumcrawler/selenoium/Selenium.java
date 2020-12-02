package com.singhand.seleniumcrawler.selenoium;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


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
    private String proxyType;

    /**
     * 最后一次执行任务的时间
     */
    private Long time = System.currentTimeMillis();

    /**
     * 浏览器状态
     * True代表正在被使用 如 正在爬取网页，正在关闭
     */
    private volatile AtomicBoolean status = new AtomicBoolean(false);


    /**
     * 策略工厂
     *
     * @param observerSeleniumList
     * @return
     * @author Kwon
     * @date 2020/12/1 10:41
     */
    public Selenium(List<ObserverSelenium> observerSeleniumList,String proxyType, String host, Integer port) {
        this.proxyType=proxyType;
        this.observerSeleniumList = observerSeleniumList;
        init(host,port);
    }


    public void init(String host, Integer port) {
        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("-headless");
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(host + ":" + port);
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
