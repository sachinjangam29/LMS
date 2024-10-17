package com.jangam.schoolmgt.userservice.repository;

import com.jangam.schoolmgt.userservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Integer> {

//    @Query(value = "SELECT CASE WHEN COUNT(*) > 0 THEN true ELSE false END FROM `user` WHERE username = :username", nativeQuery = true)
//    Boolean existsByUsernameNative(@Param("username") String username);

    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
}
