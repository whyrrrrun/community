package com.sstu.community.mapper;

import com.sstu.community.model.Comment;

public interface CommentExtMapper {
    int incCommentCount(Comment record);
}