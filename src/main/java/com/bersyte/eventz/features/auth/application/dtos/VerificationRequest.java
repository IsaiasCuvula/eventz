package com.bersyte.eventz.features.auth.application.dtos;




public record VerificationRequest(
     String requesterId,
     String verificationCode
){
}
