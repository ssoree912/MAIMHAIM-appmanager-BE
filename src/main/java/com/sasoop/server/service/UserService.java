package com.sasoop.server.service;

import com.sasoop.server.common.dto.APIResponse;
import com.sasoop.server.common.dto.enums.SuccessCode;
import com.sasoop.server.controller.dto.request.UserRequest;
import com.sasoop.server.controller.dto.response.UserResponse;
import com.sasoop.server.domain.user.User;
import com.sasoop.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final UserResponse userResponse;

    public APIResponse<UserResponse.UserInfo> createUser(UserRequest.CreateUser userRequest) {
//        uuid 중복처리
        if(userRepository.existsByUuid(userRequest.getUuid())) throw new IllegalArgumentException("User already exists");
        User user = User.toEntity(userRequest);
        User savedUser = userRepository.save(user);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS, userResponse.toUserInfo(savedUser));

    }

    //유저조회
    public User findByUser(Long userId){
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("Invalid userId"));
    }
}
