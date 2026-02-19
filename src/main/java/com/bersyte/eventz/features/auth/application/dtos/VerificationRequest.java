package com.bersyte.eventz.features.auth.application.dtos;


import java.util.UUID;

public record VerificationRequest(
     UUID requesterId,
     String verificationCode
){
}
