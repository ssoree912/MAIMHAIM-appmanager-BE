package com.sasoop.server.domain.app;

import com.sasoop.server.domain.managedApp.ManagedApp;
import com.sasoop.server.domain.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface AppRepository extends JpaRepository<App, Long> {
    boolean existsByMemberAndPackageName(Member member, String packageName);

    Optional<List<App>> findByMember(Member member);

    @Query("SELECT a FROM App a " +
            "WHERE (a.add = :add )" +
            "AND( a.member = :member) " +
            "AND (:keyword IS NULL OR a.name LIKE %:keyword%)"
                + "ORDER BY a.name")
    Optional<List<App>> findByFilter(@Param("add") boolean add, @Param("keyword") String keyword,@Param("member") Member member);

    @Query("SELECT a FROM App a " +
            "WHERE ( a.member = :member) " +
            "AND (:keyword IS NULL OR a.name LIKE %:keyword%)"
            + "ORDER BY a.name")
    Optional<List<App>> findByMemberAndKeywordk(@Param("keyword") String keyword,@Param("member") Member member);

    @Query("SELECT a.packageName FROM App a WHERE a.member = :member AND a.managedApp = :managedApp")
    Optional<String> getPackageNameByMemberAndManagedApp(Member member, ManagedApp managedApp);

    @Query("SELECT a FROM App a WHERE a.member = :member AND a.managedApp = :managedApp")
    Optional<App> findByMemberAndManagedApp(Member member, ManagedApp managedApp);

}
