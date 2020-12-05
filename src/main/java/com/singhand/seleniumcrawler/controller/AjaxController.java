package com.singhand.seleniumcrawler.controller;


import com.singhand.seleniumcrawler.selenoium.ProxyType;
import com.singhand.seleniumcrawler.service.SeleniumService;
import com.singhand.seleniumcrawler.threadpool.SeleniumThreadPool;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;


/**
 * @author Kwon
 * @Title: ajax渲染页面爬虫服务
 * @Description:
 * @date 2020/11/20 14:42
 */

@RestController
@Scope("request")
@RequestMapping("selenium")
@Log4j2
public class AjaxController {

    public static SeleniumThreadPool seleniumThreadPool = new SeleniumThreadPool();


    @Autowired
    private SeleniumService seleniumService;

    /**
     * @param url        请求路径
     * @param css        css成功选择器
     * @param isDomestic 是国内代理
     * @return html格式，如果抓取失败返回null
     * @author Kwon
     * @date 2020/11/20 17:26
     */
    @GetMapping("css/{isDomestic}")
    public String css(String url, String css, @PathVariable ProxyType isDomestic) throws ExecutionException, InterruptedException {
        return seleniumService.css(url, css, isDomestic);
    }


    /**
     * @param url        请求路径
     * @param xpath      xpath成功选择器
     * @param isDomestic 是国内代理
     * @return html格式，如果抓取失败返回null
     * @author Kwon
     * @date 2020/11/20 17:26
     */
    @GetMapping("xpath/{isDomestic}")
    public String xpath(String url, String xpath, @PathVariable ProxyType isDomestic) throws ExecutionException, InterruptedException {
        return seleniumService.xpath(url,xpath,isDomestic);
    }

    /**
     * @param url        请求路径
     * @param time      时间
     * @param isDomestic 是国内代理
     * @return html格式，如果抓取失败返回null
     * @author Kwon
     * @date 2020/11/20 17:26
     */
    @GetMapping("time/{isDomestic}")
    public String time(String url, Integer time, @PathVariable ProxyType isDomestic) throws ExecutionException, InterruptedException {
        return seleniumService.time(url,time,isDomestic);
    }


}
