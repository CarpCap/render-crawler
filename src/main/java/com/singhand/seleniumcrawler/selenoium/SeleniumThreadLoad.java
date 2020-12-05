package com.singhand.seleniumcrawler.selenoium;

import com.google.common.collect.Lists;
import com.singhand.bidcrawler.commons.entity.ApplyRequest;
import com.singhand.seleniumcrawler.feign.ProxyDispatchFeign;
import com.singhand.tinycrawler.managercenter.entities.DataPackage;
import com.singhand.tinycrawler.managercenter.entities.Proxy;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang.StringUtils;
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
public class SeleniumThreadLoad {


    @Autowired
    private ProxyDispatchFeign proxyDispatchFeign;

    /**
     *
     * 从线程保险箱拿去内容改为
     * 从Selenium管理组拿内容
     *
     * @param proxyType
     * @return
     * @author Kwon
     * @date 2020/11/30 17:14
     */
    public Selenium getSelenium(ProxyType proxyType) throws Exception {
        Selenium selenium = SeleniumSelector.getAvailableSelenium(proxyType);
        if (selenium==null) {
            return createSelenium(proxyType);
        }
        return selenium;
    }

    /**
     * @param proxyType
     * @return
     * @author Kwon
     * @date 2020/12/1 9:32
     */
    private Selenium createSelenium(ProxyType proxyType) {

        //观察者实例
        SeleniumSelectorObserver seleniumSelectorObserver = new SeleniumSelectorObserver();
        List<ObserverSelenium> observerSeleniumList = Lists.newArrayList(seleniumSelectorObserver);

        if (ProxyType.domestic==proxyType) {
            //创建国内代理浏览器
            log.info("创建国内代理浏览器");
            DataPackage<Proxy> domesticProxy = proxyDispatchFeign.getDomesticProxy();
            Proxy proxy = domesticProxy.getData();
            return new Selenium(observerSeleniumList, proxyType, proxy.getIp(), proxy.getPort());
        }
        if (ProxyType.abroad==proxyType) {
            log.info("创建国外代理浏览器");
            ApplyRequest applyRequest = new ApplyRequest();
            applyRequest.setChannel("twitter");
            DataPackage<Proxy> abroadProxy = proxyDispatchFeign.getAbroadProxy(applyRequest);
            Proxy proxy = abroadProxy.getData();
            return new Selenium(observerSeleniumList, proxyType, proxy.getIp(), proxy.getPort());
        }

        return null;
    }
}
