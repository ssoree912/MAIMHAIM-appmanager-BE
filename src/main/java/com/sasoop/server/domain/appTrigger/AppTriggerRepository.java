package com.sasoop.server.domain.appTrigger;

import com.sasoop.server.domain.app.App;
import com.sasoop.server.domain.triggerType.TriggerType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AppTriggerRepository extends JpaRepository<AppTrigger, Long> {
    @Query("SELECT at FROM AppTrigger at" +
            " WHERE at.app = :app " +
            "AND (:triggerType IS NULL OR at.triggerType = :triggerType)")
    Optional<List<AppTrigger>> findByAppAndOptionalTriggerType(
            @Param("app") App app,
            @Param("triggerType") TriggerType triggerType);
    Optional<List<AppTrigger>> findByApp(App app);

    Optional<List<AppTrigger>> findByTriggerType(TriggerType triggerType);

    @Query("SELECT a FROM AppTrigger a WHERE a.app = :app AND CAST(a.triggerValue AS text) LIKE %:locationString%")
    AppTrigger findByAppAndTriggerValueContaining(@Param("app") App app, @Param("locationString") String locationString);

    @Query("SELECT COUNT(a) > 0 FROM AppTrigger a WHERE a.app = :app AND CAST(a.triggerValue AS text) LIKE %:locationString%")
    boolean existsByAppAndTriggerValueContaining(@Param("app") App app, @Param("locationString") String locationString);

}
