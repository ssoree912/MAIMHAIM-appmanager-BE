package com.sasoop.server.controller;

import com.sasoop.server.common.DTO.APIResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/user")
@Tag(name = "user")
public class UserController {
    @PostMapping
    public ResponseEntity<APIResponse> createUser() {
        return ResponseEntity.ok(new APIResponse());
    }
}
