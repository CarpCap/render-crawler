package com.carpcap.sc.selenoium.observer;

import com.carpcap.sc.selenoium.Selenium;
import com.carpcap.sc.selenoium.SeleniumSelector;

/**
 * @author CarpCap
 * @Title: 观察者-SeleniumSelector
 * @Description:
 * @date 2020/12/1 16:02
 */
public class SeleniumSelectorObserver implements ObserverSelenium {
    /**
     * 注销
     *
     * @author CarpCap
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
     * @author CarpCap
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
