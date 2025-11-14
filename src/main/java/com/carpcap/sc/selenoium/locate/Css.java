package com.carpcap.sc.selenoium.locate;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.WebDriver;


/**
 * @author CarpCap
 * @Title: css
 * @Description:
 * @date 2020/12/10 14:54
 */
public class Css extends Html {
    public Css(WebDriver webDriver, String locateValue, Integer pageLoadTimeout) {
        super(webDriver, locateValue, pageLoadTimeout);
    }


    @Override
    public String getPageSource() {
        for (int i = 0; i < pageLoadTimeout; i++) {
            try {
                String page = webDriver.getPageSource();
                Document document = Jsoup.parse(page);
                Elements select = document.select(locateValue);
                if (select.size() > 0) {
                    return webDriver.getPageSource();
                }
            } catch (JavascriptException e) {
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}