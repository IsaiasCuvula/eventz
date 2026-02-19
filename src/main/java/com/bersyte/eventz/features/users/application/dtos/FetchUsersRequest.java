package com.bersyte.eventz.features.users.application.dtos;

import com.bersyte.eventz.common.domain.dtos.Pagination;

import java.util.UUID;

public record FetchUsersRequest(
        UUID requesterId,
        Pagination pagination
) {
}
