package com.sasoop.server.domain.app;

import com.sasoop.server.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppRepository extends JpaRepository<App, Long> {
    boolean existsByMemberAndPackageName(Member member, String packageName);
}
