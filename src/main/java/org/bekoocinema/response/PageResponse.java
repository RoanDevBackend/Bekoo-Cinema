package org.bekoocinema.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponse <T>{
    int pageIndex;
    int pageSize;
    long totalElements;
    long totalPages;
    SortBy sortBy;
    List<T> content;

    @Getter
    @Setter(AccessLevel.PRIVATE)
    public static class SortBy {
        String property;
        String direction;

        public SortBy(String property, String direction) {
            this.property = property;
            this.direction = direction;
        }
    }
}
