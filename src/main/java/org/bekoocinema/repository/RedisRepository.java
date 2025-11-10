package org.bekoocinema.repository;

import org.springframework.stereotype.Repository;

@Repository
public interface RedisRepository {
    /**
     * Mili s
     * @param key
     * @param time
     */
    void setTimeToLive(String key , Long time) ;
    void set(String key, Object value);
    Object get(String key);
    void delete(String key) ;
}
