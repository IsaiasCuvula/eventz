package com.bersyte.eventz.features.users.infrastructure.persistence.adapters;

import com.bersyte.eventz.common.domain.dtos.PagedResult;
import com.bersyte.eventz.common.domain.dtos.Pagination;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import com.bersyte.eventz.features.users.domain.repository.UserRepository;
import com.bersyte.eventz.features.users.infrastructure.persistence.entities.UserEntity;
import com.bersyte.eventz.features.users.infrastructure.persistence.mappers.UserEntityMapper;
import com.bersyte.eventz.features.users.infrastructure.persistence.repositories.UserJpaRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public class UserPersistenceAdapter implements UserRepository {
    
    private final UserEntityMapper userEntityMapper;
    private final UserJpaRepository userJpaRepository;
    
    public UserPersistenceAdapter(UserEntityMapper userEntityMapper, UserJpaRepository userJpaRepository) {
        this.userEntityMapper = userEntityMapper;
        this.userJpaRepository = userJpaRepository;
    }
    
    
    @Override
    public Optional<AppUser> findByEmail(String email) {
        return userJpaRepository.findByEmail(email)
                       .map(userEntityMapper::toDomain);
    }
    
    @Override
    public PagedResult<AppUser> fetchUsers(Pagination pagination) {
        Pageable pageable = PageRequest.of(pagination.page(), pagination.size());
        Page<UserEntity> userEntities = userJpaRepository.findAll(pageable);
        return userEntityMapper.toPagedResult(userEntities);
    }
    
    @Override
    public boolean alreadyExistsByEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
    
    @Override
    public AppUser save(AppUser user) {
        UserEntity entity = userEntityMapper.toUserEntity(user);
        UserEntity savedUser= userJpaRepository.save(entity);
        return userEntityMapper.toDomain(savedUser);
    }
    
    @Override
    public AppUser update(AppUser user) {
        UserEntity entity = userEntityMapper.toUserEntity(user);
        UserEntity savedUser= userJpaRepository.save(entity);
        return userEntityMapper.toDomain(savedUser);
    }
    
    @Override
    public void delete(UUID userId) {
        userJpaRepository.deleteById(userId);
    }
    
    @Override
    public Optional<AppUser> findById(UUID id) {
        return userJpaRepository.findById(id)
                       .map(userEntityMapper::toDomain);
    }
}
