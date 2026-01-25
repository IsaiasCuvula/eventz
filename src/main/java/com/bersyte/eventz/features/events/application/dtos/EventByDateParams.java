package com.bersyte.eventz.features.events.application.dtos;

import com.bersyte.eventz.common.domain.Pagination;
import java.util.Date;

public record EventByDateParams(
        Pagination pagination, Date date
) {
}
