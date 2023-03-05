package com.sstu.community.controller;

import com.sstu.community.dto.CommentCreateDTO;
import com.sstu.community.dto.CommentDTO;
import com.sstu.community.dto.ResultDTO;
import com.sstu.community.enums.CommentTypeEnum;
import com.sstu.community.exception.CustomizeErrorCode;
import com.sstu.community.exception.CustomizeException;
import com.sstu.community.model.Comment;
import com.sstu.community.model.User;
import com.sstu.community.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class CommentController {

    @Autowired
    private CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    public Object post(
            @RequestBody CommentCreateDTO commentCreateDTO,
            HttpServletRequest request
    ){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null)
            throw new CustomizeException(CustomizeErrorCode.NO_LOGIN);
        if(StringUtils.isEmpty(commentCreateDTO.getContent()) || commentCreateDTO == null)
            throw new CustomizeException(CustomizeErrorCode.COMMENT_CONTENT_NULL);
        Comment comment = new Comment();
        comment.setContent(commentCreateDTO.getContent());
        comment.setType(commentCreateDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(comment.getGmtCreate());
        comment.setParentId(commentCreateDTO.getParentId());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0);
        comment.setCommentCount(0);
        commentService.insert(comment);
        return ResultDTO.okOf();
    }

    @ResponseBody
    @RequestMapping(value = "/comment/{id}", method = RequestMethod.GET)
    public ResultDTO<List<CommentDTO>> comment(@PathVariable("id") Long id){
        List<CommentDTO> comments = commentService.getCommentsByParentId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(comments);
    }

}
