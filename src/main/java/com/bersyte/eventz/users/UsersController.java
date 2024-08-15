package com.bersyte.eventz.users;

import com.bersyte.eventz.security.auth.UserResponseDTO;
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
public class UsersController {

    private final UsersService usersService;


    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(usersService.getAllUsers(page, size));
    }

    @GetMapping("/current-user")
    public ResponseEntity<UserResponseDTO> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        String email = userDetails.getUsername();
        final UserResponseDTO response = usersService.getCurrentUser(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/update")
    public ResponseEntity<UserResponseDTO> updateUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody UpdateUserRequestDTO requestDTO
    ) {
        String email = userDetails.getUsername();
        final UserResponseDTO response = usersService.updateUser(requestDTO, email);
        return ResponseEntity.ok(response);
    }

}
