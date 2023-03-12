package com.sstu.community.service;


import com.sstu.community.dto.PaginationDTO;
import com.sstu.community.dto.QuestionDTO;
import com.sstu.community.dto.QuestionQueryDTO;
import com.sstu.community.exception.CustomizeErrorCode;
import com.sstu.community.exception.CustomizeException;
import com.sstu.community.mapper.QuestionExtMapper;
import com.sstu.community.mapper.QuestionMapper;
import com.sstu.community.mapper.UserMapper;
import com.sstu.community.model.Question;
import com.sstu.community.model.QuestionExample;
import com.sstu.community.model.User;
import com.sun.deploy.security.SelectableSecurityManager;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private QuestionExtMapper questionExtMapper;


    public PaginationDTO<QuestionDTO> list(String search, Integer page, Integer size) {

        if (!StringUtils.isEmpty(search)) {
            String[] tags;
            if(StringUtils.split(search, " ") == null){
                tags = new String[1];
                tags[0] = search;
            }else
                tags = StringUtils.split(search, " ");
            search = Arrays.stream(tags).collect(Collectors.joining("|"));
        }

        if(search == "")
            search = null;
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);

        Integer totalCount = questionExtMapper.countBySearch(questionQueryDTO);



        if(page < 1)
            page = 1;
        Integer offset = size * (page - 1);
        if(offset > totalCount)
            offset = totalCount - (totalCount % size);

        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        List<Question> questions = questionExtMapper.selectBySearch(questionQueryDTO);

        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }

        if(questionDTOS.size() == 0)
            return paginationDTO;
        for (QuestionDTO questionDTO : questionDTOS) {
            if(questionDTO.getDescription().length()>15){
                String substring = questionDTO.getDescription().substring(0, 15).concat("........");
                questionDTO.setDescription(substring);
            }
        }
        paginationDTO.setData(questionDTOS);
        paginationDTO.setPagination(page,size,totalCount);
        return paginationDTO;
    }

    public PaginationDTO<QuestionDTO> list(Long id, Integer page, Integer size) {

        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(id);
        Integer idCount = (int) questionMapper.countByExample(questionExample);

        if(page < 1)
            page = 1;
        Integer offset = size*(page - 1);
        if(offset > idCount)
            offset = idCount - (idCount % size);

        PaginationDTO<QuestionDTO> paginationDTO = new PaginationDTO<>();
        questionExample.setOrderByClause("gmt_modified desc");
        List<QuestionDTO> questionDTOS = getQuestionDTO(size, questionExample, offset);
        if(questionDTOS.size() == 0)
            return paginationDTO;
        paginationDTO.setData(questionDTOS);
        paginationDTO.setPagination(page,size,idCount);
        return paginationDTO;
    }

    private List<QuestionDTO> getQuestionDTO(Integer size, QuestionExample questionExample, Integer offset) {
        List<Question> questionList = questionMapper.selectByExampleWithRowbounds(questionExample,new RowBounds(offset,size));
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        for (Question question : questionList) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        return questionDTOS;
    }

    public QuestionDTO getById(Long id) {
        Question question = questionMapper.selectByPrimaryKey(id);
        if(question == null){
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.insert(question);
        }else{

            Question record = new Question();
            record.setGmtModified(System.currentTimeMillis());
            record.setTitle(question.getTitle());
            record.setDescription(question.getDescription());
            record.setTag(question.getTag());
            QuestionExample example = new QuestionExample();
            example.createCriteria().andIdEqualTo(question.getId());
            int i = questionMapper.updateByExampleSelective(record, example);
            if(i != 1){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    public void incView(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    public List<Question> selectRelated(Question query) {
        if(StringUtils.isEmpty(query.getTag())){
            return new ArrayList<>();
        }
        String[] tags;
        if(StringUtils.split(query.getTag(), ",") == null){
            tags = new String[1];
            tags[0] = query.getTag();
        }
        else
            tags = StringUtils.split(query.getTag(), ",");
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        Question question = new Question();
        question.setId(query.getId());
        question.setTag(regexpTag);

        return questionExtMapper.selectRelated(question);
    }
}
