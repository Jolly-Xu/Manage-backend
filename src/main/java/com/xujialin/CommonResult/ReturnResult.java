package com.xujialin.CommonResult;

import lombok.Data;

import java.io.Serializable;

/**
 * @author XuJiaLin
 * @date 2021/8/4 9:58
 */
@Data
public class ReturnResult {
    //返回的状态是否成功
    private Boolean Success;
    //返回的状态码
    private String Code;
    //返回的信息
    private String Message;
    //返回的数据
    private Object Data;

    public ReturnResult() {
    }

    public ReturnResult(Boolean success, String code, String message, Object data) {
        Success = success;
        Code = code;
        Message = message;
        Data = data;
    }

    public ReturnResult(Boolean success){
        this.Success=success;
        this.Code=success?ResultCode.LOGIN_SUCCESS.getCode() : ResultCode.LOGIN_ERROR.getCode();
        this.Message="";
        this.Data=null;
    }

    public ReturnResult(Boolean success,String code,String message){
        Success = success;
        Code = code;
        Message = message;
        Data = null;
    }

    public ReturnResult(Boolean success,Object data){
        Success = success;
        Code = success ? "200" : "404";
        Message = "错误";
        Data = data;
    }

    @Override
    public String toString() {
        return "ReturnResult{" +
                "Success=" + Success +
                ", Code='" + Code + '\'' +
                ", Message='" + Message + '\'' +
                ", Data=" + Data +
                '}';
    }
}
