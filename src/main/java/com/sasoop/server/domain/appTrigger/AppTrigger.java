package com.sasoop.server.domain.appTrigger;

import com.sasoop.server.common.BaseTimeEntity;
import com.sasoop.server.domain.detailFunction.DetailFunction;
import com.sasoop.server.domain.app.App;
import com.sasoop.server.domain.triggerType.TriggerType;
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
    private boolean activated;

    @Column
    @Convert(converter = StringListConverter.class)
    private List<String> triggerValue;

    @ManyToOne
    @JoinColumn(name = "app_id")
    private App app;

    @ManyToOne
    @JoinColumn(name="trigger_type_id")
    private TriggerType triggerType;

    @ManyToOne
    @JoinColumn(name = "detail_function_id")
    private DetailFunction detailFunction;

}
