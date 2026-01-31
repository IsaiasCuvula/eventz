package com.bersyte.eventz.common;

import com.bersyte.eventz.common.presentation.exceptions.DatabaseOperationException;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.users.infrastructure.persistence.repositories.UserJpaRepository;
import com.bersyte.eventz.features.users.domain.model.UserRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserCommonServiceTest {

    @InjectMocks
    UserCommonService userCommonService;

    @Mock
    UserJpaRepository userJpaRepository;


    @Test
    void shouldGetUserByIdSuccessfully() {
        //Given
        Long userId = 1L;
        UserEntity user = getOrganizerForTest ();

        //Mock calls
        Mockito.when (userJpaRepository.findById (userId))
                .thenReturn (Optional.of (user));

        //When
        UserEntity returnedUser = userCommonService.getUserById (userId);

        //Act - Assert
        assertNotNull (returnedUser);

        assertEquals (userId, returnedUser.getId ());
        assertEquals (user, returnedUser);

        verify (userJpaRepository, times (1))
                .findById (userId);
    }

    @Test
    void shouldThrowDatabaseOperationExceptionIfUserNotFound() {
        // Arrange
        Long userId = 2L;

        //Mock calls
        doThrow (
                new DatabaseOperationException (
                        "Failed to get user with eventId " + 2 + " User not found"
                )
        )
                .when (userJpaRepository).findById (userId);

        //When
        DatabaseOperationException exception = assertThrows (
                DatabaseOperationException.class,
                () -> userCommonService.getUserById (userId)
        );

        //Act - Assert
        assertEquals ("Failed to get user with eventId " + 2 + " User not found", exception.getMessage ());
    }


    @Test
    void shouldThrowDatabaseOperationExceptionIfUserIsEmpty() {
        // Arrange
        Long userId = 2L;

        //Mock calls
        Mockito.when (userJpaRepository.findById (userId))
                .thenReturn (Optional.empty ());

        //When
        DatabaseOperationException exception = assertThrows (
                DatabaseOperationException.class,
                () -> userCommonService.getUserById (userId)
        );

        //Assert
        assertEquals ("User not found", exception.getMessage ());
    }

    @Test
    public void shouldFindUserByEmailSuccessfully() {
        //Arrange
        String email = "isaias@gmail.com";
        UserEntity user = getOrganizerForTest ();

        //Mock call
        Mockito.when (userJpaRepository.findByEmail (email))
                .thenReturn (Optional.of (user));

        //When
        UserEntity returnedUser = userCommonService.getUserByEmail (email);

        //Act
        assertNotNull (returnedUser);
        assertEquals (user, returnedUser);
    }

    @Test
    public void shouldThrowDatabaseOperationExceptionWhenUserWithEmailDoesNotExist() {
        //Arrange
        String email = "test@gmail.com";
        String expectedMsg = "User not found";

        // Mock call
        Mockito.when (userJpaRepository.findByEmail (email))
                .thenReturn (Optional.empty ());

        //When
        DatabaseOperationException exception = assertThrows (
                DatabaseOperationException.class,
                () -> userCommonService.getUserByEmail (email)
        );
        //
        assertEquals (expectedMsg, exception.getMessage ());
    }

    private UserEntity getOrganizerForTest() {
        return new UserEntity(
                1L,
                "isaias@gmail.com",
                "123456",
                "May",
                "Jane",
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