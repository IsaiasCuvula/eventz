package com.bersyte.eventz.features.users.application.dtos;

import com.bersyte.eventz.common.domain.Pagination;

public record FetchUsersRequest(
        String requesterEmail,
        Pagination pagination
) {
}
