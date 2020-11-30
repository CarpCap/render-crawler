package com.singhand.seleniumcrawler.selenoium;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
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
    public static ThreadLocal<Selenium> seleniumThreadLocal = new ThreadLocal<>();
    /**
     * 某一个浏览器实例请求次数.
     */
    private Integer requestSum =0;
//    public static ThreadLocal<Integer> requestSum = ThreadLocal.withInitial(() -> 0);
    /**
     * 浏览器实例最大请求次数.
     */
    private static final Integer REOPEN_REQUEST_SUM = 10;

    public ReentrantLock reentrantLock=new ReentrantLock();

    private WebDriver webDriver=null;
    private Long time;

    public Selenium(){
        time=System.currentTimeMillis();
        newWebDriver();
    }

    public static Selenium getSelenium() {
        //判断本线程是否由Selenium实例
        if (seleniumThreadLocal.get() == null || seleniumThreadLocal.get().getWebDriver()==null) {
            seleniumThreadLocal.set(newSelenium());
            return seleniumThreadLocal.get();
        }
        //判断是否需要重新new WebDriver
        if (seleniumThreadLocal.get().requestSum > REOPEN_REQUEST_SUM) {
            seleniumThreadLocal.get().closeSelenium();
            seleniumThreadLocal.set(newSelenium());
            return seleniumThreadLocal.get();
        }
        //requestSum+1
        seleniumThreadLocal.get().requestSum++;
        //return
        return seleniumThreadLocal.get();
    }

    public static  Selenium newSelenium(){
        Selenium selenium = new Selenium();
        SeleniumSelector.register(selenium);
        return selenium;
    }




    private  WebDriver newWebDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("-headless");

        //todo 代理 加入 申请代理
        // 因为目前调研结果来看 只有在new WebDriver时才能设置代理
        // 一个代理只用 REOPEN 次
//        DataPackage<Proxy> domesticProxy = proxyDispatchFeign.getDomesticProxy();
//        System.out.println(domesticProxy.toString());

        //开启webDriver进程
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
