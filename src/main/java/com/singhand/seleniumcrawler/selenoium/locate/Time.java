package com.singhand.seleniumcrawler.selenoium.locate;


import org.openqa.selenium.JavascriptException;
import org.openqa.selenium.WebDriver;

public class Time extends Html{
    public Time(WebDriver webDriver, Integer pageLoadTimeout) {
        super(webDriver, null, pageLoadTimeout);
    }

    @Override
    public String getPageSource() {
        try {
            return webDriver.getPageSource();
        } catch (JavascriptException e) {
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
