package com.bersyte.eventz.features.users.presentation.controllers;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.common.domain.Pagination;
import com.bersyte.eventz.features.auth.infrastructure.persistence.AppUserPrincipal;
import com.bersyte.eventz.features.users.application.dtos.*;
import com.bersyte.eventz.features.users.application.usecases.*;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final FetchUsersUseCase fetchUsersUseCase;
    private final SaveUserUseCase saveUserUseCase;
    private final UpdateUserUseCase updateUserUseCase;
    private final DeleteUserUseCase deleteUserUseCase;
    private final GetCurrentUserUseCase getCurrentUserUseCase;
    
    public UserController(
            FetchUsersUseCase fetchUsersUseCase,
            SaveUserUseCase   saveUserUseCase,
            UpdateUserUseCase updateUserUseCase,
            DeleteUserUseCase deleteUserUseCase,
            GetCurrentUserUseCase getCurrentUserUseCase
    ) {
        this.fetchUsersUseCase = fetchUsersUseCase;
        this.saveUserUseCase = saveUserUseCase;
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

    @PostMapping("{id}")
    public ResponseEntity<UserResponse> updateUser(
            @AuthenticationPrincipal AppUserPrincipal currentUser,
            @Valid @RequestBody UpdateUserRequest request,
            @PathVariable String id
    ) {
        UpdateUserInput input = new UpdateUserInput(
                request, currentUser.id(),
                id
        );
        final UserResponse response = updateUserUseCase.execute(input);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(
            @AuthenticationPrincipal AppUserPrincipal currentUser,
            @PathVariable String id
    ) {
        DeleteUserRequest request = new DeleteUserRequest(
                currentUser.id(), id
        );
        deleteUserUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping
    public ResponseEntity<UserResponse> create(
            @Valid @RequestBody CreateUserRequest request
    ) {
        UserResponse response = saveUserUseCase.execute(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
