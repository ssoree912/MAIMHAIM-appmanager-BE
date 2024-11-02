package com.sasoop.server.domain.managedApp;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ManagedAppRepository extends JpaRepository<ManagedApp, Long> {
    boolean existsByPackageName(String packageName);
}
