package org.bekoocinema.repository;

import org.bekoocinema.entity.Cinema;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CinemaRepository extends JpaRepository<Cinema, String> {

    @Query("FROM Cinema c " +
            "WHERE :keyWord IS NULL OR " +
            ":keyWord = '' OR " +
            "c.province LIKE CONCAT('%', :keyWord, '%') OR " +
            "c.name LIKE CONCAT('%', :keyWord, '%') ")
    List<Cinema> getAllByKey(String keyWord);

    @Query("FROM Cinema c ORDER BY c.name ASC")
    Page<Cinema> findAllCinemas(Pageable pageable);

}
