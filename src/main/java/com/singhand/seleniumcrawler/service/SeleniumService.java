package com.singhand.seleniumcrawler.service;

import com.singhand.seleniumcrawler.selenoium.SeleniumCallable;
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
    private SeleniumCallable seleniumCallable;

    public String domestic(String url, String css) throws ExecutionException, InterruptedException {
        seleniumCallable.setCss(css);
        seleniumCallable.setUrl(url);
        seleniumCallable.setProxyType("domestic");
        Future<String> future = SeleniumThreadPool.seleniumThreadPool.submit(seleniumCallable);
        return future.get();
    }


    public String abroad(String url, String css) throws ExecutionException, InterruptedException {
        seleniumCallable.setCss(css);
        seleniumCallable.setUrl(url);
        seleniumCallable.setProxyType("abroad");
        Future<String> future = SeleniumThreadPool.seleniumThreadPool.submit(seleniumCallable);
        return future.get();
    }

}
