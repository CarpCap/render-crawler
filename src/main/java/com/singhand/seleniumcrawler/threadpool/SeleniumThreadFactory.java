package com.singhand.seleniumcrawler.threadpool;

import com.google.common.collect.Lists;
import com.singhand.bidcrawler.commons.entity.ApplyRequest;
import com.singhand.seleniumcrawler.feign.ProxyDispatchFeign;
import com.singhand.seleniumcrawler.selenoium.ObserverSelenium;
import com.singhand.seleniumcrawler.selenoium.Selenium;
import com.singhand.seleniumcrawler.selenoium.SeleniumSelectorObserver;
import com.singhand.tinycrawler.managercenter.entities.DataPackage;
import com.singhand.tinycrawler.managercenter.entities.Proxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * 自定义线程工厂
 * @author Kwon
 * @Title:
 * @Description:
 * @date 2020/12/1 16:17
 */
@Component
public class SeleniumThreadFactory implements ThreadFactory {
    @Autowired
    private ProxyDispatchFeign proxyDispatchFeign;

    private static final AtomicInteger poolNumber = new AtomicInteger(1);
    private final ThreadGroup group;
    private final AtomicInteger threadNumber = new AtomicInteger(1);
    private final String namePrefix;

    SeleniumThreadFactory() {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        namePrefix = "pool-" +
                poolNumber.getAndIncrement() +
                "-thread-";
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);
        //Selenium实例
    
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }




}
