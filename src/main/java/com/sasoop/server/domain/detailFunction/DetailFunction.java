package com.sasoop.server.domain.detailFunction;

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
public class DetailFunction extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long detailFunctionId;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    @JoinColumn(name= "category_id")
    private Category category;




}
