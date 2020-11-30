package com.singhand.seleniumcrawler.selenoium;


import com.google.common.collect.Sets;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kwon
 * @Title: selenium 选择器
 * @Description:
 * @date 2020/11/25 17:25
 */
public class SeleniumSelector {


    private volatile static Set<Selenium> selectedKey= Sets.newConcurrentHashSet();

    //超时时间 毫秒
    private static Long TIME=60000L;


    /**
     * 返回 超时的 selenium 集合
     *
     * @author Kwon
     * @date 2020/11/25 17:34
     * @param
     * @return
     */
    public static Set<Selenium> selectedKeys(){
        Set<Selenium> seleniumSet=new HashSet<>();

        selectedKey.forEach(s->{
            if (System.currentTimeMillis()-TIME>s.getTime()){
                seleniumSet.add(s);
            }
        });
        return seleniumSet;
    }

    /**
     * 获取 超时的 selenium 集合
     * 清空 其中的webDriver
     *
     * @author Kwon
     * @date 2020/11/26 10:15
     * @param
     * @return
     */
    public static boolean removeTimeOutSelenium(){
        Set<Selenium> seleniumSet = selectedKeys();
        seleniumSet.forEach(selenium->{
           selenium.closeSelenium();
        });
        return true;
    }

    /**
     * 注册
     *
     * @author Kwon
     * @date 2020/11/26 10:15
     * @param selenium
     * @return
     */
    public static boolean register(Selenium selenium){
        selectedKey.add(selenium);
        return true;
    }

    /**
     * 注销
     *
     * @author Kwon
     * @date 2020/11/26 10:15
     * @param selenium
     * @return
     */
    public static boolean unregister(Selenium selenium){
        selectedKey.remove(selenium);
        return true;
    }


}
