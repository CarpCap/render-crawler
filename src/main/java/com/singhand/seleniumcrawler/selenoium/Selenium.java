package com.singhand.seleniumcrawler.selenoium;


import com.singhand.tinycrawler.managercenter.entities.DataPackage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.Map;
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
     * 某一个浏览器实例请求次数.
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
    private Long time;


    public Selenium(Boolean b){
        time=System.currentTimeMillis();
        createWebDriver(b);

    }

    private WebDriver createWebDriver(Boolean b) {

        System.out.println("new 一个代理:"+b);
        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("-headless");
        //todo 代理 加入 申请代理
        // 因为目前调研结果来看 只有在new WebDriver时才能设置代理
        // 一个代理只用 REOPEN 次
        //  计算国内与国外代理比例 根据比例分配实例
        //  根据比例计算出理类型
//        DataPackage<Proxy> domesticProxy = proxyDispatchFeign.getDomesticProxy();
//        System.out.println(domesticProxy.toString());
//        Proxy proxy = new Proxy();
//        proxy.setHttpProxy("");
//        chromeOptions.setProxy(proxy);
        //new webDriver
        webDriver = new ChromeDriver(chromeOptions);
        return webDriver;
    }


    public void closeSelenium() {
        log.info("关闭浏览器实例："+webDriver);
        reentrantLock.lock();
        try{
            webDriver.close();
            webDriver.quit();
            webDriver=null;
            SeleniumSelector.unregister(this);
        }catch (Exception e){
                e.printStackTrace();
        }finally {
            reentrantLock.unlock();
        }
    }

}
