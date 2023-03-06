package com.sstu.community.dto;

import com.sstu.community.model.User;
import lombok.Data;

@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private User notifier;
    private String outerTitle;
    private String type;
    private Long outerId;

    public void setTypeName(String typeName) {
        this.type = typeName;
    }
}
