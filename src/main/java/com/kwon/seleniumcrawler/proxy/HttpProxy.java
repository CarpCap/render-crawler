package com.kwon.seleniumcrawler.proxy;

import org.springframework.stereotype.Component;

@Component
public class HttpProxy implements Proxy{
    @Override
    public ProxyInfo getProxy(ProxyType proxyType) {
        ///TODO 2022/1/25 14:30 Kwon 代理工厂
        ProxyInfo proxyInfo=new ProxyInfo();
        return proxyInfo;
    }
}
