package com.bersyte.eventz.features.users.application.dtos;

import com.bersyte.eventz.common.domain.dtos.Pagination;

public record FetchUsersRequest(
        String requesterId,
        Pagination pagination
) {
}
