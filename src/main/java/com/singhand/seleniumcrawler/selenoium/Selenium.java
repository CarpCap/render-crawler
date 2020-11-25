package com.singhand.seleniumcrawler.selenoium;

import lombok.Data;
import org.openqa.selenium.WebDriver;

/**
 * @author Kwon
 * @Title:
 * @Description:
 * @date 2020/11/25 17:36
 */
@Data
public class Selenium {
    private WebDriver webDriver=null;
    private Long time;
}
