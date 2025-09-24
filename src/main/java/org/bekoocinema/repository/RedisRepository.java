package org.bekoocinema.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository {
    void setTimeToLive(String key , Long time) ;
    void set(String key, Object value);
    Object get(String key);
    void delete(String key) ;
}
