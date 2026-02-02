package com.bersyte.eventz.features.users.infrastructure.persistence.mappers;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.features.events.domain.model.Event;
import com.bersyte.eventz.features.events.infrastructure.persistence.entities.EventEntity;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import jakarta.persistence.*;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class UserEntityMapper {
    
    
    public PagedResult<AppUser> toPagedResult(Page<UserEntity> userEntityPage){
        List<AppUser> usersList = userEntityPage.getContent().stream().map(this::toDomain).toList();
        return new PagedResult<>(
                usersList, userEntityPage.getTotalElements(),
                userEntityPage.getTotalPages(),
                userEntityPage.isLast()
        );
    }
    
    
    public AppUser toDomain(UserEntity entity){
         return AppUser.restore(
             entity.getId(),
             entity.getEmail(),
             entity.getFirstName(),
             entity.getLastName(),
             entity.getPhone(),
             entity.getRole(),
             entity.isEnabled(),
             entity.getCreatedAt(),
             entity.getUpdateAt(),
             entity.getVerificationCode(),
             entity.getPassword(),
             entity.getVerificationExpiration()
        );
    }

    public UserEntity toUserEntity(AppUser user) {
        if (user == null) {
            throw new IllegalArgumentException ("User cannot be null");
        }
        UserEntity entity = new UserEntity();
        entity.setId(user.getId());
        entity.setEmail(user.getEmail());
        entity.setPassword(user.getPassword());
        entity.setRole(user.getRole());
        entity.setPhone(user.getPhone());
        entity.setFirstName(user.getFirstName());
        entity.setLastName(user.getLastName());
        entity.setCreatedAt(user.getCreatedAt());
        entity.setCreatedAt(user.getUpdatedAt());
        entity.setVerificationCode(user.getVerificationCode());
        entity.setVerificationExpiration(user.getVerificationExpiration());
        entity.setEnabled(user.isEnabled());
        return entity;
    }
    
}
