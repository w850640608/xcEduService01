package com.xuecheng.framework.exception;


import com.xuecheng.framework.model.response.ResultCode;

/**
 * @Author wzg
 * @Date 2020/7/9
 * @Version 1.0
 */
public class ExceptionCast {
    public static void cast(ResultCode resultCode){
        throw new CustomException(resultCode);
    };
}
