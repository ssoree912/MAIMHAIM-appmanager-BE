package com.sasoop.server.controller.dto.response;

import com.sasoop.server.domain.member.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
public class MemberResponse {
    @Getter
    @Setter
    @Builder
    @Schema(description = "유저 정보")
    public static class MemberInfo {
        @Schema(description = "유저 기본키")
        private Long memberId;


        @Schema(description = "유저 식별자")
        private String uuid;
    }
    @Getter
    @Setter
    @Schema(description = "앱매니저 정보")
    public static class Home{
        @Schema(description = "앱매니저 활성화")
        private boolean activate;

        @Schema(description = "트리커 개수")
        private int triggerCounts;

        public Home(Member member) {
            this.activate = member.isActivate();
            this.triggerCounts = member.getCount();
        }
    }

//    객체 생성
    public static MemberInfo toMemberInfo(Member member) {
        return MemberInfo.builder()
                .memberId(member.getMemberId())
                .uuid(member.getUuid())
                .build();
    }

}
