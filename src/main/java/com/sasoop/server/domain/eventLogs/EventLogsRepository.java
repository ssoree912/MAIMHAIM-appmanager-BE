package com.sasoop.server.domain.eventLogs;

import com.sasoop.server.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventLogsRepository extends JpaRepository<EventLogs, Long> {
    Optional<List<EventLogs>> findByMember(Member member);
}
