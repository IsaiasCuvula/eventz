package com.bersyte.eventz.common.domain;

public record Pagination(
        int page, int size
) {
    public Pagination {
        if(page < 0) page = 0;
        if(size <= 0) size = 10;
        if(size > 100) size = 100;
    }
}
