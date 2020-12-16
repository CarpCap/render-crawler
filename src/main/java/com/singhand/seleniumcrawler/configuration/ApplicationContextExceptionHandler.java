package com.singhand.seleniumcrawler.configuration;

import com.singhand.seleniumcrawler.utils.DataPackageUtil;
import com.singhand.tinycrawler.managercenter.entities.DataPackage;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Kwon
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
    public DataPackage handleException(Exception ex) {
        return DataPackageUtil.defaultErrorResult(ex.getMessage());
    }
}
