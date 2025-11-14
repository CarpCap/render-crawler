package com.carpcap.rc.selenoium;


import com.google.common.collect.Sets;
import com.carpcap.rc.proxy.ProxyType;
import org.openqa.selenium.PageLoadStrategy;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author CarpCap
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
     * 获取可用的Selenium
     * 没有在执行任务的Selenium
     *
     * @author CarpCap
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
     * 活跃性低的 selenium 集合
     *
     * @param
     * @return
     * @author CarpCap
     * @date 2020/11/25 17:34
     */
    public static Set<Selenium> selectedKeys() {
        Set<Selenium> seleniumSet = new HashSet<>();
        selectedKey.forEach(s -> {
            if (System.currentTimeMillis() - TIME > s.getTime()) {
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
     * @author CarpCap
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
     * @author CarpCap
     * @date 2020/11/26 10:15
     */
    public static void register(Selenium selenium) {
        selectedKey.add(selenium);
    }
}
