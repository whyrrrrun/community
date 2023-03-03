package com.sstu.community.mapper;

import com.sstu.community.dto.QuestionDTO;
import com.sstu.community.model.Question;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface QuestionMapper {


    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator,tag) values (#{title},#{description},#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    void create(Question question);

    @Select("select * from question limit #{offset}, #{size}")
    List<Question> list(@Param("offset") Integer offset,@Param("size") Integer size);

    @Select("select * from question where creator = #{id} limit #{offset}, #{size}")
    List<Question> listById(@Param("id") Integer id, @Param("offset") Integer offset, @Param("size") Integer size);

    @Select("select count(1) from question")
    int count();

    @Select("select count(1) from question where creator = #{id}")
    int countById(@Param("id") Integer id);

    @Select("select * from question where id = #{id}")
    Question findById(@Param("id") Integer id);

    @Update("update question set title = #{title} , description = #{description} , gmt_modified = #{gmtModified} , tag = #{tag} where id = #{id}")
    void update(Question question);
}
