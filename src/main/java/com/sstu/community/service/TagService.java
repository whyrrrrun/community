package com.sstu.community.service;

import com.sstu.community.dto.TagDTO;
import com.sstu.community.mapper.TagMapper;
import com.sstu.community.model.Tag;
import com.sstu.community.model.TagExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TagService {

    @Autowired
    private TagMapper tagMapper;



    public List<TagDTO> get() {
        List<TagDTO> tagDTOS = new ArrayList<>();
        //开发语言
        TagDTO program = getByCategory("开发语言");
        tagDTOS.add(program);
        //开发框架
        TagDTO framework = getByCategory("开发框架");
        tagDTOS.add(framework);
        return tagDTOS;
    }

    public TagDTO getByCategory(String categoryName){
        TagDTO tagDTO = new TagDTO();
        TagExample tagExample = new TagExample();
        tagExample.createCriteria().andCategoryNameEqualTo(categoryName);
        List<Tag> tags = tagMapper.selectByExample(tagExample);
        tagDTO.setCategoryName(categoryName);
        tagDTO.setTags(tags);
        return tagDTO;
    }

    public String filterValid(String tags){
        String[] split;
        if(StringUtils.split(tags, ",") == null){
            split = new String[1];
            split[0] = tags;
        }else
        split = StringUtils.split(tags, ",");
        List<TagDTO> tagDTOS = get();
        List<Tag> collect = tagDTOS.stream().flatMap(tagDTO -> tagDTO.getTags().stream()).collect(Collectors.toList());
        List<String> tagList = collect.stream().map(tag -> tag.getTagName()).collect(Collectors.toList());
        String Invalid = Arrays.stream(split).filter(t -> !tagList.contains(t)).collect(Collectors.joining(","));
        return Invalid;


    }

}
