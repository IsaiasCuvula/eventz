package com.bersyte.eventz.features.users.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.users.application.dtos.FetchUsersRequest;
import com.bersyte.eventz.features.users.application.dtos.UserResponse;
import com.bersyte.eventz.features.users.application.mappers.UserMapper;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.repository.UserRepository;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

public class FetchUsersUseCase implements UseCase<FetchUsersRequest, PagedResult<UserResponse>> {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidationService validationService;
    public FetchUsersUseCase(UserRepository userRepository, UserMapper userMapper, UserValidationService validationService) {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.validationService = validationService;
    }
    
    
    @Override
    public PagedResult<UserResponse> execute(FetchUsersRequest request) {
        String requesterId = request.requesterId();
        AppUser requester = validationService.getRequesterById(requesterId);
        if (!requester.isAdmin()){
            throw new UnauthorizedException("You don't have permission");
        }
        PagedResult<AppUser> pagedResult = userRepository.fetchUsers(request.pagination());
        return userMapper.toPagedResponse(pagedResult);
    }
}
