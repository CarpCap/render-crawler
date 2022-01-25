package com.kwon.seleniumcrawler.selenoium;

import com.google.common.collect.Lists;
import com.kwon.seleniumcrawler.proxy.Proxy;
import com.kwon.seleniumcrawler.proxy.ProxyInfo;
import com.kwon.seleniumcrawler.selenoium.observer.ObserverSelenium;
import com.kwon.seleniumcrawler.selenoium.observer.SeleniumManageObserver;
import com.kwon.seleniumcrawler.selenoium.observer.SeleniumSelectorObserver;
import com.kwon.seleniumcrawler.proxy.ProxyType;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.PageLoadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * @author Kwon
 * @Title:
 * @Description:
 * @date 2020/11/30 17:04
 */
@Component
@Log4j2
public class SeleniumFactory {

    @Autowired
    private Proxy proxy;

    /**
     * 工厂模式
     *
     * @param proxyType
     * @return
     * @author Kwon
     * @date 2020/12/1 9:32
     */
    public Selenium createSelenium(ProxyType proxyType,PageLoadStrategy pageLoadType) {
        log.info("创建selenium proxy:{},加载策略:{}",proxyType,pageLoadType);
        //TODO 2022/1/25 14:30 Kwon 调用代理工厂
        ProxyInfo proxyInfo = proxy.getProxy(proxyType);
        return new Selenium(proxyType, proxyInfo.getHost(), proxyInfo.getPort(),pageLoadType,defaultObserver());
    }



    /**
     * 默认观察者
     * @author Kwon
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
