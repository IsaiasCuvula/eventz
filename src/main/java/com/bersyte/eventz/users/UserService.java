package com.bersyte.eventz.users;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.common.UserMapper;
import com.bersyte.eventz.common.UserResponseDTO;
import com.bersyte.eventz.exceptions.DatabaseOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository usersRepository;


    public List<UserResponseDTO> getAllUsers(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<AppUser> users = usersRepository.findAll(pageable);
            return users.stream().map(UserMapper::toUserResponseDTO).toList();
        } catch (DataAccessException e) {
            String errorMsg = String.format("Failed load users  %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }


    public UserResponseDTO getCurrentUser(String email) {
        final AppUser user = getUserByEmail(email);
        return UserMapper.toUserResponseDTO(user);
    }

    public UserResponseDTO updateUser(
            UpdateUserRequestDTO requestDTO,
            String email
    ) {
        try {
            final AppUser oldUser = getUserByEmail(email);
            oldUser.setFirstName(requestDTO.firstName());
            oldUser.setLastName(requestDTO.lastName());

            if (requestDTO.phone() != null && !requestDTO.phone().isEmpty()) {
                oldUser.setPhone(requestDTO.phone());
            }
            //
            final AppUser updatedUser = usersRepository.save(oldUser);

            return UserMapper.toUserResponseDTO(updatedUser);

        } catch (DataAccessException e) {
            String errorMsg = String.format("Failed to update user:  %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }

    public AppUser getUserByEmail(String email) {
        try {
            return usersRepository.findByEmail(email);
        } catch (DataAccessException e) {
            String errorMsg = String.format("Failed to get user by email %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }

    public AppUser getUserById(Long id) {
        try {
            Optional<AppUser> userOptional = usersRepository.findById(id);
            if (userOptional.isEmpty()) {
                throw new DatabaseOperationException("User not found");
            }
            return userOptional.get();
        } catch (DataAccessException e) {
            String errorMsg = String.format("Failed to get user by email %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }
}
