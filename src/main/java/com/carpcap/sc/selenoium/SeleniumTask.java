package com.carpcap.sc.selenoium;

import com.carpcap.sc.proxy.ProxyType;
import com.carpcap.sc.selenoium.locate.LocateType;
import org.openqa.selenium.PageLoadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.concurrent.Callable;

/**
 *
 *
 * 生命周期策略 request
 * @author CarpCap
 * @Title: selenium执行策略
 * @Description:
 * @date 2020/11/20 15:27
 */
@Component
@Scope("request")
public class  SeleniumTask implements Callable<String> {
    private String url;
    private String locateValue;
    private LocateType locateType;
    private ProxyType proxyType;
    private PageLoadStrategy pageLoadType;
    private Integer pageLoadTimeout = 5;


    @Autowired
    private SeleniumFactory seleniumFactory;


    @Override
    public String call() throws Exception {
        //get WebDriver
        Selenium selenium = SeleniumSelector.getAvailableSelenium(proxyType,pageLoadType);
        if (selenium==null) {
            selenium=seleniumFactory.createSelenium(proxyType,pageLoadType);
        }
        return selenium.getPageSource(url,locateValue,locateType,pageLoadTimeout);
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setLocateValue(String locateValue) {
        this.locateValue = locateValue;
    }

    public void setProxyType(ProxyType proxyType) {
        this.proxyType = proxyType;
    }

    public void setPageLoadTimeout(Integer pageLoadTimeout) {
        this.pageLoadTimeout = pageLoadTimeout;
    }

    public void setLocateType(LocateType locateType) {
        this.locateType = locateType;
    }

    public void setPageLoadType(PageLoadStrategy pageLoadType) {
        this.pageLoadType = pageLoadType;
    }
}
