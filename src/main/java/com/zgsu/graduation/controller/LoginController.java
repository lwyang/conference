package com.zgsu.graduation.controller;

import com.zgsu.graduation.Vo.ResultVo;
import com.zgsu.graduation.enums.ErrorEnum;
import com.zgsu.graduation.utils.ResultMsgUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.models.auth.In;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;


@RestController
public class LoginController {
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ResultVo login(@RequestParam("account") String account, @RequestParam("password") String password) {
        /**
         * 使用shiro编写认证操作
         */
        //1获取Subject
        Subject subject = SecurityUtils.getSubject();
        //2封装用户数据
        UsernamePasswordToken token = new UsernamePasswordToken(account, password);
        //3执行登陆方法
        try {
            subject.login(token);
            //登陆成功
            return ResultMsgUtil.success();
        } catch (UnknownAccountException e) {
            //登陆失败:用户名不存在
            return ResultMsgUtil.error(ErrorEnum.UNKNOW_ACCOUNT.getCode(), ErrorEnum.UNKNOW_ACCOUNT.getMsg());
        } catch (IncorrectCredentialsException e) {
            //登陆失败：
            return ResultMsgUtil.error(ErrorEnum.ERROR_PASSWORD.getCode(), ErrorEnum.ERROR_PASSWORD.getMsg());
        }

    }

    @ApiOperation("登出")
    @GetMapping("/logout")
    public ResultVo logout(HttpSession httpSession) {
        Subject subject = SecurityUtils.getSubject();
        subject.logout();
        return ResultMsgUtil.success("退出成功");
    }
    @GetMapping("/unlogin")
    public ResultVo unLogin(){
        return ResultMsgUtil.error(402,"您还未登陆");
    }
}
