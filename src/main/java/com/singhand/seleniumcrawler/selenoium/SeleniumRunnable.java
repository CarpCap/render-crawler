package com.singhand.seleniumcrawler.selenoium;

import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * @author Kwon
 * @Title:
 * @Description:
 * @date 2020/11/20 15:27
 */
public class SeleniumRunnable implements Runnable {
    private static ThreadLocal<WebDriver> seleniumThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<Integer> sum = ThreadLocal.withInitial(() -> 0);
    private static ThreadLocal<ChromeOptions> chromeOptionsThreadLocal=new ThreadLocal<>();
    private static final Integer vuew = 10;
    private String url;
    private String css;

    static {
        System.getProperties().setProperty("webdriver.chrome.driver","C:\\Users\\aa3\\Downloads\\chromedriver_win32\\chromedriver.exe");
    }

    public SeleniumRunnable(String url,String css){
        this.css=css;
        this.url=url;

    }

    private WebDriver getWebDriver() {
        if (seleniumThreadLocal.get() == null) {
            seleniumThreadLocal.set(newWebDriver());
            return seleniumThreadLocal.get();
        }
        if (sum.get() > vuew) {
            sum.set(0);
            closeWebDriver(seleniumThreadLocal.get());
            seleniumThreadLocal.set(newWebDriver());
            return seleniumThreadLocal.get();
        }
        sum.set(sum.get() + 1);
        return seleniumThreadLocal.get();
    }


    private WebDriver newWebDriver() {
        ChromeOptions chromeOptions = new ChromeOptions();
//        chromeOptions.addArguments("-headless");

        //todo 代理 加入 申请代理
        // 因为目前调研结果来看 只有在new WebDriver时才能设置代理

        chromeOptionsThreadLocal.set(chromeOptions);
      //开启webDriver进程
        WebDriver webDriver = new ChromeDriver(chromeOptions);
        return webDriver;
    }

    public void closeWebDriver(WebDriver webDriver) {
        seleniumThreadLocal.get().close();
        seleniumThreadLocal.get().quit();
    }
    
    @Override
    public void run() {
        //new WebDriver
        ChromeDriver webDriver = (ChromeDriver) getWebDriver();


        webDriver.get(url);
        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        ExpectedCondition<WebElement> webElementExpectedCondition = ExpectedConditions.presenceOfElementLocated(By.cssSelector(css));
        wait.until(webElementExpectedCondition);

        String s = webDriver.getPageSource();
        System.out.println("url:"+url);
        System.out.println("css:"+css);
        System.out.println();
    }
}
