package com.carpcap.sc.threadpool;

import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * @author CarpCap
 * @Title: 自定义线程池
 * @Description:
 * @date 2020/11/20 15:27
 */
@Component
public class SeleniumThreadPool {
    public final static Integer CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 4;
    public final static Integer MAXIMUM_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 10;
    public final static Integer QUEUE_SIZE = 6;
    public static ThreadPoolExecutor seleniumThreadPool;

    public SeleniumThreadPool() {
        seleniumThreadPool = new ThreadPoolExecutor(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(QUEUE_SIZE));
    }

}