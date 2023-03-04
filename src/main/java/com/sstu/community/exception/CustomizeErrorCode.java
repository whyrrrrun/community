package com.sstu.community.exception;

public enum CustomizeErrorCode implements ICustomizeErrorCode {

    NO_LOGIN(2001, "当前操作需要登录，请登陆后重试"),
    QUESTION_NOT_FOUND( 2002,"问题不存在，要不换个问题试试？"),
    TARGET_NOT_FOUND(2003, "评论对象不存在，请重新选择评论对象。"),
    COMMENT_TYPE_ERROR(2004, "评论对象出错，请重新选择评论对象。"),
    COMMENTC_NOT_FOUND(2005, "评论对象不存在，请重新选择评论对象。"),
    QUESTIONC_NOT_FOUND(2006, "评论对象不存在，请重新选择评论对象。"),
    SYS_ERROR(2007, "服务冒烟了，要不然你稍后再试试？"),
    COMMENT_CONTENT_NULL(2008,"回复内容不能为空！"),
    ;

    private String message;
    private Integer code;

    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public Integer getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

}
