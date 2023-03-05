package com.sstu.community.dto;

import com.sstu.community.model.Tag;
import lombok.Data;

import java.util.List;

@Data
public class TagDTO {
    private String categoryName;
    private List<Tag> tags;
}
