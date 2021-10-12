package com.kwon.seleniumcrawler.proxy;

import org.springframework.stereotype.Component;

@Component
public class HttpProxy implements Proxy{
    @Override
    public ProxyInfo getProxy(ProxyType proxyType) {
        ProxyInfo proxyInfo=new ProxyInfo();
        return proxyInfo;
    }
}
