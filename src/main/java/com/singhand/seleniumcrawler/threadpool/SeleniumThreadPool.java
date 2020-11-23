package com.singhand.seleniumcrawler.threadpool;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * todo：需要实现线程池生命周期结束的后续工作
 *
 * @author Kwon
 * @Title:  自定义线程池
 * @Description:
 * @date 2020/11/20 15:27
 */
public class  SeleniumThreadPool extends ThreadPoolExecutor {
    public final static Integer corePoolSize=Runtime.getRuntime().availableProcessors()*4;
    public final static Integer maximumPoolSize = Runtime.getRuntime().availableProcessors()*4;
    public final static Integer queueSize=10;

    public SeleniumThreadPool() {
        super(1,1, 1, TimeUnit.SECONDS,new LinkedBlockingDeque<>(queueSize));
    }


}