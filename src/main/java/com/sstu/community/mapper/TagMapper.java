package com.sstu.community.mapper;

import com.sstu.community.model.Tag;
import com.sstu.community.model.TagExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

public interface TagMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 05 14:45:07 CST 2023
     */
    long countByExample(TagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 05 14:45:07 CST 2023
     */
    int deleteByExample(TagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 05 14:45:07 CST 2023
     */
    int deleteByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 05 14:45:07 CST 2023
     */
    int insert(Tag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 05 14:45:07 CST 2023
     */
    int insertSelective(Tag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 05 14:45:07 CST 2023
     */
    List<Tag> selectByExampleWithRowbounds(TagExample example, RowBounds rowBounds);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 05 14:45:07 CST 2023
     */
    List<Tag> selectByExample(TagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 05 14:45:07 CST 2023
     */
    Tag selectByPrimaryKey(Integer id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 05 14:45:07 CST 2023
     */
    int updateByExampleSelective(@Param("record") Tag record, @Param("example") TagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 05 14:45:07 CST 2023
     */
    int updateByExample(@Param("record") Tag record, @Param("example") TagExample example);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 05 14:45:07 CST 2023
     */
    int updateByPrimaryKeySelective(Tag record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tag
     *
     * @mbg.generated Sun Mar 05 14:45:07 CST 2023
     */
    int updateByPrimaryKey(Tag record);
}