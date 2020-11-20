package com.singhand.seleniumcrawler.controller;

import com.singhand.seleniumcrawler.selenoium.SeleniumRunnable;
import com.singhand.seleniumcrawler.threadpool.SeleniumThreadPool;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Kwon
 * @Title:
 * @Description:
 * @date 2020/11/20 14:42
 */
@RestController
@RequestMapping("ajax")
public class AjaxController {
    public static SeleniumThreadPool seleniumThreadPool=new SeleniumThreadPool();

    /**
     *
     *
     * @author Kwon
     * @date 2020/11/20 17:26
     * @param url  请求路径
     * @param css css成功选择器
     * @param isDomestic 是国内代理
     * @return
     */
    @GetMapping("{url}/{css}/{isDomestic}")
    public String ajax(@PathVariable String url,@PathVariable String css,@PathVariable Boolean isDomestic){
        seleniumThreadPool.execute(new SeleniumRunnable("https://whatismyipaddress.com/",".textLayer >div"));
        return url;
    }
}
