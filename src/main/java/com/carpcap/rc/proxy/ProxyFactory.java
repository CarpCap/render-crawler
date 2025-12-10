package com.carpcap.rc.proxy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ProxyFactory implements Proxy{
    @Autowired
    private IntlProxyPool intlProxyPool;
    @Autowired
    private CnProxyPool cnProxyPool;

    @Override
    public ProxyInfo getProxy(ProxyType proxyType) {
        switch (proxyType){
            case INTL:
                return intlProxyPool.pop();
            case CN:
                return cnProxyPool.pop();
            default:
                return new ProxyInfo();
        }

    }
}
