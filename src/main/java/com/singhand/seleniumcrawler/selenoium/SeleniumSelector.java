package com.singhand.seleniumcrawler.selenoium;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.openqa.selenium.WebDriver;

import java.sql.Time;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Kwon
 * @Title: selenium 选择器
 * @Description:
 * @date 2020/11/25 17:25
 */
public class SeleniumSelector {

    private volatile static Set<Selenium> set= Sets.newConcurrentHashSet();

    //超时时间
    private static Long Time=5000L;


    /**
     * 返回
     *
     * @author Kwon
     * @date 2020/11/25 17:34
     * @param
     * @return 超时的 selenium 集合
     */
    public Set<Selenium> selectedKeys(){
        Set<Selenium> seleniumSet=new HashSet<>();
        set.forEach(s->{
            if (System.currentTimeMillis()-Time<s.getTime()){
                seleniumSet.add(s);
            }
        });
        return seleniumSet;
    }


    public boolean removeTimeOutSelenium(){
        Set<Selenium> seleniumSet = selectedKeys();
        seleniumSet.forEach(s->{
            set.remove(s);
        });
        return true;
    }

    public boolean register(Selenium selenium){
        set.add(selenium);
        return true;
    }

}
