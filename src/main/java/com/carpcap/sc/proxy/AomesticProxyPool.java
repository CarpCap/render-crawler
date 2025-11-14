package com.carpcap.sc.proxy;

import org.springframework.stereotype.Component;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author CarpCap
 * @Title: 国内代理池
 * @Description:
 * @date 2022/1/25  14:40
 */
@Component
public class AomesticProxyPool {
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
     * @author CarpCap
     * @date 2022/1/25 14:47
     */
    private void replenish(){

    }
}
