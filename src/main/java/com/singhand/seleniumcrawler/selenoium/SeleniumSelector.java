package com.singhand.seleniumcrawler.selenoium;


import com.google.common.collect.Sets;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Kwon
 * @Title: selenium 选择器
 * @Description:
 * @date 2020/11/25 17:25
 */
public class SeleniumSelector {

    private volatile static Set<Selenium> selectedKey = Sets.newConcurrentHashSet();

    /**
     * 超时时间，毫秒
     */
    private static Long TIME = 30000L;
    /**
     * 浏览器实例最大请求次数.
     */
    private static final Integer REOPEN_REQUEST_SUM = 10;

    static {
        new Thread(() -> {
            while (true) {
                SeleniumSelector.removeTimeOutSelenium();
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();


    }

    /**
     * 获取可用的Selenium
     *
     * @author Kwon
     * @date 2020/12/2 10:08
     * @param proxyType
     * @return
     */
    public static Selenium getAvailableSelenium(String proxyType) throws Exception {
        if (StringUtils.isBlank(proxyType)) {
            throw new Exception();
        }
        for (Selenium s : selectedKey) {
            if (proxyType.equals(s.getProxyType()) && s.getStatus().get() == false) {
                s.getStatus().set(true);
                return s;
            }
        }

        return null;
    }

    /**
     * 返回 超时的 or 超过请求数的 selenium 集合
     *
     * @param
     * @return
     * @author Kwon
     * @date 2020/11/25 17:34
     */
    public static Set<Selenium> selectedKeys() {
        Set<Selenium> seleniumSet = new HashSet<>();

        selectedKey.forEach(s -> {
            if (System.currentTimeMillis() - TIME > s.getTime() || s.getRequestSum()>=REOPEN_REQUEST_SUM) {
                seleniumSet.add(s);
            }
        });
        return seleniumSet;
    }


    /**
     * 获取 超时的 selenium 集合
     * 清空 其中的webDriver
     *
     * @param
     * @return
     * @author Kwon
     * @date 2020/11/26 10:15
     */
    public static boolean removeTimeOutSelenium() {
        Set<Selenium> seleniumSet = selectedKeys();
        seleniumSet.forEach(selenium -> {
            selenium.closeSelenium();
        });
        return true;
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
