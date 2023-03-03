package com.sstu.community.dto;

import lombok.Data;


//github接口更改
@Data
public class GithubUserDTO {
    private String login;
    private Long id;
    private String node_id;
    private String avatarUrl;

    public String getName() {
        return login;
    }
}
