package com.kwon.seleniumcrawler.selenoium.locate;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;


/**
 * @author Kwon
 * @Title: xpath 定位
 * @Description:
 * @date 2020/12/10 14:54
 */
public class Xpath extends Html {

    public Xpath(WebDriver webDriver, String locateValue, Integer pageLoadTimeout) {
        super(webDriver, locateValue, pageLoadTimeout);
    }

    @Override
    public String getPageSource() {
        for (int i = 0; i < pageLoadTimeout; i++) {
            try {
                List<WebElement> elements = webDriver.findElements(By.xpath(locateValue));
                if (elements.size() > 0) {
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
