package com.bersyte.eventz.features.events.application.dtos;

import com.bersyte.eventz.common.domain.Pagination;

import java.time.LocalDateTime;

public record EventSearchFilter(
        String title,
        String location,
//        String category,
        LocalDateTime startDate,
        LocalDateTime endDate,
        Pagination pagination
) {
}
