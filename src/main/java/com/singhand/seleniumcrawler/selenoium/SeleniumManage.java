package com.singhand.seleniumcrawler.selenoium;

import org.springframework.stereotype.Component;

import java.util.Set;

/**
 * @author Kwon
 * @Title:
 * @Description:
 * @date 2020/12/10 17:26
 */
@Component
public class SeleniumManage {

    /**
     * 清理掉活跃的浏览器
     *
     * @author Kwon
     * @date 2020/12/10 17:09
     */
    static {
        new Thread(() -> {
            while (true) {
                removeTimeOutSelenium();
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        }).start();
    }

    /**
     * 获取 超时的 selenium 集合
     * 清空 其中的webDriver
     *
     * @param
     * @return
     * @author Kwon
     * @date 2020/11/26 10:15
     */
    public static boolean removeTimeOutSelenium() {
        SeleniumSelector.selectedKeys().forEach(selenium -> {
            selenium.closeSelenium();
        });
        return true;
    }


}
