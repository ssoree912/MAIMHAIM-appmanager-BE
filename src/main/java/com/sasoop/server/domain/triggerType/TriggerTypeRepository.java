package com.sasoop.server.domain.triggerType;

import org.hibernate.mapping.Set;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TriggerTypeRepository extends JpaRepository<TriggerType, Long> {
    Optional<TriggerType> findBySettingType(SettingType settingType);

}
