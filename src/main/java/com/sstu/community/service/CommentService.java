package com.sstu.community.service;


import com.sstu.community.dto.CommentDTO;
import com.sstu.community.enums.CommentTypeEnum;
import com.sstu.community.enums.NotificationStatusEnum;
import com.sstu.community.enums.NotificationTypeEnum;
import com.sstu.community.exception.CustomizeErrorCode;
import com.sstu.community.exception.CustomizeException;
import com.sstu.community.mapper.*;
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
    @Autowired
    private CommentExtMapper commentExtMapper;
    @Autowired
    private NotificationMapper notificationMapper;


    @Transactional
    public void insert(Comment comment, Long id) {

        if(comment.getParentId() == null || comment.getParentId() == 0)
            throw new CustomizeException(CustomizeErrorCode.TARGET_NOT_FOUND);
        if(comment.getType() == null || !CommentTypeEnum.isExist(comment.getType()))
            throw new CustomizeException(CustomizeErrorCode.COMMENT_TYPE_ERROR);

        if(comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment Pcomment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if(Pcomment == null)
                throw new CustomizeException(CustomizeErrorCode.COMMENTC_NOT_FOUND);
            //新增通知
            commentMapper.insert(comment);
            //增加通知评论数
            Comment comment1 = new Comment();
            comment1.setId(comment.getParentId());
            comment1.setCommentCount(1);
            commentExtMapper.incCommentCount(comment1);

            createNotification(id, comment, Pcomment.getCommentator(), NotificationTypeEnum.REPLY_COMMENT);

        }else if(comment.getType() == CommentTypeEnum.QUESTION.getType()){
            //回复问题
            Question Pquestion = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(Pquestion == null)
                throw new CustomizeException(CustomizeErrorCode.QUESTIONC_NOT_FOUND);
            //新增评论
            commentMapper.insert(comment);
            //增加问题评论数
            Question question = new Question();
            question.setId(comment.getParentId());
            question.setCommentCount(1);
            questionExtMapper.incComment(question);
            //创建通知
            createNotification(id, comment, Pquestion.getCreator(), NotificationTypeEnum.REPLY_QUESTION);

        }

    }


    /*
    * 创建通知方法
    * */
    private void createNotification(Long id, Comment comment, Long notifier, NotificationTypeEnum notificationTypeEnum) {
        Notification notification = new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationTypeEnum.getStatus());
        notification.setOuterId(comment.getParentId());
        notification.setNotifier(notifier);
        notification.setReceiver(id);
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        notificationMapper.insert(notification);
    }

    /*
    * 通过被评论对象获取其下所有评论
    * */

    public List<CommentDTO> getCommentsByParentId(Long id, CommentTypeEnum commentTypeEnum) {
        CommentExample example = new CommentExample();
        example.createCriteria()
                .andTypeEqualTo(commentTypeEnum.getType())
                .andParentIdEqualTo(id);
        example.setOrderByClause("gmt_create desc");
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
