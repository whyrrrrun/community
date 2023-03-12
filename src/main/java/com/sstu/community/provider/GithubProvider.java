package com.sstu.community.provider;


import com.alibaba.fastjson.JSON;
import com.sstu.community.dto.AccessTokenDTO;
import com.sstu.community.dto.GithubUserDTO;
import lombok.SneakyThrows;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {



    @SneakyThrows
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType, JSON.toJSONString(accessTokenDTO));
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            try {
                return response.body().string().split("&")[0].split("=")[1];
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }


    public GithubUserDTO getUser(
            String accessToken
    ){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user").header("Authorization", "token ".concat(accessToken))
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUserDTO githubUserDTO = JSON.parseObject(string, GithubUserDTO.class);
            return githubUserDTO;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }



}
