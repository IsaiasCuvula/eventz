package com.bersyte.eventz.users;

import com.bersyte.eventz.common.UserResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {

    private final UserService usersService;


    @GetMapping
    public ResponseEntity<List<UserResponseDto>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(usersService.getAllUsers(page, size));
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserResponseDto> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        final UserResponseDto response = usersService.getCurrentUser(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<UserResponseDto> updateUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateUserRequestDto requestDTO
    ) {
        String email = userDetails.getUsername();
        final UserResponseDto response = usersService.updateUser(requestDTO, email);
        return ResponseEntity.ok(response);
    }

}
