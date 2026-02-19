package com.bersyte.eventz.features.users.domain.repository;

import com.bersyte.eventz.common.domain.dtos.PagedResult;
import com.bersyte.eventz.common.domain.dtos.Pagination;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import java.util.Optional;
import java.util.UUID;

public interface UserRepository {
     AppUser save(AppUser user);
     AppUser update(AppUser user);
     void delete(UUID userId);
     Optional<AppUser> findById(UUID id);
     Optional<AppUser> findByEmail(String email);
     PagedResult<AppUser> fetchUsers(Pagination pagination);
    boolean alreadyExistsByEmail(String email);
}
