package com.singhand.seleniumcrawler.selenoium;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import java.util.List;
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
public class Selenium extends SeleniumAbstract{
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
        notifyObserverSeleniumCreate();
    }
    /**
     * 策略工厂
     *
     * @author Kwon
     * @date 2020/12/1 10:41
     * @param observerSeleniumList
     * @return
     */
    public Selenium(List<ObserverSelenium> observerSeleniumList){
        this.observerSeleniumList=observerSeleniumList;
        init();
    }



    public void init(){
        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("-headless");
        //new webDriver
        webDriver = new ChromeDriver(chromeOptions);
        notifyObserverSeleniumCreate();
    }

    public void closeSelenium() {
        log.info("关闭浏览器实例："+webDriver);
        reentrantLock.lock();
        try{
            webDriver.quit();
            webDriver=null;
            notifyObserverSeleniumClose();
        }catch (Exception e){
                e.printStackTrace();
        }finally {
            reentrantLock.unlock();
        }
    }


    private void notifyObserverSeleniumCreate(){
        observerSeleniumList.forEach(os->{
            os.seleniumCreated(this);
        });
    }

    private void notifyObserverSeleniumClose(){
        observerSeleniumList.forEach(os->{
            os.seleniumClosed(this);
        });
    }




}
