package com.carpcap.rc.controller;


import com.carpcap.rc.dto.*;
import com.carpcap.rc.proxy.ProxyType;
import com.carpcap.rc.selenoium.locate.LocateType;
import com.carpcap.rc.service.SeleniumService;
import com.carpcap.rc.utils.ResponseResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.PageLoadStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.web.bind.annotation.*;


/**
 * @author CarpCap
 * @Title: 渲染页面爬虫服务
 * @Description:
 * @date 2020/11/20 14:42
 */
@Api(description = "爬虫接口")
@RestController
@Scope("request")
@RequestMapping("selenium")
@Log4j2
public class SeleniumController {


    @Autowired
    private SeleniumService seleniumService;

    /**
     * @param css             css成功选择器
     * @param proxyType       代理类型
     * @param pageLoadType    页面加载类型
     * @param pageLoadTimeout 页面加载最长时间
     * @return html格式，如果抓取失败返回null
     * @author CarpCap
     * @date 2020/11/20 17:26
     */
    @PostMapping("css")
    @ApiOperation(value="css定位", notes="", produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name="css",value="参数",required=true,paramType="body",dataType  = "CssDto"),
            @ApiImplicitParam(name="pageLoadType",value="加载类型 \n " +
                    "(1) NONE: 当html下载完成之后，重复去获取半解析状态的html 跟定位符匹配" +
                    "\n" +
                    "(2) EAGER: 要等待整个dom树加载完成，即DOMContentLoaded这个事件完成，仅对html的内容进行下载解析" +
                    "\n" +
                    "(3) NORMAL: 即正常情况下，selenium会等待整个界面加载完成（指对html和子资源的下载与解析,如JS文件，图片等，不包括ajax）\n",defaultValue="NORMAL",paramType="query",dataType  = "PageLoadStrategyDto"),
            @ApiImplicitParam(name="pageLoadTimeout",value="加载超时时间 单位秒",defaultValue="10",paramType="query",dataType="Integer"),
            @ApiImplicitParam(name="proxyType",value="代理类型",defaultValue="OFF",paramType="query",dataType="ProxyType")
            })
    public ResponseResult<String> css(@RequestBody CssDto css, @RequestParam(defaultValue = "NORMAL") PageLoadStrategyDto pageLoadType, @RequestParam(defaultValue = "10") Integer pageLoadTimeout, @RequestParam(defaultValue = "OFF") ProxyType proxyType) throws Exception {
        long startTime = System.currentTimeMillis();
        String result = seleniumService.getPageSource(css.getUrl(), proxyType, LocateType.css, css.getCss(), PageLoadStrategy.valueOf(pageLoadType.name()), pageLoadTimeout);
        return ResponseResultUtil.defaultResult(result, System.currentTimeMillis() - startTime);
    }


    /**
     * @param xpath           xpath成功选择器
     * @param proxyType       代理类型
     * @param pageLoadType    页面加载类型
     * @param pageLoadTimeout 页面加载最长时间
     * @return html格式，如果抓取失败返回null
     * @author CarpCap
     * @date 2020/11/20 17:26
     */
    @PostMapping("xpath")
    @ApiOperation(value="xpath定位", notes="", produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name="xpath",value="参数",required=true,paramType="body",dataType  = "XpathDto"),
            @ApiImplicitParam(name="pageLoadType",value="加载类型 \n " +
                    "(1) NONE: 当html下载完成之后，重复去获取半解析状态的html 跟定位符匹配" +
                    "\n" +
                    "(2) EAGER: 要等待整个dom树加载完成，即DOMContentLoaded这个事件完成，仅对html的内容进行下载解析" +
                    "\n" +
                    "(3) NORMAL: 即正常情况下，selenium会等待整个界面加载完成（指对html和子资源的下载与解析,如JS文件，图片等，不包括ajax）\n",defaultValue="NORMAL",paramType="query",dataType  = "PageLoadStrategyDto"),
            @ApiImplicitParam(name="pageLoadTimeout",value="加载超时时间 单位秒",defaultValue="10",paramType="query",dataType="Integer"),
            @ApiImplicitParam(name="proxyType",value="代理类型",defaultValue="OFF",paramType="query",dataType="ProxyType")
    })
    public ResponseResult<String> xpath(@RequestBody XpathDto xpath, @RequestParam(defaultValue = "NORMAL") PageLoadStrategyDto pageLoadType, @RequestParam(defaultValue = "10") Integer pageLoadTimeout, @RequestParam(defaultValue = "OFF") ProxyType proxyType) throws Exception {
        long startTime = System.currentTimeMillis();
        String result = seleniumService.getPageSource(xpath.getUrl(), proxyType, LocateType.xpath, xpath.getXpath(), PageLoadStrategy.valueOf(pageLoadType.name()), pageLoadTimeout);
        return ResponseResultUtil.defaultResult(result, System.currentTimeMillis() - startTime);
    }

    /**
     * @param pageLoadType 页面加载类型
     * @param proxyType 代理类型
     * @return html格式，如果抓取失败返回null
     * @author CarpCap
     * @date 2021/3/6 15:33
     */
    @PostMapping("time")
    @ApiOperation(value="延迟加载", notes="", produces="application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name="timeDto",value="参数",required=true,paramType="body",dataType  = "TimeDto"),
            @ApiImplicitParam(name="pageLoadType",value="加载类型 \n " +
                    "(1) NONE: 当html下载完成之后，重复去获取半解析状态的html 跟定位符匹配" +
                    "\n" +
                    "(2) EAGER: 要等待整个dom树加载完成，即DOMContentLoaded这个事件完成，仅对html的内容进行下载解析" +
                    "\n" +
                    "(3) NORMAL: 即正常情况下，selenium会等待整个界面加载完成（指对html和子资源的下载与解析,如JS文件，图片等，不包括ajax）\n",defaultValue="NORMAL",paramType="query",dataType  = "PageLoadStrategyDto"),
            @ApiImplicitParam(name="proxyType",value="代理类型",defaultValue="OFF",paramType="query",dataType="ProxyType")
    })
    public ResponseResult<String> time(@RequestBody TimeDto timeDto, @RequestParam(defaultValue = "NORMAL") PageLoadStrategyDto pageLoadType, @RequestParam(defaultValue = "10") Integer pageLoadTimeout, @RequestParam(defaultValue = "OFF") ProxyType proxyType) throws Exception {
        long startTime = System.currentTimeMillis();
        String result = seleniumService.getPageSource(timeDto.getUrl(), proxyType, LocateType.time, null, PageLoadStrategy.valueOf(pageLoadType.name()), pageLoadTimeout);
        return ResponseResultUtil.defaultResult(result, System.currentTimeMillis() - startTime);
    }

}
