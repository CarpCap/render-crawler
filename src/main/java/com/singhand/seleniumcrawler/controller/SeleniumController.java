package com.singhand.seleniumcrawler.controller;


import com.singhand.seleniumcrawler.proxy.ProxyType;
import com.singhand.seleniumcrawler.service.SeleniumService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;


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
     * @param url        请求路径
     * @param css        css成功选择器
     * @param isDomestic 是国内代理
     * @return html格式，如果抓取失败返回null
     * @author Kwon
     * @date 2020/11/20 17:26
     */
    @GetMapping("css/{isDomestic}")
    public String css(String url, String css,Integer pageLoadTimeout, @PathVariable ProxyType isDomestic) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        String result = seleniumService.css(url, css, isDomestic,pageLoadTimeout);
        log.info("消耗时间：{}",System.currentTimeMillis()-startTime);
        return result;
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
    public String xpath(String url, @RequestBody String xpath, Integer pageLoadTimeout, @PathVariable ProxyType isDomestic) throws ExecutionException, InterruptedException {
        long startTime = System.currentTimeMillis();
        String result = seleniumService.xpath(url,xpath,isDomestic,pageLoadTimeout);
        log.info("消耗时间：{}",System.currentTimeMillis()-startTime);
        return result;
    }




}
