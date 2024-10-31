package com.sasoop.server.domain.app;

import com.sasoop.server.common.BaseTimeEntity;
import com.sasoop.server.domain.apptrigger.AppTrigger;
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
    private String appName;
    private boolean activated;
    private Category category;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @OneToMany(mappedBy = "app")
    private List<AppTrigger> appTriggers = new ArrayList<>();


}
