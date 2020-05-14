package com.coin.msdict.config;

import com.coin.util.BaseException;
import com.coin.util.Result;
import com.coin.util.ResultCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @program: auth-admin
 * @description:
 * @author: kh
 * @create: 2019-04-16 11:00:23
 **/
@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler({ Exception.class })
	@ResponseBody
	public Result exceptionHandler(Exception e, HttpServletResponse response) {
		if (e instanceof BaseException) {
			// log.error("系统Exception", e);
			return Result.fail((BaseException) e);
		} else if (e instanceof DuplicateKeyException) {
			// log.error("系统Exception", e);
			return Result.fail(ResultCodeEnum.UNUNIQUE);
		} else if (e instanceof MethodArgumentNotValidException) {
	        List<ObjectError>  objectErrors = ((MethodArgumentNotValidException) e).getBindingResult().getAllErrors();
	        if(!CollectionUtils.isEmpty(objectErrors)) {
	            StringBuilder msgBuilder = new StringBuilder();
	            for (ObjectError objectError : objectErrors) {
	                msgBuilder.append(objectError.getDefaultMessage()).append(",");
	            }
	            String errorMessage = msgBuilder.toString();
	            if (errorMessage.length() > 1) {
	                errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
	            }
	            return new Result(4001,errorMessage);
	        }
			return new Result(4001,e.getMessage());
		}else if (e instanceof BindException) {
	        List<ObjectError>  objectErrors = ((BindException) e).getBindingResult().getAllErrors();
	        if(!CollectionUtils.isEmpty(objectErrors)) {
	            StringBuilder msgBuilder = new StringBuilder();
	            for (ObjectError objectError : objectErrors) {
	                msgBuilder.append(objectError.getDefaultMessage()).append(",");
	            }
	            String errorMessage = msgBuilder.toString();
	            if (errorMessage.length() > 1) {
	                errorMessage = errorMessage.substring(0, errorMessage.length() - 1);
	            }
	            return new Result(4001,errorMessage);
	        }
			return new Result(4001,e.getMessage());
		} else {
			log.error("系统Exception", e);
			return Result.fail(ResultCodeEnum.SERVER_ERROR);
		}
	}

}
