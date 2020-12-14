package com.singhand.seleniumcrawler.selenoium.locate;

/**
 * @author Kwon
 * @Title:  页面抓取环境类
 * @Description:
 * @date 2020/12/10 15:07
 */
public class CrawlerMethod {

    private Html html;

    public CrawlerMethod(Html html){
        this.html=html;
    }


    public String getPageSource ()  {
        return html.getPageSource();
    }

}
