package com.xujialin.CommonResult;

/**
 * @author XuJiaLin
 * @date 2021/8/4 9:55
 */
public enum ResultCode {
    ACCESS_DENY("403","权限不足"),
    DELETE_SUCCESS("200","删除成功"),
    ADD_SUCCESS("200","添加成功"),
    LOGIN_SUCCESS("202","登录成功"),
    LOGIN_ERROR("400","登录失败"),
    NOT_FOUND("404","抱歉,页面未找到"),
    SUCCESS("200","恭喜你，访问成功！"),
    GLOBAL_ERROR("101","系统繁忙,请稍后再试"),
    DELETE_ERROR("400","删除失败,请稍后重试"),
    UPDATA_SUCCESS("400","修改成功"),
    UPDATA_ERROR("200","修改失败"),
    LOGIN_FRIST("406","请先登录"),
    GET_SUCCESS("400","查询成功")
    ;

    private String code;
    private String message;

    ResultCode(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

}
