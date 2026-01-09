package com.forum.forum;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

public class PageTestData {

    public static final Integer PAGE_NUMBER = 0;
    public static final Integer PAGE_SIZE = 10;

    public static final Pageable PAGE = PageRequest.of(PAGE_NUMBER, PAGE_SIZE);

}
