package com.sasoop.server.controller.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
public class MemberRequest {
    @Getter
    @Setter
    public static class CreateMember {
        @NotBlank(message = "식별자를 입력해주세요")
        @Schema(description = "유저 식별자")
        private String uuid;
    }
}
