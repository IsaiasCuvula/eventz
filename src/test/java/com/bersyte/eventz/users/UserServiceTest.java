package com.bersyte.eventz.users;

import com.bersyte.eventz.common.UserCommonService;
import com.bersyte.eventz.common.UserMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserCommonService userCommonService;

    @Mock
    private UserMapper userMapper;


    @Test
    void getAllUsers() {
    }

//    @Test
//    void getCurrentUser() {
//    }
//
//    @Test
//    void updateUser() {
//    }
//
//    @Test
//    void getUserByEmail() {
//    }
}