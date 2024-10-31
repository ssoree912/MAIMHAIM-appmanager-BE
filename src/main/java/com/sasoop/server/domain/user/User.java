package com.sasoop.server.domain.user;

import com.sasoop.server.common.BaseTimeEntity;
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
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column
    private String location;
    private String uuid;
    private String email;
    private String preferences;
    private boolean activated;
//    json
    @Column
    @Convert(converter = StringListConverter.class)
    private List<String> setting;

    @OneToMany(mappedBy = "user",orphanRemoval = true,cascade = CascadeType.ALL)
    private List<App> apps = new ArrayList<>();
}
