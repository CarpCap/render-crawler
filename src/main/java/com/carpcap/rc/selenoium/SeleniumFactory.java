package com.carpcap.rc.selenoium;

import com.google.common.collect.Lists;
import com.carpcap.rc.proxy.ProxyFactory;
import com.carpcap.rc.proxy.ProxyInfo;
import com.carpcap.rc.selenoium.observer.ObserverSelenium;
import com.carpcap.rc.selenoium.observer.SeleniumManageObserver;
import com.carpcap.rc.selenoium.observer.SeleniumSelectorObserver;
import com.carpcap.rc.proxy.ProxyType;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.PageLoadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author CarpCap
 * @Title:
 * @Description:
 * @date 2020/11/30 17:04
 */
@Component
@Log4j2
public class SeleniumFactory {

    @Autowired
    private ProxyFactory proxyFactory;

    /**
     * 工厂模式
     *
     * @param proxyType
     * @return
     * @author CarpCap
     * @date 2020/12/1 9:32
     */
    public Selenium createSelenium(ProxyType proxyType,PageLoadStrategy pageLoadType) {
        log.info("创建selenium proxy:{},加载策略:{}",proxyType,pageLoadType);
        ProxyInfo proxyInfo = proxyFactory.getProxy(proxyType);
        return new Selenium(proxyType, proxyInfo.getHost(), proxyInfo.getPort(),pageLoadType,defaultObserver());
    }



    /**
     * 默认观察者
     * @author CarpCap
     * @date 2020/12/10 17:08
     * @param
     * @return
     */
    private List<ObserverSelenium> defaultObserver(){
        SeleniumSelectorObserver seleniumSelectorObserver = new SeleniumSelectorObserver();
        SeleniumManageObserver seleniumManageObserver=new SeleniumManageObserver();
        List<ObserverSelenium> observerSeleniumList = Lists.newArrayList(seleniumSelectorObserver,seleniumManageObserver);
        return observerSeleniumList;
    }
}
