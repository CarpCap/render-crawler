package com.singhand.seleniumcrawler.controller;


import com.singhand.seleniumcrawler.proxy.ProxyType;
import com.singhand.seleniumcrawler.service.SeleniumService;
import com.singhand.seleniumcrawler.utils.DataPackageUtil;
import com.singhand.tinycrawler.managercenter.entities.DataPackage;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.PageLoadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;


/**
 * @author Kwon
 * @Title: 渲染页面爬虫服务
 * @Description:
 * @date 2020/11/20 14:42
 */

@RestController
@Scope("request")
@RequestMapping("selenium")
@Log4j2
public class SeleniumController {


    @Autowired
    private SeleniumService seleniumService;

    /**
     * @param url             请求路径
     * @param css             css成功选择器
     * @param proxyType       代理类型
     * @param pageLoadType    页面加载类型
     * @param pageLoadTimeout 页面加载最长时间
     * @return html格式，如果抓取失败返回null
     * @author Kwon
     * @date 2020/11/20 17:26
     */
    @GetMapping("css")
    public DataPackage css(@RequestParam String url, @RequestParam String css, @RequestParam(defaultValue = "NORMAL") PageLoadStrategy pageLoadType, @RequestParam(defaultValue = "10") Integer pageLoadTimeout, @RequestParam(defaultValue = "domestic") ProxyType proxyType) throws Exception {
        long startTime = System.currentTimeMillis();
        String result = seleniumService.css(url, css, proxyType, pageLoadType, pageLoadTimeout);
        return DataPackageUtil.defaultResult(result,System.currentTimeMillis() - startTime);
    }


    /**
     * @param url             请求路径
     * @param xpath           xpath成功选择器
     * @param proxyType       代理类型
     * @param pageLoadType    页面加载类型
     * @param pageLoadTimeout 页面加载最长时间
     * @return html格式，如果抓取失败返回null
     * @author Kwon
     * @date 2020/11/20 17:26
     */
    @GetMapping("xpath")
    public DataPackage xpath(@RequestParam String url, @RequestBody String xpath, @RequestParam(defaultValue = "NORMAL") PageLoadStrategy pageLoadType, @RequestParam(defaultValue = "10") Integer pageLoadTimeout,  @RequestParam(defaultValue = "domestic") ProxyType proxyType) throws Exception {
        long startTime = System.currentTimeMillis();
        String result = seleniumService.xpath(url, xpath, proxyType, pageLoadType, pageLoadTimeout);
        return DataPackageUtil.defaultResult(result,System.currentTimeMillis() - startTime);
    }






}
