package com.alien.admin.common.exception;
import com.alien.base.common.exception.AlienException;
import com.alien.base.common.vo.Result;
import com.alien.base.utils.ResultUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author Exrickx
 */
@Slf4j
@RestControllerAdvice
public class RestCtrlExceptionHandler {

    @ExceptionHandler(AlienException.class)
    @ResponseStatus(value = HttpStatus.OK)
    public Result<Object> handleXCloudException(AlienException e) {

        String errorMsg="Alien exception";
        if (e!=null){
            errorMsg = e.getMsg();
            log.error(e.toString(), e);
        }
        return new ResultUtil<>().setErrorMsg(500, errorMsg);
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.OK)
    public Result<Object> handleException(Exception e) {

        String errorMsg="Exception";
        if (e!=null){
            errorMsg = e.getMessage();
            log.error(e.toString(), e);
        }
        return new ResultUtil<>().setErrorMsg(500, errorMsg);
    }
}
