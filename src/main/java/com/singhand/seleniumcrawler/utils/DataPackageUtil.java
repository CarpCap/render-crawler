package com.singhand.seleniumcrawler.utils;

import com.singhand.tinycrawler.managercenter.entities.DataPackage;

/**
 * @author Kwon
 * @Title:
 * @Description:
 * @date 2020/12/16 10:26
 */
public class DataPackageUtil {
    public static DataPackage<String> defaultResult(String data, Long requestTime){
        //成功
        DataPackage<String> dataPackage=new DataPackage();
        dataPackage.setCode(1);
        dataPackage.setMessage("success");
        dataPackage.setData(data);
        dataPackage.setRequestTime(requestTime);
        return dataPackage;
    }
    public static DataPackage<String> defaultErrorResult(String message) {
        DataPackage<String> dataPackage = new DataPackage();
        dataPackage.setCode(0);
        dataPackage.setMessage(message);
        return dataPackage;
    }

}
