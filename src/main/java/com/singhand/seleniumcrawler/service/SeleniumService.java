package com.singhand.seleniumcrawler.service;

import com.singhand.seleniumcrawler.proxy.ProxyType;
import com.singhand.seleniumcrawler.selenoium.locate.LocateType;
import com.singhand.seleniumcrawler.selenoium.SeleniumTask;
import com.singhand.seleniumcrawler.threadpool.SeleniumThreadPool;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.PageLoadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author Kwon
 * @Title:
 * @Description:
 * @date 2020/11/30 10:12
 */
@Service
@Log4j2
@Scope("request")
public class SeleniumService {
    @Autowired
    private SeleniumTask seleniumTask;

    public String css(String url, String css, ProxyType proxyType, PageLoadStrategy pageLoadType, Integer pageLoadTimeout) throws ExecutionException, InterruptedException {
        return getString(url, css, proxyType, pageLoadType, pageLoadTimeout, LocateType.css);
    }

    public String xpath(String url, String xpath, ProxyType proxyType, PageLoadStrategy pageLoadType, Integer pageLoadTimeout) throws ExecutionException, InterruptedException {
        return getString(url, xpath, proxyType, pageLoadType, pageLoadTimeout, LocateType.xpath);
    }


    private String getString(String url, String locateValue, ProxyType proxyType, PageLoadStrategy pageLoadType, Integer pageLoadTimeout, LocateType locateType) throws InterruptedException, ExecutionException {
        if (pageLoadType==null){
            pageLoadType=PageLoadStrategy.NORMAL;
        }
        seleniumTask.setPageLoadTimeout(pageLoadTimeout);
        seleniumTask.setLocateValue(locateValue);
        seleniumTask.setLocateType(locateType);
        seleniumTask.setUrl(url);
        seleniumTask.setProxyType(proxyType);
        seleniumTask.setPageLoadType(pageLoadType);
        Future<String> future = SeleniumThreadPool.seleniumThreadPool.submit(seleniumTask);
        return future.get();
    }
}
