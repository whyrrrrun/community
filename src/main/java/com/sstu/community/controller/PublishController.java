package com.sstu.community.controller;

import com.sstu.community.dto.QuestionDTO;
import com.sstu.community.model.Question;
import com.sstu.community.model.User;
import com.sstu.community.service.QuestionService;
import com.sstu.community.service.TagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private TagService tagService;


    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags",tagService.get());
        return "publish";
    }


    @GetMapping("/publish/{id}")
    public String modifiedQuestion(
            @PathVariable("id") Long id,
            Model model
    ){
        QuestionDTO question = questionService.getById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        model.addAttribute("id",question.getId());
        return "publish";
    }




    @PostMapping("/publish")
    public String dopublish(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("tag") String tag,
            @RequestParam("id") Long id,
            HttpServletRequest request,
            Model model
    ){

        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags",tagService.get());

        User user = (User) request.getSession().getAttribute("user");

        if(user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }


        if(title == null || title == ""){
            model.addAttribute("error","问题标题不能为空");
            return "publish";
        }
        if(description == null || description == ""){
            model.addAttribute("error","问题描述不能为空");
            return "publish";
        }
        if(tag == null || tag == ""){
            model.addAttribute("error","问题标签不能为空");
            return "publish";
        }

        String filterValid = tagService.filterValid(tag);
        if(!  StringUtils.isEmpty(filterValid)){
            model.addAttribute("error","输入非法标签：" + filterValid);
            return "publish";
        }

        Question question = new Question();
        question.setDescription(description);
        question.setTag(tag);
        question.setTitle(title);
        question.setCreator(user.getId());
        question.setId(id);
        question.setCommentCount(0);
        question.setViewCount(0);
        question.setLikeCount(0);
        questionService.createOrUpdate(question);
        return "redirect:/";
    }

}
