package com.singhand.seleniumcrawler.controller;

import com.singhand.seleniumcrawler.selenoium.SeleniumCallable;
import com.singhand.seleniumcrawler.threadpool.SeleniumThreadPool;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * @author Kwon
 * @Title: ajax渲染页面爬虫服务
 * @Description:
 * @date 2020/11/20 14:42
 */

@RestController
@Scope("request")
@RequestMapping("ajax")
@Log4j2
public class AjaxController {

    public static SeleniumThreadPool seleniumThreadPool = new SeleniumThreadPool();
    @Autowired
    private SeleniumCallable seleniumCallable;

    /**
     * @param url        请求路径
     * @param css        css成功选择器
     * @param isDomestic 是国内代理
     * @return html格式，如果抓取失败返回null
     * @author Kwon
     * @date 2020/11/20 17:26
     */
    @GetMapping("/{css}/{isDomestic}")
    public String ajax(String url, @PathVariable String css, @PathVariable Boolean isDomestic) throws ExecutionException, InterruptedException {
        //todo：根据isDomestic判断是国内还是国外， 处理策略尚未实现
        seleniumCallable.setCss(css);
        seleniumCallable.setUrl(url);

        Future<String> future;
        try {
            future = seleniumThreadPool.submit(seleniumCallable);
        }catch (Exception e){
              log.warn("线程池队列已满，执行拒绝策略");
              return null;
        }

        return future.get();
    }
}
