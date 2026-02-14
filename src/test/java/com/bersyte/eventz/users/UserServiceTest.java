package com.bersyte.eventz.users;

import com.bersyte.eventz.features.users.*;
import com.bersyte.eventz.features.users.application.dtos.UpdateUserRequest;
import com.bersyte.eventz.features.users.application.dtos.UserResponse;
import com.bersyte.eventz.features.users.domain.model.UserRole;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.users.infrastructure.persistence.mappers.UserEntityMapper;
import com.bersyte.eventz.features.users.infrastructure.persistence.repositories.UserJpaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserJpaRepository userJpaRepository;

    @Mock
    private UserCommonService userCommonService;

    @Mock
    private UserEntityMapper userMapper;


    @Test
    void shouldGetAllUsersSuccessfully() {

        // Arrange
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of (page, size);
        UserEntity user1 = getUser (
                23L,
                "bernardo@gmail.com",
                "232425",
                "Bernardo"
        );

        UserEntity user2 = getUser (
                3L,
                "isaias@gmail.com",
                "132825",
                "Isaias"
        );

        UserEntity user3 = getUser (
                5L,
                "isa@gmail.com",
                "932820",
                "Isaiah"
        );

        List<UserEntity> users = List.of (user1, user2, user3);
        Page<UserEntity> usersPage = new PageImpl<> (users);

        //Mock
        Mockito.when (
                userMapper.toUserResponse(any ())
        ).thenReturn (
                new UserResponse(
                        2L,
                        "isaias@gmail.com",
                        "fullName",
                        "lastName",
                        "+244989647474",
                        new Date (),
                        UserRole.USER
                )
        );

        Mockito.when (userJpaRepository.findAll (pageable))
                .thenReturn (usersPage);


        //When
        List<UserResponse> result = userService.getAllUsers (page, size);

        //Act
        assertEquals (users.size (), result.size ());
        verify (userJpaRepository, times (1))
                .findAll (pageable);

        //o mapper will be called n time (result.size)
        //will map all items in result
        verify (userMapper, times (result.size ()))
                .toUserResponse(any ());
    }


    @Test
    void shouldGetCurrentUserSuccessfully() {
        //Arrange
        String email = "isa@gmail.com";
        UserEntity currentUser = getUser (
                5L,
                email,
                "932820",
                "Isaiah"
        );

        //Mock calls
        Mockito.when (userCommonService.getUserByEmail (email))
                .thenReturn (currentUser);

        Mockito.when (userMapper.toUserResponse(currentUser))
                .thenReturn (
                        new UserResponse(
                                5L,
                                email,
                                "Isaiah",
                                "Isaiah",
                                "+244989647474",
                                new Date (),
                                UserRole.USER
                        )
                );

        //When
        UserResponse result = userService.getCurrentUser (email);

        //Act - Assert
        assertEquals (currentUser.getId (), result.id ());
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        //Arrange
        String email = "isa@gmail.com";
        UserEntity oldUser = getUser (
                5L,
                email,
                "932820",
                "Isaiah"
        );

        UpdateUserRequest dto = new UpdateUserRequest(
                "Jonas",
                "Brothers",
                "+359989647474"
        );

        UserEntity updatedUser = getUser (
                oldUser.getId (),
                email,
                "932820",
                dto.firstName () + " " + dto.lastName ()
        );

        //Mock calls
        Mockito.when (userCommonService.getUserByEmail (email))
                .thenReturn (oldUser);

        Mockito.when (userJpaRepository.save (oldUser))
                .thenReturn (updatedUser);

        Mockito.when (userMapper.toUserResponse(updatedUser))
                .thenReturn (
                        new UserResponse(
                                oldUser.getId (),
                                oldUser.getEmail (),
                                "Jonas",
                                "Brothers",
                                "+359989647474",
                                oldUser.getCreatedAt (),
                                oldUser.getRole ()
                        )
                );

        //When
        UserResponse response = userService.updateUser (
                dto, email
        );
        //Assert
        assertEquals (updatedUser.getId (), response.id ());
    }

    private UserEntity getUser(
            Long userId,
            String email,
            String password,
            String name
    ) {
        return new UserEntity(
                userId,
                email,
                password,
                name,
                name,
                "+244989647474",
                new Date (),
                UserRole.USER,
                List.of (),
                List.of (),
                "123456",
                LocalDateTime.now (),
                true
        );
    }
}