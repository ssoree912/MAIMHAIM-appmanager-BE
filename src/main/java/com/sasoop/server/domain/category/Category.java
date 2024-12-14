package com.sasoop.server.domain.category;

import com.sasoop.server.common.BaseTimeEntity;
import com.sasoop.server.domain.detailFunction.DetailFunction;
import com.sasoop.server.domain.managedApp.ManagedApp;
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
public class Category extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false)
    private String name;

    @OneToMany(mappedBy = "category",orphanRemoval = true, cascade = CascadeType.ALL)
    private List<DetailFunction> detailFunctions = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<ManagedApp> managedApps = new ArrayList<>();
}
