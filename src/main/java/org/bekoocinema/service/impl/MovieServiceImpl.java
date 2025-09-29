package org.bekoocinema.service.impl;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.entity.Genre;
import org.bekoocinema.entity.Movie;
import org.bekoocinema.mapper.MovieMapper;
import org.bekoocinema.repository.GenreRepository;
import org.bekoocinema.repository.MovieRepository;
import org.bekoocinema.request.movie.CreateMovieRequest;
import org.bekoocinema.response.PageResponse;
import org.bekoocinema.response.movie.MovieResponse;
import org.bekoocinema.service.MovieService;
import org.hibernate.search.engine.search.query.SearchResult;
import org.hibernate.search.mapper.orm.Search;
import org.hibernate.search.mapper.orm.session.SearchSession;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class MovieServiceImpl implements MovieService {

    final MovieRepository movieRepository;
    final GenreRepository genreRepository;
    final MovieMapper movieMapper;
    final EntityManager entityManager;

    @Override
    public void addMovie(CreateMovieRequest createMovieRequest) {
        Movie movie = movieMapper.toMovie(createMovieRequest);
        Set<Genre> genres = new HashSet<>();
        for(String id : createMovieRequest.getGenreIds()) {
            var genreOptional = genreRepository.findById(id);
            genreOptional.ifPresent(genres::add);
        }
        movie.setGenres(genres);
        movieRepository.save(movie);
    }

    @Override
    @Transactional(readOnly = true)
    public PageResponse<?> filterMovie(
            String searchName,
            String genre,
            int minPrice,
            int maxPrice,
            int pageIndex,
            int pageSize,
            String orderType,
            String sortDirection) {
        this.validPage(pageIndex, pageSize);
        String orderBy = this.getOrderBy(orderType);
        SearchSession searchSession = Search.session(entityManager);
        SearchResult<Movie> searchResult = searchSession.search(Movie.class)
                .where(f -> {
                    var bool = f.bool();
                    // Khoảng giá vé
                    bool.must(f.range().field("price").between(minPrice, maxPrice));
                    // Tìm theo tên hoặc mô tả hoặc thể loại
                    if (searchName != null && !searchName.isBlank()) {
                        bool.must(f.simpleQueryString()
                                .fields("name", "description", "genres.name")
                                .matching(searchName));
                    }
                    // Lọc theo genre.id
                    if (genre != null && !genre.isBlank()) {
                            bool.must(f.match().field("genres.id").matching(genre));
                    }
                    return bool;
                })
                .sort(f -> {
                    var sort = f.composite();
                    if ("asc".equalsIgnoreCase(sortDirection)) {
                        sort.add(f.field(orderBy).asc());
                    } else {
                        sort.add(f.field(orderBy).desc());
                    }
                    return sort;
                })
                .fetch((pageIndex - 1) * pageSize, pageSize);
        List<Movie> movies = searchResult.hits();
        return PageResponse.<MovieResponse>builder()
                .pageIndex(pageIndex)
                .pageSize(pageSize)
                .sortBy(new PageResponse.SortBy(orderBy, sortDirection))
                .content(movies.stream().map(movieMapper::toMovieResponse).toList())
                .totalElements(searchResult.total().hitCount())
                .totalPages((searchResult.total().hitCount() + pageSize - 1) / pageSize)
                .build();
    }


    private String getOrderBy(String orderType){
        return switch (orderType) {
            case "1" -> "releaseDate";
            case "2" -> "price";
            case "3" -> "name_sort";
            case "4" -> "totalSold";
            case "5" -> "totalReview";
            default -> throw new IllegalArgumentException("Loại sắp xếp không được định nghĩa: " + orderType);
        };
    }

    private void validPage(int pageIndex, int pageSize){
        if(pageIndex < 1 || pageSize < 1){
            throw new IllegalArgumentException("Số trang và số phần tử trong một trang cần lớn hơn 1");
        }
    }
}
