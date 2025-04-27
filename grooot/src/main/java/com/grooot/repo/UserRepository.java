package com.grooot.repo;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.grooot.entities.Users;

@Repository
public interface UserRepository extends JpaRepository<Users, Long> {
    @Query("select usr from Users usr where externalSourceId = :externalSourceId")
    Optional<Users> fetchByExternalSrcId(@Param("externalSourceId") String externalSourceId);
    
}
