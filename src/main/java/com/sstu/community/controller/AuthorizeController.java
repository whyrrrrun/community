package com.sstu.community.controller;


import com.sstu.community.dto.AccessTokenDTO;
import com.sstu.community.dto.GithubUserDTO;
import com.sstu.community.mapper.UserMapper;
import com.sstu.community.model.User;
import com.sstu.community.provider.GithubProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;
    @Autowired
    private UserMapper userMapper;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client.secret}")
    private String clientSecret;
    @Value("${github.redirect.uri}")
    private String redirectUri;



    @GetMapping("/callback")
    public String Callback(
            @RequestParam("code") String code,
            @RequestParam("state") String state,
            HttpServletRequest request
    ){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        System.out.println(accessToken);
        GithubUserDTO githubUserDTO = githubProvider.getUser(accessToken);
        System.out.println(githubUserDTO.getLogin());
        if(githubUserDTO != null){
            //登陆成功，保存cookie和session
            User user = new User();
            user.setToken(UUID.randomUUID().toString());
            user.setName(githubUserDTO.getLogin());
            user.setAccountId(String.valueOf(githubUserDTO.getId()));
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
            request.getSession().setAttribute("user",githubUserDTO);

        }else{
            //登录失败，请重新登陆！
        }
        return "redirect:/";
    }
}
