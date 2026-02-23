package com.bersyte.eventz.features.users.presentation.controllers;

import com.bersyte.eventz.common.domain.dtos.PagedResult;
import com.bersyte.eventz.common.domain.dtos.Pagination;
import com.bersyte.eventz.features.auth.application.dtos.RevokeAllTokensRequest;
import com.bersyte.eventz.features.auth.application.usecases.RevokeAllTokensUseCase;
import com.bersyte.eventz.features.auth.infrastructure.persistence.dtos.AppUserPrincipal;
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
    private final RevokeAllTokensUseCase  revokeAllTokensUseCase;
    
    public UserController(
            FetchUsersUseCase fetchUsersUseCase,
            UpdateUserUseCase updateUserUseCase,
            DeleteUserUseCase deleteUserUseCase,
            GetCurrentUserUseCase getCurrentUserUseCase, RevokeAllTokensUseCase revokeAllTokensUseCase
    ) {
        this.fetchUsersUseCase = fetchUsersUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.getCurrentUserUseCase = getCurrentUserUseCase;
        this.revokeAllTokensUseCase = revokeAllTokensUseCase;
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

    @PostMapping("{userId}")
    public ResponseEntity<UserResponse> updateUser(
            @AuthenticationPrincipal AppUserPrincipal currentUser,
            @Valid @RequestBody UpdateUserRequest request,
            @PathVariable UUID userId
    ) {
        UpdateUserInput input = new UpdateUserInput(
                request, currentUser.id(), userId
        );
        final UserResponse response = updateUserUseCase.execute(input);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("{userId}")
    public ResponseEntity<Void> deleteUser(
            @AuthenticationPrincipal AppUserPrincipal currentUser,
            @PathVariable UUID userId
    ) {
        DeleteUserRequest request = new DeleteUserRequest(
                currentUser.id(), userId
        );
        deleteUserUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/me/revoke-sessions/{userId}")
    public ResponseEntity<Void> revokeMySessions(
            @AuthenticationPrincipal AppUserPrincipal currentUser,
            @PathVariable UUID userId
    ) {
        RevokeAllTokensRequest request = new RevokeAllTokensRequest(
                currentUser.id(), userId
        );
        revokeAllTokensUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }

}
