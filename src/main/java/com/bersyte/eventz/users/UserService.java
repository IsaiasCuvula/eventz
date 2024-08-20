package com.bersyte.eventz.users;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.common.UserCommonService;
import com.bersyte.eventz.common.UserMapper;
import com.bersyte.eventz.common.UserResponseDto;
import com.bersyte.eventz.exceptions.DatabaseOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserCommonService userCommonService;

    public List<UserResponseDto> getAllUsers(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<AppUser> users = userRepository.findAll(pageable);
            return users.stream().map(UserMapper::toUserResponseDTO).toList();
        } catch (DataAccessException e) {
            String errorMsg = String.format("Failed load users  %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }


    public UserResponseDto getCurrentUser(String email) {
        final AppUser user = getUserByEmail(email);
        return UserMapper.toUserResponseDTO(user);
    }

    public UserResponseDto updateUser(
            UpdateUserRequestDto requestDTO,
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
            final AppUser updatedUser = userRepository.save(oldUser);

            return UserMapper.toUserResponseDTO(updatedUser);

        } catch (DataAccessException e) {
            String errorMsg = String.format("Failed to update user:  %s", e.getLocalizedMessage());
            throw new DatabaseOperationException(errorMsg);
        }
    }

    public AppUser getUserByEmail(String email) {
        return userCommonService.findUserByEmail(email);
    }


}
