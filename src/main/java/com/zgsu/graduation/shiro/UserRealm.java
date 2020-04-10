package com.zgsu.graduation.shiro;

import com.zgsu.graduation.model.UserInfo;
import com.zgsu.graduation.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashSet;
import java.util.Set;

/**
 * 自定义Realm
 */
public class UserRealm extends AuthorizingRealm {
    @Autowired
    private UserService userService;

    /**
     * 执行授权逻辑
     *
     * @param principalCollection
     * @return
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        System.out.println("执行授权逻辑");
        //给资源授权
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
       // info.addStringPermission("user");
        Subject subject= SecurityUtils.getSubject();
        UserInfo userInfo=(UserInfo)subject.getPrincipal();
        UserInfo dbUser=userService.findUserById(userInfo.getId());
        Set<String> roles=new HashSet<String>();
        roles.add(dbUser.getRole());
        info.setRoles(roles);
        //info.addStringPermission(dbUser.getRole());
        return info;
    }

    /**
     * 执行认证逻辑
     *
     * @param authenticationToken
     * @return
     * @throws AuthenticationException
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        System.out.println("执行认证逻辑");
        //编写shiro判断逻辑
        UsernamePasswordToken token = (UsernamePasswordToken) authenticationToken;
        UserInfo userInfo=userService.login(token.getUsername(), String.valueOf(token.getPassword()));
        if(userInfo==null){
            return null;
        }
        //判断密码
        return new SimpleAuthenticationInfo(userInfo,userInfo.getPassword(),"");

    }
}
