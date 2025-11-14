package com.carpcap.rc.proxy;

/**
 * @author CarpCap
 * @Title: 代理调度
 * @Description:
 * @date 2020/11/10 15:20
 */

public interface Proxy {
    /**
     * 获取一个代理
     *
     * @param proxyType
     * @return ip:port
     * @author CarpCap
     * @date 2021/10/11 13:23
     */
    ProxyInfo getProxy(ProxyType proxyType);
}
