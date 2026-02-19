package com.bersyte.eventz.features.users.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.features.users.application.dtos.UserResponse;
import com.bersyte.eventz.features.users.application.mappers.UserMapper;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

import java.util.UUID;

public class GetCurrentUserUseCase implements UseCase<UUID, UserResponse> {
    private final UserMapper userMapper;
    private final UserValidationService validationService;
    
    public GetCurrentUserUseCase(UserMapper userRepository, UserValidationService validationService) {
        this.userMapper = userRepository;
        this.validationService = validationService;
    }
    
    @Override
    public UserResponse execute(UUID requesterId) {
        AppUser currentUser = validationService.getRequesterById(requesterId);
        return userMapper.toResponse(currentUser);
    }
}
