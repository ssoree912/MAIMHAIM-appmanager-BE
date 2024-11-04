package com.sasoop.server.controller;

import com.sasoop.server.controller.dto.request.TriggerTypeRequest;
import com.sasoop.server.domain.triggerType.SettingOption;
import com.sasoop.server.domain.triggerType.SettingType;
import com.sasoop.server.service.TriggerTypeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/tigger-type")
@Tag(name = "TriggerType")
@RequiredArgsConstructor
public class TriggerTypeController {
    private final TriggerTypeService triggerTypeService;
    @PostMapping
    public ResponseEntity<?> createTriggerType(@RequestBody TriggerTypeRequest triggerTypeRequest) {
        triggerTypeService.getCreateType(triggerTypeRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping()
    public String getTriggerType(){
        return triggerTypeService.findBySettingType(SettingType.LOCATION);
    }

}
