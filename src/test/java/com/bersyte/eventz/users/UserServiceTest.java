package com.bersyte.eventz.users;

import com.bersyte.eventz.common.*;
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
    private UserRepository userRepository;

    @Mock
    private UserCommonService userCommonService;

    @Mock
    private UserMapper userMapper;


    @Test
    void shouldGetAllUsersSuccessfully() {

        // Arrange
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of (page, size);
        AppUser user1 = getUser (
                23L,
                "bernardo@gmail.com",
                "232425",
                "Bernardo"
        );

        AppUser user2 = getUser (
                3L,
                "isaias@gmail.com",
                "132825",
                "Isaias"
        );

        AppUser user3 = getUser (
                5L,
                "isa@gmail.com",
                "932820",
                "Isaiah"
        );

        List<AppUser> users = List.of (user1, user2, user3);
        Page<AppUser> usersPage = new PageImpl<> (users);

        //Mock
        Mockito.when (
                userMapper.toUserResponseDTO (any ())
        ).thenReturn (
                new UserResponseDto (
                        2L,
                        "isaias@gmail.com",
                        "firstName",
                        "lastName",
                        "+244989647474",
                        new Date (),
                        UserRole.USER
                )
        );

        Mockito.when (userRepository.findAll (pageable))
                .thenReturn (usersPage);


        //When
        List<UserResponseDto> result = userService.getAllUsers (page, size);

        //Act
        assertEquals (users.size (), result.size ());
        verify (userRepository, times (1))
                .findAll (pageable);

        //o mapper will be called n time (result.size)
        //will map all items in result
        verify (userMapper, times (result.size ()))
                .toUserResponseDTO (any ());
    }


    @Test
    void shouldGetCurrentUserSuccessfully() {
        //Arrange
        String email = "isa@gmail.com";
        AppUser currentUser = getUser (
                5L,
                email,
                "932820",
                "Isaiah"
        );

        //Mock calls
        Mockito.when (userCommonService.getUserByEmail (email))
                .thenReturn (currentUser);

        Mockito.when (userMapper.toUserResponseDTO (currentUser))
                .thenReturn (
                        new UserResponseDto (
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
        UserResponseDto result = userService.getCurrentUser (email);

        //Act - Assert
        assertEquals (currentUser.getId (), result.id ());
    }

    @Test
    void shouldUpdateUserSuccessfully() {
        //Arrange
        String email = "isa@gmail.com";
        AppUser oldUser = getUser (
                5L,
                email,
                "932820",
                "Isaiah"
        );

        UpdateUserRequestDto dto = new UpdateUserRequestDto (
                "Jonas",
                "Brothers",
                "+359989647474"
        );

        AppUser updatedUser = getUser (
                oldUser.getId (),
                email,
                "932820",
                dto.firstName () + " " + dto.lastName ()
        );

        //Mock calls
        Mockito.when (userCommonService.getUserByEmail (email))
                .thenReturn (oldUser);

        Mockito.when (userRepository.save (oldUser))
                .thenReturn (updatedUser);

        Mockito.when (userMapper.toUserResponseDTO (updatedUser))
                .thenReturn (
                        new UserResponseDto (
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
        UserResponseDto response = userService.updateUser (
                dto, email
        );
        //Assert
        assertEquals (updatedUser.getId (), response.id ());
    }

    private AppUser getUser(
            Long userId,
            String email,
            String password,
            String name
    ) {
        return new AppUser (
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