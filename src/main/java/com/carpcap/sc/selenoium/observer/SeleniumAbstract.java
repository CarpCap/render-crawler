package com.carpcap.sc.selenoium.observer;


import java.util.List;

/**
 * @author CarpCap
 * @Title: 观察对象抽象类
 * @Description:
 * @date 2020/12/1 9:53
 */
public abstract class SeleniumAbstract {
    /**
     * selenium
     * 创建 关闭 事件观察者
     *
     * @author CarpCap
     * @date 2020/12/1 9:54
     * @param null
     * @return
     */
    protected List<ObserverSelenium> observerSeleniumList;


    public List<ObserverSelenium> getObserverSeleniumList() {
        return observerSeleniumList;
    }

    public void setObserverSeleniumList(List<ObserverSelenium> observerSeleniumList) {
        this.observerSeleniumList = observerSeleniumList;
    }
}
