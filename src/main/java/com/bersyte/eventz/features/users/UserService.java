package com.bersyte.eventz.features.users;

import com.bersyte.eventz.common.AppUser;
import com.bersyte.eventz.common.UserCommonService;
import com.bersyte.eventz.common.UserMapper;
import com.bersyte.eventz.common.UserResponseDto;
import com.bersyte.eventz.exceptions.DatabaseOperationException;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserCommonService userCommonService;
    private final UserMapper userMapper;

    public UserService(UserRepository userRepository, UserCommonService userCommonService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.userCommonService = userCommonService;
        this.userMapper = userMapper;
    }

    public List<UserResponseDto> getAllUsers(int page, int size) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<AppUser> users = userRepository.findAll(pageable);
            return users.stream ().map (userMapper::toUserResponseDTO).toList ();
        } catch (DataAccessException e) {
            throw new DatabaseOperationException (
                    "Error access database " + e.getLocalizedMessage ()
            );
        }
    }


    public UserResponseDto getCurrentUser(String email) {
        final AppUser user = getUserByEmail(email);
        return userMapper.toUserResponseDTO (user);
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

            return userMapper.toUserResponseDTO (updatedUser);

        } catch (DataAccessException e) {
            throw new DatabaseOperationException (
                    "Error access database " + e.getLocalizedMessage ()
            );
        }
    }

    public AppUser getUserByEmail(String email) {
        return userCommonService.getUserByEmail (email);
    }


}
