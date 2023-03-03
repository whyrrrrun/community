package com.sstu.community.service;


import com.sstu.community.dto.PaginationDTO;
import com.sstu.community.dto.QuestionDTO;
import com.sstu.community.mapper.QuestionMapper;
import com.sstu.community.mapper.UserMapper;
import com.sstu.community.model.Question;
import com.sstu.community.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionMapper questionMapper;
    @Autowired
    private UserMapper userMapper;


    public PaginationDTO list(Integer page, Integer size) {

        if(page < 1)
            page = 1;
        Integer offset = size*(page-1);
        if(offset > questionMapper.count())
            offset = questionMapper.count() - (questionMapper.count() % size);
        List<Question> questionList = questionMapper.list(offset,size);
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : questionList) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOS);
        paginationDTO.setPagination(page,size,questionMapper.count());
        return paginationDTO;
    }

    public PaginationDTO list(Integer id, Integer page, Integer size) {

        Integer idCount = questionMapper.countById(id);

        if(page < 1)
            page = 1;
        Integer offset = size*(page - 1);
        if(offset > idCount)
            offset = idCount - (idCount % size);
        List<Question> questionList = questionMapper.listById(id,offset,size);
        List<QuestionDTO> questionDTOS = new ArrayList<>();
        PaginationDTO paginationDTO = new PaginationDTO();
        for (Question question : questionList) {
            User user = userMapper.findById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(question,questionDTO);
            questionDTO.setUser(user);
            questionDTOS.add(questionDTO);
        }
        paginationDTO.setQuestions(questionDTOS);
        paginationDTO.setPagination(page,size,idCount);
        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        Question question = questionMapper.findById(id);
        QuestionDTO questionDTO = new QuestionDTO();
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.findById(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }

    public void createOrUpdate(Question question) {
        if(question.getId() == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            questionMapper.create(question);
        }else{
            question.setGmtModified(System.currentTimeMillis());
            questionMapper.update(question);
        }
    }
}
