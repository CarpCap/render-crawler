package com.carpcap.rc.selenoium.locate;

import org.openqa.selenium.WebDriver;

public class Time extends Html{
    public Time(WebDriver webDriver) {
        super(webDriver, null, null);
    }

    @Override
    public String getPageSource() {
        return webDriver.getPageSource();
    }
}
