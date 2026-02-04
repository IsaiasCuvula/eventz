package com.bersyte.eventz.features.auth.application.dtos;




public record VerifyUserRequest(
     String email,
     String verificationCode
){
}
