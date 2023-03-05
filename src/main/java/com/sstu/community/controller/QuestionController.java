package com.sstu.community.controller;


import com.sstu.community.dto.CommentDTO;
import com.sstu.community.dto.QuestionDTO;
import com.sstu.community.enums.CommentTypeEnum;
import com.sstu.community.model.Question;
import com.sstu.community.service.CommentService;
import com.sstu.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;
    @Autowired
    private CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(
            @PathVariable("id") Long id,
            Model model
    ){
        QuestionDTO questionDTO = questionService.getById(id);
        questionService.incView(id);
        List<CommentDTO> commentDTOS = commentService.getCommentsByParentId(id, CommentTypeEnum.QUESTION);
        Question question = new Question();
        question.setId(id);
        question.setTag(questionDTO.getTag());
        List<Question> questions = questionService.selectRelated(question);
        model.addAttribute("question",questionDTO);
        model.addAttribute("comments",commentDTOS);
        model.addAttribute("questionRelated",questions);
        return "question";
    }
}
