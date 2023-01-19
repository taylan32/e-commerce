package com.example.ecommerce.utils;


import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.util.List;

@Data
public class BasePageableModel<T> {

    private int number;
    private int size;
    private Sort sort;
    private int totalPages;
    private long totalElements;
    private List<T> content;

    public BasePageableModel(Page page, List<T> list) {
        this.number = page.getNumber();
        this.size = page.getSize();
        this.sort = page.getSort();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.content = list;

    }

}
