package com.bersyte.eventz.features.users.application.dtos;

public record CreateUserRequest(
          String email,
          String firstName,
          String lastName,
          String phone
) {
}
