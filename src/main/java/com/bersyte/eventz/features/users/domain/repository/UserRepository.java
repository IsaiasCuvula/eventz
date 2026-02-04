package com.bersyte.eventz.features.users.domain.repository;

import com.bersyte.eventz.common.domain.PagedResult;
import com.bersyte.eventz.common.domain.Pagination;
import com.bersyte.eventz.features.users.domain.model.AppUser;
import java.util.Optional;

public interface UserRepository {
     AppUser save(AppUser user);
     AppUser update(AppUser user);
     void delete(String userId);
     Optional<AppUser> findById(String id);
     Optional<AppUser> findByEmail(String email);
     PagedResult<AppUser> fetchUsers(Pagination pagination);
    boolean alreadyExistsByEmail(String email);
}
