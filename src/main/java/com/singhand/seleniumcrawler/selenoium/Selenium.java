package com.singhand.seleniumcrawler.selenoium;

import com.singhand.seleniumcrawler.proxy.ProxyType;
import com.singhand.seleniumcrawler.selenoium.observer.ObserverSelenium;
import com.singhand.seleniumcrawler.selenoium.observer.SeleniumAbstract;
import com.singhand.seleniumcrawler.selenoium.webdriver.CrawlerMethod;
import com.singhand.seleniumcrawler.selenoium.webdriver.Css;
import com.singhand.seleniumcrawler.selenoium.webdriver.LocateType;
import com.singhand.seleniumcrawler.selenoium.webdriver.Xpath;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
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
    private ProxyType proxyType;

    /**
     * 最后一次活跃时间
     */
    private Long time = System.currentTimeMillis();

    /**
     * 浏览器状态
     * True代表正在执行任务中
     */
    private volatile AtomicBoolean status = new AtomicBoolean(false);

    private String host;

    private Integer port;


    /**
     * 创建Selenium
     *
     * @param observerSeleniumList
     * @return
     * @author Kwon
     * @date 2020/12/1 10:41
     */
    public Selenium(List<ObserverSelenium> observerSeleniumList, ProxyType proxyType, String host, Integer port) {
        this.proxyType = proxyType;
        this.observerSeleniumList = observerSeleniumList;
        this.host = host;
        this.port = port;
        init();
    }


    public void init() {
        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("-headless");
        Proxy proxy = new Proxy();
        log.info("create chrome. proxy info is：" + host + ":" + port);
        proxy.setHttpProxy(host + ":" + port);
        proxy.setSslProxy(host + ":" + port);
        chromeOptions.setProxy(proxy);
        //当访问网站 不会等待渲染全部完成就可以拿到html
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
        //new webDriver
        webDriver = new ChromeDriver(chromeOptions);
        //notify Create event
        notifyObserverSeleniumCreate();
    }

    /**
     * 关闭Selenium
     *
     * @author Kwon
     * @date 2020/12/10 14:50
     * @param
     * @return
     */
    public void closeSelenium() {
        if (!status.get()) {
            reentrantLock.lock();
            try {
                if (!status.get()) {
                    status.set(true);
                    webDriver.quit();
                    webDriver = null;
                    //notify Close event
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
     *
     *
     * @author Kwon
     * @date 2020/12/10 14:35
     * @param url
     * @param locateValue 定位值
     * @param locateType 定位类型
     * @param pageLoadTimeout 最长等待页面加载时间
     * @return
     */
    public String getPageSource(String url, String locateValue, LocateType locateType, Integer pageLoadTimeout){
        Selenium selenium = this;
        reentrantLock.lock();
        try {
            //如果为空代表 浏览器被关闭了
            //重新创建一个Selenium 进行抓取
            if (selenium.webDriver == null) {
                log.warn("webDriver deleted reCreate");
                selenium = new Selenium(observerSeleniumList, proxyType, host, port);
                return selenium.getPageSource(url, locateValue,locateType,pageLoadTimeout);
            }
            selenium.status.set(true);

            //http Request
            selenium.webDriver.get(url);
            //
            selenium.setTime(System.currentTimeMillis());

            //抓取方式
            switch(locateType){
                case css:
                    return new CrawlerMethod(new Css(webDriver,locateValue,pageLoadTimeout)).getPageSource();
                case xpath:
                    return new CrawlerMethod(new Xpath(webDriver,locateValue,pageLoadTimeout)).getPageSource();
            }

            return null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
            requestSum++;
            status.set(false);
        }
        return null;
    }




    /**
     * 通知观察者 发生了创建事件
     *
     * @author Kwon
     * @date 2020/12/10 14:37
     * @param
     * @return
     */
    private void notifyObserverSeleniumCreate() {
        observerSeleniumList.forEach(os -> {
            os.seleniumCreated(this);
        });
    }

    /**
     * 通知观察者 发生了关闭事件
     *
     * @author Kwon
     * @date 2020/12/10 14:38
     * @param
     * @return
     */
    private void notifyObserverSeleniumClose() {
        observerSeleniumList.forEach(os -> {
            os.seleniumClosed(this);
        });
    }


}
