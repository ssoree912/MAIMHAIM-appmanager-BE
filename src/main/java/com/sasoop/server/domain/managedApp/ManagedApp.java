package com.sasoop.server.domain.managedApp;

import com.sasoop.server.common.BaseTimeEntity;
import com.sasoop.server.domain.category.Category;
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
public class ManagedApp extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long managedAppId;

    @Column(nullable = false)
    private String name;
    private String packageName;
    private String mainUrl;
    private String image;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

}
