package com.grooot.repo;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.grooot.entities.UserTokens;

public interface UserTokensRepository extends JpaRepository<UserTokens,Long>{
    @Query("select ut from UserTokens ut where userId = :userId")
    Optional<UserTokens> fetchByUserId(@Param("userId") Long userId);

    @Modifying
    @Query("update UserTokens ut set " +
            "ut.accessToken = :accessToken, " +
            "where ut.userId = :userId ")
    int updateAccessTokenForUser(@Param("accessToken") String accessToken,
                                 @Param("userId") Long userId);

    @Modifying
    @Query("update UserTokens ut set " +
           "ut.accessToken = :accessToken, " +
           "ut.refreshToken = :refreshToken, " +
           "where ut.userId = :userId ")
    int updateAccessAndRefreshTokenForUser(
        @Param("accessToken") String accessToken, 
        @Param("refreshToken") String refreshToken, 
        @Param("userId") Long userId);                                          
}
