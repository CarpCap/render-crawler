package com.singhand.seleniumcrawler.selenoium;

import com.google.common.collect.Lists;
import com.singhand.bidcrawler.commons.entity.ApplyRequest;
import com.singhand.seleniumcrawler.feign.ProxyDispatchFeign;
import com.singhand.tinycrawler.managercenter.entities.DataPackage;
import com.singhand.tinycrawler.managercenter.entities.Proxy;
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
public class SeleniumThreadLoad {


    @Autowired
    private ProxyDispatchFeign proxyDispatchFeign;

    /**
     * todo 修改获取策略
     * 从线程保险箱拿去内容改为
     * 从Selenium管理组拿内容
     *
     * @param proxyType
     * @return
     * @author Kwon
     * @date 2020/11/30 17:14
     */
    public Selenium getSelenium(String proxyType) throws Exception {
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
    private Selenium createSelenium(String proxyType) {
        if (StringUtils.isBlank(proxyType)) {
            return null;
        }
        //观察者实例
        SeleniumSelectorObserver seleniumSelectorObserver = new SeleniumSelectorObserver();
        List<ObserverSelenium> observerSeleniumList = Lists.newArrayList(seleniumSelectorObserver);

        if ("domestic".equals(proxyType)) {
            //创建国内代理浏览器
            DataPackage<Proxy> domesticProxy = proxyDispatchFeign.getDomesticProxy();
            Proxy proxy = domesticProxy.getData();
            return new Selenium(observerSeleniumList, proxyType, proxy.getIp(), proxy.getPort());
        }
        if ("abroad".equals(proxyType)) {
            ApplyRequest applyRequest = new ApplyRequest();
            applyRequest.setChannel("domesticProxy");
            DataPackage<Proxy> abroadProxy = proxyDispatchFeign.getAbroadProxy(applyRequest);
            Proxy proxy = abroadProxy.getData();
            return new Selenium(observerSeleniumList, proxyType, proxy.getIp(), proxy.getPort());
        }

        return null;
    }
}
