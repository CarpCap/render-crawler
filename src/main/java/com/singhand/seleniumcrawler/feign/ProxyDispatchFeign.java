package com.singhand.seleniumcrawler.feign;

import com.singhand.bidcrawler.commons.feign.description.ProxyDispatch;
import org.springframework.cloud.openfeign.FeignClient;

/**
 * @author Kwon
 * @Title: 代理调度
 * @Description:
 * @date 2020/11/10 15:20
 */
@FeignClient(value = "proxy-dispatch",url = "http://172.18.104.201:7777/proxy-dispatch-service")
public interface ProxyDispatchFeign extends ProxyDispatch {
}
