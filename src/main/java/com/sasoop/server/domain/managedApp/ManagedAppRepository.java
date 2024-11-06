package com.sasoop.server.domain.managedApp;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ManagedAppRepository extends JpaRepository<ManagedApp, Long> {
    boolean existsByPackageName(String packageName);
    Optional<ManagedApp> findByPackageName(String packageName);

    @Query("SELECT m FROM ManagedApp m WHERE m.apBSSID LIKE %:apBSSID%")
    Optional<ManagedApp> findByApBSSIDContaining(@Param("apBSSID") String apBSSID);
    Optional<ManagedApp> findByApBSSID(String apBSSID);
    boolean existsByApBSSID(String apBSSID);

}
