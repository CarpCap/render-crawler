package com.kwon.seleniumcrawler.selenoium;

import com.kwon.seleniumcrawler.selenoium.locate.CrawlerMethod;
import com.kwon.seleniumcrawler.selenoium.locate.LocateType;
import com.kwon.seleniumcrawler.selenoium.observer.ObserverSelenium;
import com.kwon.seleniumcrawler.selenoium.observer.SeleniumAbstract;
import com.kwon.seleniumcrawler.proxy.ProxyType;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;


import java.net.URL;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 内置了WebDriver
 *
 * @author Kwon
 * @Title: Selenium实例
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
     * 页面加载策略
     */
    private PageLoadStrategy pageLoadStrategy;

    /**
     * 失败次数
     */
    private Integer failSum = 0;

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
    public Selenium(ProxyType proxyType, String host, Integer port, PageLoadStrategy pageLoadStrategy, List<ObserverSelenium> observerSeleniumList) {
        this.proxyType = proxyType;
        this.observerSeleniumList = observerSeleniumList;
        this.host = host;
        this.port = port;
        this.pageLoadStrategy = pageLoadStrategy;
        init();
    }


    public void init()   {
        ChromeOptions chromeOptions = new ChromeOptions();
        //无头设置
        chromeOptions.addArguments("-headless");
        //禁用图片
        chromeOptions.addArguments("blink-settings=imagesEnabled=false");
        Proxy proxy = new Proxy();
        log.info("create chrome. proxy info is：" + host + ":" + port);
        //如果有代理则设置
        if (StringUtils.isNotBlank(host)&&StringUtils.isNotBlank(port.toString())){
            proxy.setHttpProxy(host + ":" + port);
            proxy.setSslProxy(host + ":" + port);
            chromeOptions.setProxy(proxy);
        }


        //当访问网站 不会等待渲染全部完成就可以拿到html
        //默认normal
        chromeOptions.setPageLoadStrategy(pageLoadStrategy);
        //new webDriver
        webDriver = new ChromeDriver(chromeOptions);

        try {
//            webDriver =new RemoteWebDriver(new URL("http://127.0.0.1:4444/wd/hub/"), chromeOptions);

        } catch (Exception e) {
            e.printStackTrace();
        }
        log.info("hashcode :{}",webDriver.hashCode());
        //notify Create event
        notifyObserverSeleniumCreate();
    }

    /**
     * 关闭Selenium
     *
     * @param
     * @return
     * @author Kwon
     * @date 2020/12/10 14:50
     */
    public void closeSelenium() {
        reentrantLock.lock();
        try {
            log.info("关闭浏览器实例：" + webDriver);
            status.set(true);
            webDriver.quit();
            webDriver = null;
            //notify Close event
            notifyObserverSeleniumClose();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
            status.set(false);
        }

    }

    /**
     * @param url
     * @param locateValue     定位值
     * @param locateType      定位类型
     * @param pageLoadTimeout 最长等待页面加载时间
     * @return
     * @author Kwon
     * @date 2020/12/10 1
     * 4:35
     */
    public String getPageSource(String url, String locateValue, LocateType locateType, Integer pageLoadTimeout) throws Exception {
        Selenium selenium = this;
        reentrantLock.lock();
        try {
            selenium.status.set(true);

            //如果为空代表 浏览器被关闭了
            //重新创建一个Selenium 进行抓取
            if (selenium.webDriver == null) {
                log.warn("webDriver deleted reCreate");
                selenium = new Selenium(proxyType, host, port, pageLoadStrategy, observerSeleniumList);
                return selenium.getPageSource(url, locateValue, locateType, pageLoadTimeout);
            }


            //http Request
            try {
                selenium.webDriver.get(url);
            } catch (WebDriverException e) {
                //Exception close Selenium
                e.printStackTrace();
                closeSelenium();
                throw e;
            }


            String pageSource = null;
            //抓取方式
            pageSource = new CrawlerMethod(webDriver, locateType, locateValue, pageLoadTimeout).getPageSource();

            if (StringUtils.isNotBlank(pageSource)) {
                selenium.setTime(System.currentTimeMillis());
                return pageSource;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
            status.set(false);
            requestSum++;
            notifyObserverSeleniumRequested();
        }
        failSum++;
        throw new Exception("get pageSource fail");
    }


    /**
     * 通知观察者 发生了创建事件
     *
     * @param
     * @return
     * @author Kwon
     * @date 2020/12/10 14:37
     */
    private void notifyObserverSeleniumCreate() {
        observerSeleniumList.forEach(os -> {
            os.created(this);
        });
    }

    /**
     * 通知观察者 发生了关闭事件
     *
     * @param
     * @return
     * @author Kwon
     * @date 2020/12/10 14:38
     */
    private void notifyObserverSeleniumClose() {
        observerSeleniumList.forEach(os -> {
            os.closed(this);
        });
    }

    private void notifyObserverSeleniumRequested() {
        observerSeleniumList.forEach(os -> {
            os.requested(this);
        });
    }


}
