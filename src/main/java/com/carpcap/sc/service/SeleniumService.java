package com.carpcap.sc.service;

import com.carpcap.sc.proxy.ProxyType;
import com.carpcap.sc.selenoium.SeleniumTask;
import com.carpcap.sc.selenoium.locate.LocateType;
import com.carpcap.sc.threadpool.SeleniumThreadPool;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.PageLoadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionException;

/**
 * @author CarpCap
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


    public String getPageSource(String url, ProxyType proxyType, LocateType locateType, String locateValue, PageLoadStrategy pageLoadType, Integer pageLoadTimeout) throws Exception {
        seleniumTask.setPageLoadTimeout(pageLoadTimeout);
        seleniumTask.setLocateValue(locateValue);
        seleniumTask.setLocateType(locateType);
        seleniumTask.setUrl(url);
        seleniumTask.setProxyType(proxyType);
        seleniumTask.setPageLoadType(pageLoadType);
        Future<String> future;
        try {
            future = SeleniumThreadPool.seleniumThreadPool.submit(seleniumTask);
        } catch (RejectedExecutionException ex) {
            log.warn("线程池队列已满");
            throw new RejectedExecutionException("负载已满，拒绝本次请求");
        }
        return future.get();
    }
}
