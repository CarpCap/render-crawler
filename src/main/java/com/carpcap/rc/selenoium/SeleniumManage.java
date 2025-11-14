package com.carpcap.rc.selenoium;

import org.springframework.stereotype.Component;

/**
 * @author CarpCap
 * @Title:
 * @Description:
 * @date 2020/12/10 17:26
 */
@Component
public class SeleniumManage {
    /**
     * 浏览器单例执行任务最大次数.
     */
    private static final Integer REOPEN_REQUEST_SUM = 60;
    /**
     * 浏览器允许最大失败次数
     */
    private static final Integer REOPEN_FAIL_SUM = 2;


    /**
     * 清理掉活跃的浏览器
     *
     * @author CarpCap
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
     * @author CarpCap
     * @date 2020/11/26 10:15
     */
    public static boolean removeTimeOutSelenium() {
        SeleniumSelector.selectedKeys().forEach(selenium -> {
            selenium.closeSelenium();
        });
        return true;
    }


    /**
     * 如果Selenium参数
     *
     * @author CarpCap
     * @date 2020/12/15 10:34
     * @param selenium
     * @return
     */
    public static void removeFailSelenium(Selenium selenium){
        if (selenium.getRequestSum()>=REOPEN_REQUEST_SUM || selenium.getFailSum() >=REOPEN_FAIL_SUM){
            selenium.closeSelenium();
        }
    }


}
