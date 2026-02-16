package com.bersyte.eventz.common.domain.dtos;

import java.util.List;

public record PagedResult<T>(
        List<T> data,
        long totalElements,
        int totalPages,
        boolean isLast
) {
}
