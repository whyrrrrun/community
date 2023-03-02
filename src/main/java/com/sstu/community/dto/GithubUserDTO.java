package com.sstu.community.dto;

import lombok.Data;

@Data
public class GithubUserDTO {
    private String login;
    private Long id;
    private String node_id;
}
