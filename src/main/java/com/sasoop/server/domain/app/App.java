package com.sasoop.server.domain.app;

import com.sasoop.server.common.BaseTimeEntity;
import com.sasoop.server.controller.dto.request.AppRequest;
import com.sasoop.server.domain.appTrigger.AppTrigger;
import com.sasoop.server.domain.managedApp.ManagedApp;
import com.sasoop.server.domain.member.Member;
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
    private String packageName;
    private boolean activate;
    private boolean advancedActivate;
    private String uid;
    private boolean add;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name="managed_app_id")
    private ManagedApp managedApp;

    @OneToMany(mappedBy = "app")
    private List<AppTrigger> appTriggers = new ArrayList<>();

    public static App toEntity(AppRequest.AppSetting appSetting, Member member, boolean add, ManagedApp managedApp){
        return App.builder()
                .member(member)
                .name(managedApp.getName())
                .managedApp(managedApp)
                .activate(false)
                .advancedActivate(false)
                .packageName(appSetting.getPackageName())
                .add(add)
                .uid(appSetting.getUid())
                .build();
    }

    public void updateActivate(boolean activate) {
        this.activate = activate;
    }

    public void updateAdd(boolean add) {
        this.add = add;
    }
}
