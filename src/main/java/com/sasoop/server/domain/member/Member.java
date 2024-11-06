package com.sasoop.server.domain.member;

import com.sasoop.server.common.BaseTimeEntity;
import com.sasoop.server.controller.dto.request.MemberRequest;
import com.sasoop.server.domain.app.App;
import com.sasoop.server.handler.StringListConverter;
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
public class Member extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column
    private String location;
    private String uuid;
    private String email;
    private String preferences;
    private int count;
//    앱매니저 자체 활성화 여부
    private boolean activate;
//    json
    @Column
    @Convert(converter = StringListConverter.class)
    private List<String> setting;

    @OneToMany(mappedBy = "member",orphanRemoval = true,cascade = CascadeType.ALL)
    private List<App> apps = new ArrayList<>();

    @OneToOne
    @JoinColumn(name="shaker_app_id")
    private App shakerApp;

    public void updateActivate(boolean activate) {
        this.activate = activate;
    }

    public static Member toEntity(MemberRequest.CreateMember memberRequest) {
        return Member.builder()
                .location("")
                .uuid(memberRequest.getUuid())
                .email("")
                .preferences("")
                .activate(false)
                .build();
    }

    public void updateShakerApp(App shakerApp) {
        this.shakerApp = shakerApp;
    }

    public void addCount() {
        this.count +=1;
    }
}
