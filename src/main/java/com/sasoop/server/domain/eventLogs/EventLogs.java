package com.sasoop.server.domain.eventLogs;

import com.sasoop.server.common.BaseTimeEntity;
import com.sasoop.server.domain.appTrigger.AppTrigger;
import com.sasoop.server.domain.member.Member;
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
public class EventLogs extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long eventLogId;

    @Column
    private String description;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @ManyToOne
    @JoinColumn(name = "app_triiger_id")
    private AppTrigger appTrigger;



}
