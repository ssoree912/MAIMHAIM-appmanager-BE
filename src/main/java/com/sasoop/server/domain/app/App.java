package com.sasoop.server.domain.app;

import com.sasoop.server.common.BaseTimeEntity;
import com.sasoop.server.controller.dto.request.AppRequest;
import com.sasoop.server.domain.appTrigger.AppTrigger;
import com.sasoop.server.domain.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class App extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appId;

    @Column(nullable = false)
    private String name;
    private boolean activate;
    private boolean advancedActivate;
//    private Category category;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "app")
    private List<AppTrigger> appTriggers = new ArrayList<>();

    public static App toEntity(AppRequest.AppSetting appSetting, User user){
        return App.builder()
                .user(user)
                .name(appSetting.getName())
                .activate(false)
                .advancedActivate(false)
                .build();
    }

}
