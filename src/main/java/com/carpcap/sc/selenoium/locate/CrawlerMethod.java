package com.carpcap.sc.selenoium.locate;

import org.openqa.selenium.WebDriver;

/**
 * @author CarpCap
 * @Title:  页面抓取环境类
 * @Description:
 * @date 2020/12/10 15:07
 */
public class CrawlerMethod {

    private Html html;

    public CrawlerMethod(WebDriver webDriver, LocateType locateType, String locateValue, Integer pageLoadTimeout){
        //抓取方式
        switch (locateType) {
            case css:
                this.html = new Css(webDriver,locateValue,pageLoadTimeout);
                break;
            case xpath:
                this.html = new Xpath(webDriver,locateValue,pageLoadTimeout);
            case time:
                this.html = new Time(webDriver);
                break;
            default:
                break;
        }
    }


    public String getPageSource ()  {
        return html.getPageSource();
    }

}
