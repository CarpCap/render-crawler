package com.singhand.seleniumcrawler.controller;

import com.singhand.seleniumcrawler.selenoium.SeleniumRunnable;
import com.singhand.seleniumcrawler.threadpool.SeleniumThreadPool;
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
@RequestMapping("ajax")
@Scope("request")
public class AjaxController {
    public static SeleniumThreadPool seleniumThreadPool=new SeleniumThreadPool();
    @Autowired
    private SeleniumRunnable seleniumRunnable;

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
    @GetMapping("/{css}/{isDomestic}")
    public String ajax(String url,@PathVariable String css,@PathVariable Boolean isDomestic) throws ExecutionException, InterruptedException {
        //根据isDomestic判断是国内还是国外


        seleniumRunnable.setCss(css);
        seleniumRunnable.setUrl(url);
        return seleniumThreadPool.submit(seleniumRunnable).get();
    }
}
