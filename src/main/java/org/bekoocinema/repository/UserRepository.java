package org.bekoocinema.repository;

import org.bekoocinema.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    @Query("FROM User u " +
            "WHERE u.email = :username")
    User findByUserName(String username);

    boolean existsByEmail(String email);

    @Query("FROM User u " +
            "WHERE (:key IS NULL OR :key = '' OR " +
            "LOWER(CONCAT(u.firstName, ' ', u.lastName)) LIKE LOWER(CONCAT('%', :key, '%')))")
    Page<User> findAllWithSearch(@Param("key") String key, Pageable pageable);
}
