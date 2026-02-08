package com.bersyte.eventz.features.users.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.users.application.dtos.UpdateUserInput;
import com.bersyte.eventz.features.users.application.dtos.UpdateUserRequest;
import com.bersyte.eventz.features.users.application.dtos.UserResponse;
import com.bersyte.eventz.features.users.application.mappers.UserMapper;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.repository.UserRepository;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

public class UpdateUserUseCase implements UseCase<UpdateUserInput, UserResponse> {
    private final UserRepository repository;
    private final UserValidationService validationService;
    private final UserMapper mapper;
    
    public UpdateUserUseCase(UserRepository repository,UserMapper mapper,  UserValidationService validationService) {
        this.repository = repository;
        this.validationService = validationService;
        this.mapper = mapper;
    }
    
    @Override
    public UserResponse execute(UpdateUserInput input) {
        String targetId= input.targetUserId();
        String requestId = input.requesterId();
        UpdateUserRequest request = input.request();
        AppUser requester =  validationService.getRequesterById(requestId);
        
        if (!requester.canDeleteOrUpdateUser(targetId)){
            throw  new UnauthorizedException("You don't have permission");
        }
        AppUser targetUser =  validationService.getValidUserById(targetId);
        
        String firstName = request.firstName();
        String lastName = request.lastName();
        String phone =request.phone();
        
        if (firstName != null) {
            targetUser.setFirstName(firstName);
        }
        if (lastName != null) {
            targetUser.setLastName(lastName);
        }
        if (phone != null) {
            targetUser.setPhone(phone);
        }
        AppUser savedUser = repository.update(targetUser);
        return mapper.toResponse(savedUser);
    }
}
