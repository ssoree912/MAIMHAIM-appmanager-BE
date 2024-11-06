package com.sasoop.server.service;

import com.sasoop.server.common.dto.APIResponse;
import com.sasoop.server.common.dto.enums.SuccessCode;
import com.sasoop.server.controller.dto.request.MemberRequest;
import com.sasoop.server.controller.dto.response.MemberResponse;
import com.sasoop.server.domain.member.Member;
import com.sasoop.server.domain.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final MemberResponse memberResponse;

    public APIResponse<MemberResponse.MemberInfo> createMember(MemberRequest.CreateMember memberRequest) {
//        uuid 중복처리
        if(memberRepository.existsByUuid(memberRequest.getUuid())) throw new IllegalArgumentException("Member already exists");
        Member member = Member.toEntity(memberRequest);
        Member savedMember = memberRepository.save(member);
        return APIResponse.of(SuccessCode.INSERT_SUCCESS, memberResponse.toMemberInfo(savedMember));

    }

//        앱 매니저 활성화
    public APIResponse<MemberResponse.Home> activateAppManager(MemberRequest.HomeActivate activateRequest) {
        Member member = findByMemberId(activateRequest.getMemberId());
        member.updateActivate(activateRequest.isActive());
        memberRepository.save(member);

        return APIResponse.of(SuccessCode.UPDATE_SUCCESS, new MemberResponse.Home(member));


    }

    public APIResponse<MemberResponse.Home> getHome(Long memberId){
        Member member = findByMemberId(memberId);
        return APIResponse.of(SuccessCode.SELECT_SUCCESS, new MemberResponse.Home(member));
    }

    //유저조회
    public Member findByMemberId(Long memberId){
        return memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("Invalid memberId"));
    }

}
