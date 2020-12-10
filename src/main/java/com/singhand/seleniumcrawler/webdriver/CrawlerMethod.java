package com.singhand.seleniumcrawler.webdriver;

/**
 * 页面抓取环境类
 *
 * @author Kwon
 * @Title:
 * @Description:
 * @date 2020/12/10 15:07
 */
public class CrawlerMethod {

    private Html html;

    public CrawlerMethod(Html html){
        this.html=html;
    }

    public Html getStrategy() {
        return html;
    }

    public String getPageSource ()  {
        return html.getPageSource();
    }

}
