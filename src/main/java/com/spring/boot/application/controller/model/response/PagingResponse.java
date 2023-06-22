package com.spring.boot.application.controller.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagingResponse {
    private List<?> content;
    private long totalElements;
    private long numberOfElements;
    private int pageSize;
    private int pageNumber;
    private long totalPages;

    public PagingResponse(Page<?> page, List<?> content) {
        this.content = content;
        this.totalElements = page.getTotalElements();
        this.numberOfElements = page.getNumberOfElements();
        this.pageSize = page.getSize();
        this.pageNumber = page.getNumber() + 1;
        this.totalPages = page.getTotalPages();
    }

    public PagingResponse(Page<?> page) {
        this.content = page.getContent();
        this.totalElements = page.getTotalElements();
        this.numberOfElements = page.getNumberOfElements();
        this.pageSize = page.getSize();
        this.pageNumber = page.getNumber() + 1;
        this.totalPages = page.getTotalPages();
    }
}
