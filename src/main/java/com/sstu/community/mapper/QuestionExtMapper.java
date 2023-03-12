package com.sstu.community.mapper;

import com.sstu.community.dto.QuestionDTO;
import com.sstu.community.dto.QuestionQueryDTO;
import com.sstu.community.model.Question;
import com.sstu.community.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    int incView(Question record);
    int incComment(Question record);
    List<Question> selectRelated(Question record);
    Integer countBySearch(QuestionQueryDTO questionQueryDTO);
    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}