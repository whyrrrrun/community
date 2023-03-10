package com.sstu.community.controller;


import com.sstu.community.dto.PaginationDTO;
import com.sstu.community.dto.QuestionDTO;
import com.sstu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping("/")
    public String Index(
            HttpServletRequest request,
            Model model,
            @RequestParam(value = "page",defaultValue = "1") Integer page,
            @RequestParam(value = "size",defaultValue = "5") Integer size,
            @RequestParam(name = "search", required = false) String search
    ){
       // User user = (User) request.getSession().getAttribute("user");

        PaginationDTO<QuestionDTO> paginationDTO = questionService.list(search,page,size);
        model.addAttribute("pagination",paginationDTO);
        model.addAttribute("search",search);
        return "index";
    }
}
