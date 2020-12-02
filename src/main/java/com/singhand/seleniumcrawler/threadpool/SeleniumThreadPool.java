package com.singhand.seleniumcrawler.threadpool;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * TODO 线程池消费者为何有序消费 导致新请求来了 有selenium实例的线程没有领取到任务 反而让空实例的线程在执行新的任务
 *
 * @author Kwon
 * @Title: 自定义线程池
 * @Description:
 * @date 2020/11/20 15:27
 */
@Component
public class SeleniumThreadPool {
    @Autowired
    private SeleniumThreadFactory seleniumThreadFactory;
    public final static Integer CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 10;
    //    public final static Integer MAXIMUM_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 10;
    public final static Integer MAXIMUM_POOL_SIZE = 3;
    public final static Integer QUEUE_SIZE = 10;
    public static ThreadPoolExecutor seleniumThreadPool;

    public SeleniumThreadPool(){
        seleniumThreadPool=new ThreadPoolExecutor(MAXIMUM_POOL_SIZE, MAXIMUM_POOL_SIZE, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(QUEUE_SIZE),Executors.defaultThreadFactory(), new ThreadPoolExecutor.CallerRunsPolicy());
    }

}