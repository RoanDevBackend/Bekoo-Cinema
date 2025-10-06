package org.bekoocinema.repository;

import org.bekoocinema.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, String> {

    @Query("SELECT i.url " +
            "FROM Image i " +
            "WHERE i.targetId = :targetId AND " +
            "i.imageType = :imageType ")
    List<String> getUrlByTargetId(String imageType, String targetId);
}
