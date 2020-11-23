package com.singhand.seleniumcrawler.selenoium;

import com.singhand.seleniumcrawler.feign.ProxyDispatchFeign;
import com.singhand.tinycrawler.managercenter.entities.DataPackage;
import com.singhand.tinycrawler.managercenter.entities.Proxy;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import javax.swing.text.html.ObjectView;
import java.util.concurrent.Callable;

/**
 * @author Kwon
 * @Title:  selenium执行策略
 * @Description:
 * @date 2020/11/20 15:27
 */
@Component
@Scope("request")
public class SeleniumRunnable implements Callable<String> {
    private static ThreadLocal<WebDriver> seleniumThreadLocal = new ThreadLocal<>();
    private static ThreadLocal<Integer> sum = ThreadLocal.withInitial(() -> 0);
    private static ThreadLocal<ChromeOptions> chromeOptionsThreadLocal=new ThreadLocal<>();
    private static final Integer vuew = 10;
    private String url;
    private String css;

    @Autowired
    ProxyDispatchFeign proxyDispatchFeign;

    static {
        System.getProperties().setProperty("webdriver.chrome.driver","C:\\Users\\aa3\\Downloads\\chromedriver_win32\\chromedriver.exe");
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

//        DataPackage<Proxy> domesticProxy = proxyDispatchFeign.getDomesticProxy();
//        System.out.println(domesticProxy.toString());

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
    public String call() {
        //todo setProxy

        //new WebDriver
        ChromeDriver webDriver = (ChromeDriver) getWebDriver();
        webDriver.get(url);
        WebDriverWait wait = new WebDriverWait(webDriver, 5);
        ExpectedCondition<WebElement> webElementExpectedCondition = ExpectedConditions.presenceOfElementLocated(By.cssSelector(css));
        wait.until(webElementExpectedCondition);
        return webDriver.getPageSource();
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void setCss(String css) {
        this.css = css;
    }


}
