package org.bekoocinema.repository;

import org.bekoocinema.entity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface GenreRepository extends JpaRepository<Genre, String> {
    boolean existsByName(String name);

    @Query("FROM Genre g " +
            "WHERE :name IS NULL OR " +
            ":name = '' OR " +
            "g.name like CONCAT('%', :name, '%')")
    List<Genre> findAllByName(String name);
}
