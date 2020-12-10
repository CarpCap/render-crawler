package com.singhand.seleniumcrawler.selenoium;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
        log.info("创建浏览器 代理信息为：" + host + ":" + port);
        proxy.setHttpProxy(host + ":" + port);
        proxy.setSslProxy(host + ":" + port);
        chromeOptions.setProxy(proxy);
        //当html下载完成之后，不等待解析完成，selenium会直接返回
        chromeOptions.setPageLoadStrategy(PageLoadStrategy.NONE);
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

    public String getPageSource(String url, String locateValue,LocateType locateType,Integer pageLoadTimeout){
        if (LocateType.css==locateType){
            return css(url,locateValue,pageLoadTimeout);
        }
        if (LocateType.xpath==locateType){
            return xpath(url,locateValue,pageLoadTimeout);
        }
        return null;
    }


    /**
     * 请求数据
     * css选择器
     *
     * @param url 请求地址
     * @param css 选择器
     * @return
     * @author Kwon
     * @date 2020/12/2 10:53
     */
    private  String css(String url, String css,Integer pageLoadTimeout) {
        Selenium selenium = this;
        String pageSource;
        reentrantLock.lock();
        try {
            //如果为空代表
            if (selenium.webDriver == null) {
                log.warn("webDriver deleted reCreate");
                selenium = new Selenium(observerSeleniumList, proxyType, host, port);
                return selenium.css(url, css,pageLoadTimeout);
            }
            selenium.status.set(true);
            //http Request

            selenium.webDriver.get(url);
            selenium.setTime(System.currentTimeMillis());

            for (int i = 0; i < pageLoadTimeout; i++) {
                try {
                    String page = selenium.webDriver.getPageSource();
                    Document document = Jsoup.parse(page);
                    Elements select = document.select(css);
                    if (select.size() > 0) {
                        return selenium.webDriver.getPageSource();
                    }
                } catch (Exception e) { }
                Thread.sleep(1000);
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
     * 请求数据
     * css选择器
     *
     * @param url 请求地址
     * @param xpath xpath
     * @return
     * @author Kwon
     * @date 2020/12/2 10:53
     */
    private String xpath(String url, String xpath,Integer pageLoadTimeout) {
        Selenium selenium = this;
        String pageSource;
        reentrantLock.lock();
        try {
            //如果为空代表
            if (selenium.webDriver == null) {
                log.warn("webDriver deleted reCreate");
                selenium = new Selenium(observerSeleniumList, proxyType, host, port);
                return selenium.css(url, xpath,pageLoadTimeout);
            }
            selenium.status.set(true);
            //http Request

            selenium.webDriver.get(url);
            selenium.setTime(System.currentTimeMillis());

            for (int i = 0; i < pageLoadTimeout; i++) {
                try {
                    String page = selenium.webDriver.getPageSource();
                    Document document = Jsoup.parse(page);
                    XPath xpath1 = XPathFactory.newInstance().newXPath();
                    String evaluate = (String)xpath1.evaluate(xpath, document, XPathConstants.STRING);
                    if (StringUtils.isNotBlank(evaluate)) {
                        return selenium.webDriver.getPageSource();
                    }
                } catch (Exception e) { }
                Thread.sleep(1000);
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
