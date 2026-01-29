package com.bersyte.eventz.features.users.application.mappers;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.features.users.application.dtos.CreateUserRequest;
import com.bersyte.eventz.features.users.application.dtos.UserResponse;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import java.util.List;

public class UserMapper {
    
    
    public PagedResult<UserResponse> toPagedResponse(PagedResult<AppUser> pagedResult){
        List<UserResponse> usersResponseList = pagedResult.data().stream().map(
                this::toResponse
        ).toList();
        return new PagedResult<>(
                usersResponseList,
                pagedResult.totalElements(),
                pagedResult.totalPages(), pagedResult.isLast()
        );
    }
    
    public AppUser toDomain(String userId, CreateUserRequest request){
          return AppUser.create(
                userId,
                request.email(),
                request.firstName(),
                request.lastName(),
                request.phone()
         );
    }
    
    public UserResponse toResponse(AppUser user){
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getPhone(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }

}
