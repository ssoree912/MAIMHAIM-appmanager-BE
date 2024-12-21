package com.sasoop.server.controller;

import com.sasoop.server.common.dto.APIResponse;
import com.sasoop.server.common.dto.ErrorResponse;
import com.sasoop.server.controller.dto.request.MemberRequest;
import com.sasoop.server.controller.dto.response.AppResponse;
import com.sasoop.server.controller.dto.response.MemberResponse;
import com.sasoop.server.domain.member.Member;
import com.sasoop.server.service.AppService;
import com.sasoop.server.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@Tag(name = "Home")
@RequiredArgsConstructor
public class HomeController {
    private final MemberService memberService;
    private final AppService appService;

    @Operation(summary = "앱매니저 메인 화면 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("{memberId}")
    public ResponseEntity<APIResponse<MemberResponse.Home>> getHome(@PathVariable("memberId") Long memberId){
        Member getMember = memberService.findByMemberId(memberId); //유저 조회
        List<AppResponse.AppInfo> appInfos =  appService.findByFilter(null,getMember);
        APIResponse response = memberService.getHome(getMember,appInfos);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }



    @Operation(summary = "앱 매니저 활성화 ")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @PatchMapping("/status")
    public ResponseEntity<APIResponse<MemberResponse.Home>> activateApp(@RequestBody MemberRequest.HomeActivate activateRequest){
        APIResponse response = memberService.activateAppManager(activateRequest);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "전체 count 값 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "요청 형식 혹은 요청 콘텐츠가 올바르지 않을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "요청한 URL/URI와 일치하는 항목을 찾지 못함,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "외부 API 요청 실패, 정상적 수행을 할 수 없을 때,",content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
    })
    @GetMapping("/{memberId}/count")
    public ResponseEntity<APIResponse<MemberResponse.Count>> getCount(@PathVariable("memberId") Long memberId){
        Member getMember = memberService.findByMemberId(memberId); //유저 조회
        APIResponse<MemberResponse.Count> response = memberService.getCount(getMember);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
