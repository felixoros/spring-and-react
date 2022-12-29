package com.task.demi.db.repositories;

import com.task.demi.db.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer>{
    @Query(value = "SELECT * FROM users u WHERE u.email = :email", nativeQuery = true)
    List<User> findByEmail(@Param("email") String email);
}
