package com.singhand.seleniumcrawler.service;

import com.singhand.seleniumcrawler.proxy.ProxyType;
import com.singhand.seleniumcrawler.selenoium.webdriver.LocateType;
import com.singhand.seleniumcrawler.selenoium.SeleniumTask;
import com.singhand.seleniumcrawler.threadpool.SeleniumThreadPool;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 *
 *
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

    public String css(String url, String css, ProxyType isDomestic, Integer pageLoadTimeout) throws ExecutionException, InterruptedException {
        return getString(url, css, isDomestic, pageLoadTimeout, LocateType.css);
    }
    public String xpath(String url, String xpath, ProxyType isDomestic, Integer pageLoadTimeout) throws ExecutionException, InterruptedException {
        return getString(url, xpath, isDomestic, pageLoadTimeout, LocateType.xpath);
    }


    private String getString(String url, String locateValue, ProxyType isDomestic, Integer pageLoadTimeout, LocateType locateType) throws InterruptedException, ExecutionException {
        if (pageLoadTimeout!=null && pageLoadTimeout!=0){
            seleniumTask.setPageLoadTimeout(pageLoadTimeout);
        }
        seleniumTask.setLocateValue(locateValue);
        seleniumTask.setLocateType(locateType);
        seleniumTask.setUrl(url);
        seleniumTask.setProxyType(isDomestic);
        Future<String> future = SeleniumThreadPool.seleniumThreadPool.submit(seleniumTask);
        return future.get();
    }
}
