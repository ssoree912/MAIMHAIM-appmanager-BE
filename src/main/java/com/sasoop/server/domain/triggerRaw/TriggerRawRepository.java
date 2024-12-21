package com.sasoop.server.domain.triggerRaw;

import com.sasoop.server.domain.appTrigger.AppTrigger;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TriggerRawRepository extends JpaRepository<TriggerRaw, Long> {
}
