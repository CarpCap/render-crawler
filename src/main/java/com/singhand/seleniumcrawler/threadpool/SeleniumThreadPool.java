package com.singhand.seleniumcrawler.threadpool;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * todo：需要实现线程池生命周期结束的后续工作
 *
 * @author Kwon
 * @Title: 自定义线程池
 * @Description:
 * @date 2020/11/20 15:27
 */
public class SeleniumThreadPool extends ThreadPoolExecutor {
    public final static Integer CORE_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 10;
    public final static Integer MAXIMUM_POOL_SIZE = Runtime.getRuntime().availableProcessors() * 10;
    public final static Integer QUEUE_SIZE = 20;

    public SeleniumThreadPool() {
        //todo：拒绝策略需要修改，改为队列不够时 自身线程执行
        super(CORE_POOL_SIZE, MAXIMUM_POOL_SIZE, 1, TimeUnit.SECONDS, new LinkedBlockingDeque<>(QUEUE_SIZE));
    }


}