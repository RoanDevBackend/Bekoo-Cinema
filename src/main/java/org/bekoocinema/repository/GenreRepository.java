package org.bekoocinema.repository;

import org.bekoocinema.entity.Genre;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreRepository extends JpaRepository<Genre, String> {
    boolean existsByName(String name);
}
