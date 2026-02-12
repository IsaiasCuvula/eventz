package com.bersyte.eventz.features.users.application.usecases;

import com.bersyte.eventz.common.application.usecases.VoidUseCase;
import com.bersyte.eventz.common.domain.exceptions.UnauthorizedException;
import com.bersyte.eventz.features.users.application.dtos.DeleteUserRequest;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.repository.UserRepository;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;

public class DeleteUserUseCase implements VoidUseCase<DeleteUserRequest> {
    private final UserRepository repository;
    private final UserValidationService validationService;
    
    public DeleteUserUseCase(UserRepository repository, UserValidationService validationService) {
        this.repository = repository;
        this.validationService = validationService;
    }
    
    @Override
    public void execute(DeleteUserRequest request) {
        String requesterId = request.requesterId();
        String targetId = request.targetUserId();
        AppUser requester = validationService.getRequesterById(requesterId);
        if(!requester.canDeleteOrUpdateUser(targetId)){
            throw new UnauthorizedException("You don't have permission to perform this operation");
        }
        AppUser targetUser = validationService.getValidUserById(targetId);
        repository.delete(targetUser.getId());
    }
}
