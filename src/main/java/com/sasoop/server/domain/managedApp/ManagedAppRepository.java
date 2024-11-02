package com.sasoop.server.domain.managedApp;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ManagedAppRepository extends JpaRepository<ManagedApp, Long> {
    boolean existsByPackageName(String packageName);
    Optional<ManagedApp> findByPackageName(String packageName);
}
