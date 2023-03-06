package com.sstu.community.controller;

import com.sstu.community.dto.NotificationDTO;
import com.sstu.community.dto.PaginationDTO;
import com.sstu.community.dto.QuestionDTO;
import com.sstu.community.mapper.UserMapper;
import com.sstu.community.model.User;
import com.sstu.community.service.NotificationService;
import com.sstu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class ProfileController {


    @Autowired
    private QuestionService questionService;
    @Autowired
    private NotificationService notificationService;


    @GetMapping("/profile/{action}")
    public String profile(
            HttpServletRequest request,
            @PathVariable("action") String action,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "size",defaultValue = "5") Integer size,
            Model model
    ){
        User user = (User) request.getSession().getAttribute("user");

        if(user == null)
            return "redirect:/";

        if("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            PaginationDTO<QuestionDTO> paginationDTO = questionService.list(user.getId(),page,size);
            model.addAttribute("pagination",paginationDTO);
        }else if("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
            PaginationDTO<NotificationDTO> paginationDTO = notificationService.list(user.getId(),page,size);
            model.addAttribute("pagination",paginationDTO);
        }
        return "profile";
    }
}
