package com.bersyte.eventz.features.events.application.dtos;

import com.bersyte.eventz.common.domain.dtos.Pagination;

import java.time.LocalDateTime;

public record EventByDateInput(
        Pagination pagination, LocalDateTime startDate,
        LocalDateTime endDate
) {
}
