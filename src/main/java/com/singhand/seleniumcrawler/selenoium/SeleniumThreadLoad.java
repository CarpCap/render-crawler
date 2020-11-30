package com.singhand.seleniumcrawler.selenoium;

import java.util.HashMap;

/**
 * @author Kwon
 * @Title:
 * @Description:
 * @date 2020/11/30 17:04
 */
public class SeleniumThreadLoad {
    /**
     * Selenium线程保险箱
     */
    public static ThreadLocal<HashMap<String,Selenium>> seleniumThreadLocal = new ThreadLocal<>();
    /**
     * 浏览器实例最大请求次数.
     */
    private static final Integer REOPEN_REQUEST_SUM = 10;

    public static Selenium getSelenium(Boolean b) {
        if (b){
            //判断本线程是否有Selenium实例
            if (seleniumThreadLocal.get() == null) {
                HashMap<String, Selenium> seleniumHashMap = new HashMap<>(2);
                seleniumHashMap.put("domestic",newSelenium());
                seleniumThreadLocal.set(seleniumHashMap);
                return seleniumThreadLocal.get().get("domestic");
            }

            if (seleniumThreadLocal.get().get("domestic")==null){
                seleniumThreadLocal.get().put("domestic",newSelenium());
                return seleniumThreadLocal.get().get("domestic");
            }

            //判断是否需要重新new WebDriver
            if (seleniumThreadLocal.get().get("domestic").getRequestSum() > REOPEN_REQUEST_SUM) {
                seleniumThreadLocal.get().get("domestic").closeSelenium();
                seleniumThreadLocal.get().put("domestic",newSelenium());
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

    public static  Selenium newSelenium(){
        Selenium selenium = new Selenium(true);
        SeleniumSelector.register(selenium);
        return selenium;
    }



}
