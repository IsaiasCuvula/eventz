package com.bersyte.eventz.features.users.application.usecases;

import com.bersyte.eventz.common.application.usecases.UseCase;
import com.bersyte.eventz.common.domain.IdGenerator;
import com.bersyte.eventz.features.users.application.dtos.CreateUserRequest;
import com.bersyte.eventz.features.users.application.dtos.UserResponse;
import com.bersyte.eventz.features.users.application.mappers.UserMapper;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.repository.UserRepository;

public class SaveUserUseCase implements UseCase<CreateUserRequest, UserResponse> {
    private final UserRepository repository;
    private final UserMapper userMapper;
    private final IdGenerator idGenerator;
    
    public SaveUserUseCase(UserRepository repository, UserMapper userMapper, IdGenerator idGenerator) {
        this.repository = repository;
        this.userMapper = userMapper;
        this.idGenerator = idGenerator;
    }
    
    @Override
    public UserResponse execute(CreateUserRequest request) {
        String userId = idGenerator.generateUuid();
        AppUser user = userMapper.toDomain(userId, request);
        AppUser savedUser = repository.save(user);
        return userMapper.toResponse(savedUser);
    }
}
