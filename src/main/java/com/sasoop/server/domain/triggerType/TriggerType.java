package com.sasoop.server.domain.triggerType;

import com.sasoop.server.common.BaseTimeEntity;
import com.sasoop.server.handler.StringListConverter;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    @Convert(converter = StringListConverter.class)
    private List<String> settingOptions;



}
