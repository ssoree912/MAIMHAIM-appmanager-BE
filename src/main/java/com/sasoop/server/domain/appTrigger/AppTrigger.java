package com.sasoop.server.domain.appTrigger;

import com.fasterxml.jackson.databind.JsonNode;
import com.sasoop.server.common.BaseTimeEntity;
import com.sasoop.server.controller.dto.request.TriggerRequest;
import com.sasoop.server.domain.LocationTrigger.LocationTriggerReport;
import com.sasoop.server.domain.app.App;
import com.sasoop.server.domain.detailFunction.DetailFunction;
import com.sasoop.server.domain.triggerType.SettingType;
import com.sasoop.server.domain.triggerType.TriggerType;
import com.sasoop.server.handler.JsonConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
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
    private boolean foreGround;
    private int count;


//    private String urlScheme;

    @Column(columnDefinition = "text")
    @Convert(converter = JsonConverter.class)
    private JsonNode triggerValue;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private App app;

    @ManyToOne
    @JoinColumn(name="trigger_type_id")
    private TriggerType triggerType;

    @ManyToOne
    @JoinColumn(name = "function_id")
    private DetailFunction function;

    @OneToMany(mappedBy = "appTrigger", cascade = CascadeType.ALL)
    private List<LocationTriggerReport> locationTriggerReports = new ArrayList<>();

    public void updateActivate(boolean activate) {
        this.activate = activate;
    }

    public void updateTriggerValue(JsonNode triggerValue) {
        this.triggerValue = triggerValue;
    }

    public void updateForeGround(boolean foreGround) {
        this.foreGround = foreGround;
    }
    public void addCount() {
        this.count+=1;
    }

    public static AppTrigger toEntity(TriggerRequest.CreateTrigger triggerRequest, JsonNode triggerValue, App app, TriggerType triggerType, DetailFunction detailFunction) {
        return AppTrigger.builder()
                .name(triggerType.getTriggerTypeName())
                .activate(triggerRequest.isActivate())
                .triggerValue(triggerValue)
                .function(detailFunction)
                .app(app)
                .count(0)
                .triggerType(triggerType)
                .build();

    }
    public static AppTrigger toEntity( JsonNode triggerValue, App app, TriggerType triggerType, DetailFunction detailFunction) {
        boolean defaultActive = false;
        if(app.getManagedApp().getSSID().equals("gs25") && triggerType.getSettingType().equals(SettingType.MOTION)){
            defaultActive = true;
        }else if (!app.getManagedApp().getSSID().equals("gs25") && triggerType.getSettingType().equals(SettingType.LOCATION)){
            defaultActive = true;
        }
        return AppTrigger.builder()
                .name(triggerType.getTriggerTypeName())
                .activate(defaultActive)
                .triggerValue(triggerValue)
                .foreGround(true)
                .function(detailFunction)
                .app(app)
                .triggerType(triggerType)
                .build();

    }


}
