package com.sasoop.server.service;

import com.sasoop.server.common.DTO.APIResponse;
import com.sasoop.server.common.DTO.enums.SuccessCode;
import com.sasoop.server.controller.DTO.request.UserRequest;
import com.sasoop.server.controller.DTO.response.UserResponse;
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
        if(userRepository.existsByUuid(userRequest.getUuid())) throw new IllegalArgumentException("중복된 uuid입니다");
        User user = User.toEntity(userRequest);
        User savedUser = userRepository.save(user);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS, userResponse.toUserInfo(savedUser));

    }
}
