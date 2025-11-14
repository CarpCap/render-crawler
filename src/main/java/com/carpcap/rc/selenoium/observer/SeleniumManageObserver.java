package com.carpcap.rc.selenoium.observer;

import com.carpcap.rc.selenoium.Selenium;
import com.carpcap.rc.selenoium.SeleniumManage;

/**
 * @author CarpCap
 * @Title:
 * @Description:
 * @date 2020/12/15 10:42
 */
public class SeleniumManageObserver implements ObserverSelenium{


    @Override
    public void closed(Selenium selenium) { }


    @Override
    public void created(Selenium selenium) {}

    /**
     * selenium 请求完成
     *
     * @param selenium
     * @return
     * @author CarpCap
     * @date 2020/12/1 10:56
     */
    @Override
    public void requested(Selenium selenium) {
        SeleniumManage.removeFailSelenium(selenium);
    }
}
