package com.bersyte.eventz.features.users.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.users.application.dtos.GetUserByIdInput;
import com.bersyte.eventz.features.users.application.dtos.UserResponse;
import com.bersyte.eventz.features.users.application.mappers.UserMapper;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

public class GetUserByIdUseCase implements UseCase<GetUserByIdInput, UserResponse> {
    private final UserValidationService validationService;
    private final UserMapper userMapper;
    
    public GetUserByIdUseCase(UserValidationService validationService, UserMapper userMapper) {
        this.validationService = validationService;
        this.userMapper = userMapper;
    }
    
    @Override
    public UserResponse execute(GetUserByIdInput input) {
        String targetId = input.targetId();
        String requesterEmail = input.requesterEmail();
        AppUser requester = validationService.getRequester(requesterEmail);
        
        if(!requester.getId().equals(targetId)){
            return userMapper.toResponse(requester);
        }
        
        if(!requester.canSeeUserDetails(targetId)){
            throw new UnauthorizedException("You don't have permission");
        }
        AppUser targetUser= validationService.getValidUserById(targetId);
        return userMapper.toResponse(targetUser);
    }
}
