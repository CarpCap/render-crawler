package com.kwon.seleniumcrawler.selenoium.observer;

import com.kwon.seleniumcrawler.selenoium.Selenium;

/**
 * @author Kwon
 * @Title: Selenium抽象观察者
 * @Description:
 * @date 2020/12/1 9:44
 */
public interface ObserverSelenium {
    /**
     * selenium 关闭完成
     *
     * @author Kwon
     * @date 2020/12/1 10:55
     * @param selenium
     * @return
     */
    void closed(Selenium selenium);

    /**
     *selenium 创建完成
     *
     * @author Kwon
     * @date 2020/12/1 10:56
     * @param selenium
     * @return
     */
    void created(Selenium selenium);
    /**
     *selenium 请求完成
     *
     * @author Kwon
     * @date 2020/12/1 10:56
     * @param selenium
     * @return
     */
    void requested(Selenium selenium);

}
