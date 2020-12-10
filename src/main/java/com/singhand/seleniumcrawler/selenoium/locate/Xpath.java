package com.singhand.seleniumcrawler.selenoium.locate;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.WebDriver;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * @Title:
 * @Description:
 * @author Kwon
 * @date 2020/12/10 14:54
 */
public class Xpath extends Html{

    public Xpath(WebDriver webDriver, String locateValue, Integer pageLoadTimeout) {
        super(webDriver, locateValue, pageLoadTimeout);
    }

    @Override
    public String getPageSource() {
        for (int i = 0; i < pageLoadTimeout; i++) {
            try {
                String page = webDriver.getPageSource();
                Document document = Jsoup.parse(page);
                XPath xpath1 = XPathFactory.newInstance().newXPath();
                String evaluate = (String)xpath1.evaluate(locateValue, document, XPathConstants.STRING);
                if (StringUtils.isNotBlank(evaluate)) {
                    return webDriver.getPageSource();
                }
            } catch (Exception e) { }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return null;
    }


}
