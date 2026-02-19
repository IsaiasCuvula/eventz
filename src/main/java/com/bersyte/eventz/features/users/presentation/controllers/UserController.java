package com.bersyte.eventz.features.users.presentation.controllers;

import com.bersyte.eventz.common.domain.dtos.PagedResult;
import com.bersyte.eventz.common.domain.dtos.Pagination;
import com.bersyte.eventz.features.auth.infrastructure.persistence.AppUserPrincipal;
import com.bersyte.eventz.features.users.application.dtos.*;
import com.bersyte.eventz.features.users.application.usecases.*;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final FetchUsersUseCase fetchUsersUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    
    public UserController(
            FetchUsersUseCase fetchUsersUseCase,
            UpdateUserUseCase updateUserUseCase,
            DeleteUserUseCase deleteUserUseCase,
            GetCurrentUserUseCase getCurrentUserUseCase
    ) {
        this.fetchUsersUseCase = fetchUsersUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.getCurrentUserUseCase = getCurrentUserUseCase;
    }


    @GetMapping
    public ResponseEntity<PagedResult<UserResponse>> fetchUsers(
            @AuthenticationPrincipal AppUserPrincipal currentUser,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pagination pagination = new Pagination(page, size);
        FetchUsersRequest request = new FetchUsersRequest(
                currentUser.id() ,pagination
        );
        PagedResult<UserResponse> result = fetchUsersUseCase.execute(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal AppUserPrincipal currentUser
    ) {
        final UserResponse response = getCurrentUserUseCase.execute(currentUser.getId());
        return ResponseEntity.ok(response);
    }

    @PostMapping("{eventId}")
    public ResponseEntity<UserResponse> updateUser(
            @AuthenticationPrincipal AppUserPrincipal currentUser,
            @Valid @RequestBody UpdateUserRequest request,
            @PathVariable UUID eventId
    ) {
        UpdateUserInput input = new UpdateUserInput(
                request, currentUser.id(), eventId
        );
        final UserResponse response = updateUserUseCase.execute(input);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("{userId}")
    public ResponseEntity<String> deleteUser(
            @AuthenticationPrincipal AppUserPrincipal currentUser,
            @PathVariable UUID userId
    ) {
        DeleteUserRequest request = new DeleteUserRequest(
                currentUser.id(), userId
        );
        deleteUserUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }

}
