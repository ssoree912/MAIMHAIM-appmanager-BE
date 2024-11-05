package com.sasoop.server.domain.appTrigger;

import com.sasoop.server.domain.app.App;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppTriggerRepository extends JpaRepository<AppTrigger, Long> {
    Optional<List<AppTrigger>> findByApp(App app);
}
