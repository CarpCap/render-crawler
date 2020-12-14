package com.singhand.seleniumcrawler.selenoium;


import com.google.common.collect.Sets;
import com.singhand.seleniumcrawler.proxy.ProxyType;
import org.openqa.selenium.PageLoadStrategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kwon
 * @Title: selenium 选择器
 * @Description:
 * @date 2020/11/25 17:25
 */
public class SeleniumSelector {

    private volatile static Set<Selenium> selectedKey = Collections.synchronizedSet(Sets.newLinkedHashSet());

    /**
     * 活跃判定时间，毫秒
     */
    private static Long TIME = 60000L;
    /**
     * 浏览器单例执行任务最大次数.
     */
    private static final Integer REOPEN_REQUEST_SUM = 60;
    /**
     * 浏览器允许最大失败次数
     */
    private static final Integer REOPEN_FAIL_SUM = 3;


    /**
     * 获取可用的Selenium
     * 没有在执行任务的Selenium
     *
     * @author Kwon
     * @date 2020/12/2 10:08
     * @param proxyType
     * @return
     */
    public static Selenium getAvailableSelenium(ProxyType proxyType)  {
        for (Selenium s : selectedKey) {
            if (proxyType.equals(s.getProxyType()) && s.getStatus().get() == false) {
                return s;
            }
        }
        return null;
    }

    /**
     * 获取可用的Selenium
     * 没有在执行任务的Selenium
     *
     * @author Kwon
     * @date 2020/12/2 10:08
     * @param proxyType
     * @return
     */
    public static Selenium getAvailableSelenium(ProxyType proxyType, PageLoadStrategy pageLoadType)  {
        for (Selenium s : selectedKey) {
            if (proxyType.equals(s.getProxyType()) && s.getStatus().get() == false && s.getPageLoadStrategy()==pageLoadType) {
                return s;
            }
        }
        return null;
    }


    /**
     * 
     * 活跃性低
     * 执行过多任务
     * 失败过多
     * 的 selenium 集合
     *
     * @param
     * @return
     * @author Kwon
     * @date 2020/11/25 17:34
     */
    public static Set<Selenium> selectedKeys() {
        Set<Selenium> seleniumSet = new HashSet<>();
        selectedKey.forEach(s -> {
            if (System.currentTimeMillis() - TIME > s.getTime() || s.getRequestSum()>=REOPEN_REQUEST_SUM || s.getFailSum() >=REOPEN_FAIL_SUM) {
                seleniumSet.add(s);
            }
        });
        return seleniumSet;
    }




    /**
     * 注销
     *
     * @param selenium
     * @return
     * @author Kwon
     * @date 2020/11/26 10:15
     */
    public static void unRegister(Selenium selenium) {
        selectedKey.remove(selenium);
    }


    /**
     * 注册
     *
     * @param selenium
     * @return
     * @author Kwon
     * @date 2020/11/26 10:15
     */
    public static void register(Selenium selenium) {
        selectedKey.add(selenium);
    }
}
