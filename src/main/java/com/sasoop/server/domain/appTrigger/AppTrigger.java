package com.sasoop.server.domain.appTrigger;

import com.fasterxml.jackson.databind.JsonNode;
import com.sasoop.server.common.BaseTimeEntity;
import com.sasoop.server.controller.dto.request.TriggerRequest;
import com.sasoop.server.domain.detailFunction.DetailFunction;
import com.sasoop.server.domain.app.App;
import com.sasoop.server.domain.triggerType.TriggerType;
import com.sasoop.server.handler.JsonConverter;
import com.sasoop.server.handler.StringListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppTrigger extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long triggerId;
    @Column(nullable = false)
    private String name;
    private boolean activate;
    private String urlScheme;

    @Column(columnDefinition = "text")
    @Convert(converter = JsonConverter.class)
    private JsonNode triggerValue;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private App app;

    @ManyToOne
    @JoinColumn(name="trigger_type_id")
    private TriggerType triggerType;

    public void updateActivate(boolean activate) {
        this.activate = activate;
    }

    public void updateTriggerValue(JsonNode triggerValue) {
        this.triggerValue = triggerValue;
    }

    public static AppTrigger toEntity(TriggerRequest.CreateTrigger triggerRequest, JsonNode triggerValue, App app, TriggerType triggerType, DetailFunction detailFunction) {
        return AppTrigger.builder()
                .name(triggerType.getTriggerTypeName())
                .activate(triggerRequest.isActivate())
                .triggerValue(triggerValue)
                .app(app)
                .triggerType(triggerType)
                .build();

    }


}
