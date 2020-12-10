package com.singhand.seleniumcrawler.selenoium.observer;

import com.singhand.seleniumcrawler.selenoium.Selenium;

/**
 * @author Kwon
 * @Title: Selenium抽象观察者
 * @Description:
 * @date 2020/12/1 9:44
 */
public interface ObserverSelenium {
    /**
     * selenium关闭事件
     *
     * @author Kwon
     * @date 2020/12/1 10:55
     * @param selenium
     * @return
     */
    void seleniumClosed(Selenium selenium);

    /**
     *selenium创建事件
     *
     * @author Kwon
     * @date 2020/12/1 10:56
     * @param selenium
     * @return
     */
    void seleniumCreated(Selenium selenium);
}
