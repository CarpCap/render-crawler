package com.singhand.seleniumcrawler.selenoium;

import com.google.common.collect.Lists;
import com.singhand.bidcrawler.commons.entity.ApplyRequest;
import com.singhand.seleniumcrawler.feign.ProxyDispatchFeign;
import com.singhand.seleniumcrawler.proxy.ProxyType;
import com.singhand.seleniumcrawler.selenoium.observer.ObserverSelenium;
import com.singhand.seleniumcrawler.selenoium.observer.SeleniumSelectorObserver;
import com.singhand.tinycrawler.managercenter.entities.DataPackage;
import com.singhand.tinycrawler.managercenter.entities.Proxy;
import lombok.extern.log4j.Log4j2;
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
    private ProxyDispatchFeign proxyDispatchFeign;

    /**
     * 工厂模式
     *
     * @param proxyType
     * @return
     * @author Kwon
     * @date 2020/12/1 9:32
     */
    public Selenium createSelenium(ProxyType proxyType) {
        switch (proxyType) {
            case domestic:
                return domestic();
            case abroad:
                return abroad();
            default:
                return null;
        }
    }

    /**
     * 创建国内代理浏览器
     *
     * @author Kwon
     * @date 2020/12/10 17:07
     * @param
     * @return
     */
    private Selenium domestic(){
        log.info("创建国内代理浏览器");
        DataPackage<Proxy> domesticProxy = proxyDispatchFeign.getDomesticProxy();
        Proxy proxy = domesticProxy.getData();
        return new Selenium(defaultObserver(), ProxyType.domestic, proxy.getIp(), proxy.getPort());
    }
    /**
     * 创建国外代理浏览器
     *
     * @author Kwon
     * @date 2020/12/10 17:07
     * @param
     * @return
     */
    private Selenium abroad(){
        log.info("创建国外代理浏览器");
        ApplyRequest applyRequest = new ApplyRequest();
        applyRequest.setChannel("twitter");
        DataPackage<Proxy> abroadProxy = proxyDispatchFeign.getAbroadProxy(applyRequest);
        Proxy proxy = abroadProxy.getData();
        return new Selenium(defaultObserver(), ProxyType.abroad, proxy.getIp(), proxy.getPort());
    }

    /**
     * 默认观察者
     *
     * @author Kwon
     * @date 2020/12/10 17:08
     * @param
     * @return
     */
    private List<ObserverSelenium> defaultObserver(){
        SeleniumSelectorObserver seleniumSelectorObserver = new SeleniumSelectorObserver();
        List<ObserverSelenium> observerSeleniumList = Lists.newArrayList(seleniumSelectorObserver);
        return observerSeleniumList;
    }
}
