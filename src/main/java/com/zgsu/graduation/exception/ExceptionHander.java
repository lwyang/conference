package com.zgsu.graduation.exception;

import com.zgsu.graduation.Vo.ResultVo;
import com.zgsu.graduation.utils.ResultMsgUtil;
import org.apache.shiro.authz.UnauthenticatedException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ExceptionHander {
    @ExceptionHandler(value = LoginException.class)
    public ResultVo loginExceptionHander(HttpServletRequest req, LoginException e) {
        return ResultMsgUtil.error(e.getErrorCode(), e.getErrorMsg());
    }

    @ExceptionHandler(value = UnauthorizedException.class)
    @ResponseBody
    public ResultVo UnauthorizedExceptionHander(HttpServletRequest req, UnauthorizedException e) {
        return ResultMsgUtil.error(401, "您没有权限");
    }

    @ExceptionHandler(value = NullPointerException.class)
    @ResponseBody
    public ResultVo NullPointerExceptionHander(HttpServletRequest req, NullPointerException e) {
        return ResultMsgUtil.error(501, "空指针");
    }

    @ExceptionHandler(value = UnauthenticatedException.class)
    @ResponseBody
    public ResultVo UnauthenticatedExceptionHander(HttpServletRequest req, UnauthenticatedException e) {
        return ResultMsgUtil.error(402, "您未登陆");
    }
}
