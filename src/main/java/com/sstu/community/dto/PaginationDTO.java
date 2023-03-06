package com.sstu.community.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class PaginationDTO<T> {
    private List<T> data;
    private boolean showPrevious;
    private boolean showFirstPage;
    private boolean showNext;
    private boolean showEndPage;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage;

    public void setPagination(Integer page,Integer size,Integer totalCount){

        if(0 == totalCount){
            showEndPage = false;
            showFirstPage = false;
            showNext = false;
            showPrevious = false;
            totalPage = 0;
            return;
        }

        if(totalCount % size == 0){
            totalPage = totalCount / size;
        }else {
            totalPage = totalCount / size + 1;
        }

        if(page < 1)
            page = 1;
        if(page > totalPage)
            page = totalPage;
        this.page = page;

        int pagep = page - 3;
        while (true){
            if(pagep == page + 4 || pagep > totalPage)
                break;
            if(pagep > 0)
                pages.add(pagep);
            pagep++;
        }
        if(page == 1){
            showPrevious = false;
        }else{
            showPrevious = true;
        }
        if(page == totalPage){
            showNext = false;
        }else{
            showNext = true;
        }
        if(pages.contains(1)){
            showFirstPage = false;
        }else{
            showFirstPage = true;
        }
        if(pages.contains(totalPage)){
            showEndPage = false;
        }else {
            showEndPage = true;
        }
    }
}
