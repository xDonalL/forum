package com.forum.forum;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class PageTestData {

    public static final Integer PAGE_NUMBER = 0;
    public static final Integer PAGE_SIZE = 10;

    public static final Pageable PAGE = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);
    public static final Pageable PAGE_DATE_DESC = PageRequest.of(PAGE_NUMBER, PAGE_SIZE,
            Sort.by(Sort.Order.desc("createdAt")));

}
