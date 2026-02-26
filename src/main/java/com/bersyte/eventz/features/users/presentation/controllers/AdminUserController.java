package com.bersyte.eventz.features.users.presentation.controllers;


import com.bersyte.eventz.features.auth.application.dtos.RevokeAllTokensRequest;
import com.bersyte.eventz.features.auth.application.usecases.RevokeAllTokensUseCase;
import com.bersyte.eventz.features.auth.infrastructure.persistence.dtos.AppUserPrincipal;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/v1/admin/users")
public class AdminUserController {
    
    private final RevokeAllTokensUseCase revokeAllTokensUseCase;
    
    public AdminUserController(RevokeAllTokensUseCase revokeAllTokensUseCase) {
        this.revokeAllTokensUseCase = revokeAllTokensUseCase;
    }
    
    
    @PostMapping("/{userId}/revike-sessions")
    public ResponseEntity<Void> forceLogout(
            @AuthenticationPrincipal AppUserPrincipal currentUser,
            @PathVariable UUID userId
    ){
        
        RevokeAllTokensRequest request = new RevokeAllTokensRequest(
                currentUser.getId() , userId
        );
        revokeAllTokensUseCase.execute(request);
        return ResponseEntity.noContent().build();
    }
    
}
