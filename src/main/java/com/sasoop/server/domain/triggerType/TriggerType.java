package com.sasoop.server.domain.triggerType;

import com.fasterxml.jackson.databind.JsonNode;
import com.sasoop.server.common.BaseTimeEntity;
import com.sasoop.server.handler.JsonConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TriggerType extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long triggerTypeId;

    @Column(nullable = false)
    private String triggerTypeName;

    @Column
    @Enumerated(EnumType.STRING)
    private SettingType settingType;

    @Column(columnDefinition = "text")
    @Convert(converter = JsonConverter.class)
    private JsonNode settingOptions;



}
