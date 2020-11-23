package com.singhand.seleniumcrawler.threadpool;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author aa3
 */
public class  SeleniumThreadPool extends ThreadPoolExecutor {
    public final static Integer corePoolSize=Runtime.getRuntime().availableProcessors()*4;
    public final static Integer maximumPoolSize = Runtime.getRuntime().availableProcessors()*4;
    public final static Integer queueSize=2;

    public SeleniumThreadPool() {
        super(1,1, 1, TimeUnit.SECONDS,new LinkedBlockingDeque<>(queueSize));
    }


}