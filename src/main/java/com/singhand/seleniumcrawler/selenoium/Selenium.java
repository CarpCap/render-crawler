package com.singhand.seleniumcrawler.selenoium;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
public class Selenium {
    /**
     * 浏览器实例请求次数.
     */
    private Integer requestSum =0;
    /**
     * 锁
     */
    public ReentrantLock reentrantLock=new ReentrantLock();
    /**
     * 浏览器实例
     */
    private WebDriver webDriver=null;

    /**
     * 最后一次执行任务的时间
     */
    private Long time=System.currentTimeMillis();


    public Selenium(String host,String port){
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("-headless");
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(host+":"+port);
        chromeOptions.setProxy(proxy);
        //new webDriver
        webDriver = new ChromeDriver(chromeOptions);
        SeleniumSelector.register(this);
    }
    public Selenium(){
        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("-headless");
        //new webDriver
        webDriver = new ChromeDriver(chromeOptions);
        SeleniumSelector.register(this);

    }

    public void closeSelenium() {
        log.info("关闭浏览器实例："+webDriver);
        reentrantLock.lock();
        try{
            webDriver.quit();
            webDriver=null;
            SeleniumSelector.unregister(this);
        }catch (Exception e){
            SeleniumSelector.unregister(this);
                e.printStackTrace();
        }finally {
            reentrantLock.unlock();
        }
    }

}
