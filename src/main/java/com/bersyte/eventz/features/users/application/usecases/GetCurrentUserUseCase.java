package com.bersyte.eventz.features.users.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.features.users.application.dtos.UserResponse;
import com.bersyte.eventz.features.users.application.mappers.UserMapper;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

public class GetCurrentUserUseCase implements UseCase<String, UserResponse> {
    private final UserMapper userMapper;
    private final UserValidationService validationService;
    
    public GetCurrentUserUseCase(UserMapper userRepository, UserValidationService validationService) {
        this.userMapper = userRepository;
        this.validationService = validationService;
    }
    
    @Override
    public UserResponse execute(String requesterEmail) {
        AppUser currentUser = validationService.getRequester(requesterEmail);
        return userMapper.toResponse(currentUser);
    }
}
