package com.carpcap.sc.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProxyFactory implements Proxy{
    @Autowired
    private AbroadProxyPool abroadProxyPool;
    @Autowired
    private AomesticProxyPool aomesticProxyPool;

    @Override
    public ProxyInfo getProxy(ProxyType proxyType) {
        switch (proxyType){
            case abroad:
                return abroadProxyPool.pop();
            case deactivate:
                return aomesticProxyPool.pop();
            default:
                return new ProxyInfo();
        }

    }
}
