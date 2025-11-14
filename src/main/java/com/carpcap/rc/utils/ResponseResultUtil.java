package com.carpcap.rc.utils;


import com.carpcap.rc.dto.ResponseResult;

/**
 * @author CarpCap
 * @Title:
 * @Description:
 * @date 2020/12/16 10:26
 */
public class ResponseResultUtil {
    public static ResponseResult<String> defaultResult(String data, Long requestTime){
        //成功
        ResponseResult<String> dataPackage=new ResponseResult();
        dataPackage.setCode(1);
        dataPackage.setMessage("success");
        dataPackage.setData(data);
        dataPackage.setRequestTime(requestTime);
        return dataPackage;
    }
    public static ResponseResult<String> defaultErrorResult(String message) {
        ResponseResult<String> dataPackage = new ResponseResult();
        dataPackage.setCode(0);
        dataPackage.setMessage(message);
        return dataPackage;
    }

}
