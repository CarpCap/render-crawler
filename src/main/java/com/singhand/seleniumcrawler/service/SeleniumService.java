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
 * TODO 代理策略如何实现
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


    public static SeleniumThreadPool seleniumThreadPool = new SeleniumThreadPool();


    public String domestic(String url, String css) throws ExecutionException, InterruptedException {
        seleniumCallable.setCss(css);
        seleniumCallable.setUrl(url);
        Future<String> future = seleniumThreadPool.submit(seleniumCallable);
        return future.get();
    }


    public String foreign(String url, String css) throws ExecutionException, InterruptedException {
        seleniumCallable.setCss(css);
        seleniumCallable.setUrl(url);
        Future<String> future = seleniumThreadPool.submit(seleniumCallable);
        return future.get();
    }

}
