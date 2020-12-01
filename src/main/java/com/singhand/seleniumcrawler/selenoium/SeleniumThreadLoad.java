package com.singhand.seleniumcrawler.selenoium;

import com.google.common.collect.Lists;
import com.singhand.seleniumcrawler.feign.ProxyDispatchFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author Kwon
 * @Title:
 * @Description:
 * @date 2020/11/30 17:04
 */
@Component
public class SeleniumThreadLoad {
    /**
     * Selenium线程保险箱
     */
    public static ThreadLocal<HashMap<String,Selenium>> seleniumThreadLocal = new ThreadLocal<>();
    /**
     * 浏览器实例最大请求次数.
     */
    private static final Integer REOPEN_REQUEST_SUM = 10;

    @Autowired
    private ProxyDispatchFeign proxyDispatchFeign;

    /**
     *
     *
     * @author Kwon
     * @date 2020/11/30 17:14
     * @param proxyType
     * @return
     */
    public  Selenium getSelenium(boolean proxyType) {
        if (proxyType){
            //判断本线程是否有Selenium实例
            if (seleniumThreadLocal.get() == null) {
                HashMap<String, Selenium> seleniumHashMap = new HashMap<>(2);
                seleniumHashMap.put("domestic",createSelenium(proxyType));
                seleniumThreadLocal.set(seleniumHashMap);
                return seleniumThreadLocal.get().get("domestic");
            }

            if (seleniumThreadLocal.get().get("domestic")==null ||seleniumThreadLocal.get().get("domestic").getWebDriver()==null){
                seleniumThreadLocal.get().put("domestic",createSelenium(proxyType));
                return seleniumThreadLocal.get().get("domestic");
            }



            //判断是否需要重新new WebDriver
            if (seleniumThreadLocal.get().get("domestic").getRequestSum() > REOPEN_REQUEST_SUM) {
                seleniumThreadLocal.get().get("domestic").closeSelenium();
                seleniumThreadLocal.get().put("domestic",createSelenium(proxyType));
                return seleniumThreadLocal.get().get("domestic");
            }
            //requestSum+1
            Selenium selenium = seleniumThreadLocal.get().get("domestic");
            selenium.setRequestSum(selenium.getRequestSum()+1);
            //return
            return seleniumThreadLocal.get().get("domestic");
        }

        return seleniumThreadLocal.get().get("domestic");

    }

    /**
     * TODO 工厂模式
     *
     * @author Kwon
     * @date 2020/12/1 9:32
     * @param proxyType
     * @return
     */
    private  Selenium createSelenium(boolean proxyType){
//        //TODO 待实现
//        DataPackage<Proxy> domesticProxy;
//        if (proxyType){
//            domesticProxy = proxyDispatchFeign.getDomesticProxy();
//        }else {
//            ApplyRequest applyRequest = new ApplyRequest();
//            // TODO: 2020/11/30 添加参数
//            applyRequest.setChannel("");
//            applyRequest.setPriority(1);
//            applyRequest.setAnonymity(1);
//
//            domesticProxy = proxyDispatchFeign.getAbroadProxy(applyRequest);
//        }
//        System.out.println(domesticProxy.toString());

        SeleniumSelector seleniumSelector=new SeleniumSelector();
        List<ObserverSelenium> observerSeleniumList = Lists.newArrayList(seleniumSelector);
        Selenium selenium = new Selenium(observerSeleniumList);
        return selenium;
    }
}
