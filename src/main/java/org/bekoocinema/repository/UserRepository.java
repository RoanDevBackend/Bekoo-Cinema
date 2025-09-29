package org.bekoocinema.repository;

import org.bekoocinema.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("FROM User u " +
            "WHERE u.email = :username")
    User findByUserName(String username);
    boolean existsByEmail(String email);
}
