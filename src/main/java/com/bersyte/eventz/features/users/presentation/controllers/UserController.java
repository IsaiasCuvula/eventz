package com.bersyte.eventz.features.users.presentation.controllers;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.common.domain.Pagination;
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
            GetCurrentUserUseCase getCurrentUserUseCase) {
        this.fetchUsersUseCase = fetchUsersUseCase;
        this.saveUserUseCase = saveUserUseCase;
        this.updateUserUseCase = updateUserUseCase;
        this.deleteUserUseCase = deleteUserUseCase;
        this.getCurrentUserUseCase = getCurrentUserUseCase;
    }


    @GetMapping
    public ResponseEntity<PagedResult<UserResponse>> fetchUsers(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Pagination pagination = new Pagination(page, size);
        String requesterEmail = userDetails.getUsername();;
        FetchUsersRequest request = new FetchUsersRequest(requesterEmail,pagination);
        PagedResult<UserResponse> result = fetchUsersUseCase.execute(request);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserResponse> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        final UserResponse response = getCurrentUserUseCase.execute(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("{id}")
    public ResponseEntity<UserResponse> updateUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateUserRequest request,
            @PathVariable String id
    ) {
        String email = userDetails.getUsername();
        UpdateUserInput input = new UpdateUserInput(request, email,id);
        final UserResponse response =updateUserUseCase.execute(input);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("{id}")
    public ResponseEntity<String> deleteUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable String id
    ) {
        DeleteUserRequest request = new DeleteUserRequest(
                userDetails.getUsername(), id
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
