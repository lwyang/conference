package com.zgsu.graduation.exception;

import com.zgsu.graduation.enums.ResultEnum;
import com.zgsu.graduation.utils.ResultMsgUtil;
import lombok.Data;

@Data
public class LoginException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer errorCode;

    private String errorMsg;

    public LoginException(Integer errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
