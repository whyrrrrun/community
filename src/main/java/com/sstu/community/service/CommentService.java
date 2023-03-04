package com.sstu.community.service;


import com.sstu.community.dto.CommentDTO;
import com.sstu.community.enums.CommentTypeEnum;
import com.sstu.community.exception.CustomizeErrorCode;
import com.sstu.community.exception.CustomizeException;
import com.sstu.community.mapper.CommentMapper;
import com.sstu.community.mapper.QuestionExtMapper;
import com.sstu.community.mapper.QuestionMapper;
import com.sstu.community.mapper.UserMapper;
import com.sstu.community.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;
    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;
    @Autowired
    private UserMapper userMapper;



    @Transactional
    public void insert(Comment comment) {

        if(comment.getParentId() == null || comment.getParentId() == 0)
            throw new CustomizeException(CustomizeErrorCode.TARGET_NOT_FOUND);
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType()))
            throw new CustomizeException(CustomizeErrorCode.COMMENT_TYPE_ERROR);

        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment Pcomment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(Pcomment == null)
                throw new CustomizeException(CustomizeErrorCode.COMMENTC_NOT_FOUND);
            commentMapper.insert(comment);
        }else if(comment.getType() == CommentTypeEnum.QUESTION.getType()){
            Question Pquestion = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(Pquestion == null)
                throw new CustomizeException(CustomizeErrorCode.QUESTIONC_NOT_FOUND);
            commentMapper.insert(comment);
            Pquestion.setCommentCount(1);
            questionExtMapper.incComment(Pquestion);
        }

    }

    public List<CommentDTO> getByQuestion(Long id) {
        CommentExample example = new CommentExample();
        example.createCriteria().andTypeEqualTo(CommentTypeEnum.QUESTION.getType()).andParentIdEqualTo(id);
        List<Comment> comments = commentMapper.selectByExample(example);
        if(comments.size() == 0)
            return new ArrayList<>();
        //获取不重复的评论者
        List<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toList());

        //获取评论者的id键值对
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdIn(commentators);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> collect = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //组装成List<CommentDTO>
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            commentDTO.setUser(collect.get(commentDTO.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;


    }
}
