package com.kwon.seleniumcrawler.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author Kwon
 * @Title:
 * @Description:
 * @date 2022/1/25  14:41
 */
@Component
public class AbroadProxyPool  {
    private ArrayBlockingQueue<ProxyInfo> poolQueue=new ArrayBlockingQueue(10);

    public ProxyInfo pop(){
        ProxyInfo poll = poolQueue.poll();
        if (poll==null){
            replenish();
            return pop();
        }

        return poll;
    }
    /**
     * 补充代理池
     *
     * @param
     * @return
     * @author Kwon
     * @date 2022/1/25 14:47
     */
    private void replenish(){

    }

}
