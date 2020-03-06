package com.zgsu.graduation.utils;

import com.zgsu.graduation.Vo.ResultVo;
import com.zgsu.graduation.enums.ResultEnum;

/**
 * @Author: yanglinwei
 * @Date: 2020/1/7 9:50
 */
public class ResultMsgUtil {
    public static ResultVo success(Object object) {
        ResultVo resultVo = new ResultVo();
        resultVo.setData(object);
        resultVo.setCode(ResultEnum.SUCCESS.getCode());
        resultVo.setMsg(ResultEnum.SUCCESS.getMessage());
        return resultVo;

    }

    public static ResultVo success() {
        return success(null);
    }

    public static ResultVo error(Integer code, String msg) {

        ResultVo resultVO = new ResultVo();
        resultVO.setMsg(msg);
        resultVO.setCode(code);

        return resultVO;
    }

}
