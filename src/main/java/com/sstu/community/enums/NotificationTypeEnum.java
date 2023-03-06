package com.sstu.community.enums;

public enum NotificationTypeEnum {
    REPLY_QUESTION(1,"回复了问题"),
    REPLY_COMMENT(2,"回复了评论");

    public static String nameOfType(Integer type) {
        for (NotificationTypeEnum notificationTypeEnum : NotificationTypeEnum.values()) {
            if (notificationTypeEnum.getStatus() == type) {
                return notificationTypeEnum.getMessage();
            }
        }
        return "";
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    private int status;
    private String message;

    NotificationTypeEnum(int status, String message) {
        this.status = status;
        this.message = message;
    }
}
