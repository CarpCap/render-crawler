package com.kwon.seleniumcrawler.selenoium.observer;

import com.kwon.seleniumcrawler.selenoium.Selenium;
import com.kwon.seleniumcrawler.selenoium.SeleniumSelector;

/**
 * @author Kwon
 * @Title: 观察者-SeleniumSelector
 * @Description:
 * @date 2020/12/1 16:02
 */
public class SeleniumSelectorObserver implements ObserverSelenium {
    /**
     * 注销
     *
     * @author Kwon
     * @date 2020/11/26 10:15
     * @param selenium
     * @return
     */
    @Override
    public void closed(Selenium selenium) {
        SeleniumSelector.unRegister(selenium);
    }


    /**
     * 注册
     *
     * @author Kwon
     * @date 2020/11/26 10:15
     * @param selenium
     * @return
     */
    @Override
    public void created(Selenium selenium) {
        SeleniumSelector.register(selenium);
    }

    @Override
    public void requested(Selenium selenium) {}


}
