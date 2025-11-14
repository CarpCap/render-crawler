package com.carpcap.rc.configuration;

import com.carpcap.rc.dto.ResponseResult;
import com.carpcap.rc.utils.ResponseResultUtil;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author CarpCap
 * @Title:
 * @Description:
 * @date 2020/12/16 10:15
 */
@ControllerAdvice
public class ApplicationContextExceptionHandler {

    /**
     * 处理全部异常
     */
    @ExceptionHandler
    @ResponseBody
    @ResponseStatus(HttpStatus.OK)
    public ResponseResult handleException(Exception ex) {
        return ResponseResultUtil.defaultErrorResult(ex.getMessage());
    }
}
