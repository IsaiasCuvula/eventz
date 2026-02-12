package com.bersyte.eventz.features.users.infrastructure.config;

import com.bersyte.eventz.common.domain.IdGenerator;
import com.bersyte.eventz.features.users.application.mappers.UserMapper;
import com.bersyte.eventz.features.users.application.usecases.*;
import com.bersyte.eventz.features.users.domain.repository.UserRepository;
import com.bersyte.eventz.features.users.domain.services.UserValidationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserConfig {
    
    @Bean
    UserValidationService userValidationService(UserRepository repository){
        return  new UserValidationService(repository);
    }
    
    @Bean
    UserMapper userMapper(){
        return new UserMapper();
    }
    
    @Bean
    FetchUsersUseCase fetchUsersUseCase(
            UserRepository repository,
            UserValidationService validationService,
            UserMapper mapper
    ){
        return new FetchUsersUseCase(repository,mapper,validationService);
    }
    @Bean
    SaveUserUseCase saveUserUseCase(
            UserRepository repository,
            UserMapper mapper,
            IdGenerator idGenerator
    ){
        return new SaveUserUseCase(repository, mapper, idGenerator) ;
    }
    @Bean
    UpdateUserUseCase updateUserUseCase(
            UserRepository repository,
            UserValidationService validationService,
            UserMapper mapper
    ){
        return new UpdateUserUseCase(repository, mapper, validationService);
    }
    @Bean
    DeleteUserUseCase deleteUserUseCase(
            UserRepository repository,
            UserValidationService validationService
    ){
        return new DeleteUserUseCase(repository, validationService);
    }
    
    @Bean
    GetCurrentUserUseCase getCurrentUserUseCase(
            UserValidationService validationService,
            UserMapper mapper
    ){
        return new GetCurrentUserUseCase(mapper,validationService);
    }
    
}
