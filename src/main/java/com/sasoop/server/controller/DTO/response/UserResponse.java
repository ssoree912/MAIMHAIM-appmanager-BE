package com.sasoop.server.controller.DTO.response;

import com.sasoop.server.domain.user.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class UserResponse {
    @Getter
    @Setter
    @Builder
    @Schema(description = "유저 정보")
    public static class UserInfo{
        @Schema(description = "유저 기본키")
        private Long userId;


        @Schema(description = "유저 식별자")
        private String uuid;
    }

//    객체 생성
    public static UserInfo toUserInfo(User user) {
        return UserInfo.builder()
                .userId(user.getUserId())
                .uuid(user.getUuid())
                .build();
    }
}
