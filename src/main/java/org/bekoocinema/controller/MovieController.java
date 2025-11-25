package org.bekoocinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.constant.EndPointConstant;
import org.bekoocinema.request.movie.CreateMovieRequest;
import org.bekoocinema.request.movie.UpdateMovieRequest;
import org.bekoocinema.response.ApiResponse;
import org.bekoocinema.response.movie.MovieResponse;
import org.bekoocinema.service.MovieService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class MovieController {

    final MovieService movieService;

    @PostMapping("/movie")
    public ApiResponse addMovie(@ModelAttribute @Valid CreateMovieRequest movieRequest) {
        movieService.addMovie(movieRequest);
        return ApiResponse.success(201, "Thêm thành công");
    }

    @Operation(
            summary = "Cập nhật phim",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @PutMapping("/movie/{movieId}")
    public ApiResponse updateMovie(@PathVariable String movieId, @ModelAttribute @Valid UpdateMovieRequest updateMovieRequest) {
        movieService.updateMovie(movieId, updateMovieRequest);
        return ApiResponse.success(200, "Cập nhật phim thành công");
    }

    @Operation(
            summary = "Xóa phim",
            security = {
                    @SecurityRequirement(name = "bearerAuth")
            }
    )
    @DeleteMapping("/movie/{movieId}")
    public ApiResponse deleteMovie(@PathVariable String movieId) {
        movieService.deleteMovie(movieId);
        return ApiResponse.success(200, "Xóa phim thành công");
    }

    @Operation(summary = "Tìm kiếm phim", parameters = {
            @Parameter(name = "orderBy", description = "<h4>Truyền vào giá trị từ 1 -> 5</h4>" +
                    "{1}. Sắp xếp theo thời gian khởi chiếu</br>" +
                    "{2}. Sắp xếp theo giá vé </br>" +
                    "{3}. Sắp xếp theo tên </br>" +
                    "{4}. Sắp xếp theo số lượt xem </br>" +
                    "{5}. Sắp xếp theo lượt thời lượng chiếu "),
            @Parameter(name = "searchName", description = "Từ khoá muốn tìm kiếm, không truyền để lấy ra tất cả"),
            @Parameter(name = "sortDirection", description = "acs/desc"),
            @Parameter(name = "genre", description = "Tìm theo thể loại, không truyền để lấy ra tất cả")
    })
    @GetMapping(EndPointConstant.PUBLIC + "/movie/filter")
    public ApiResponse filterMovie(@RequestParam(required = false) String searchName,
                                     @RequestParam(required = false) String genre,
                                     @RequestParam(required = false, defaultValue = "0 ") int minPrice,
                                     @RequestParam(required = false, defaultValue = "999999999") int maxPrice,
                                     @RequestParam(required = false, defaultValue = "1") int pageIndex,
                                     @RequestParam(required = false, defaultValue = "40") int pageSize,
                                     @RequestParam(required = false, defaultValue = "1") String orderType,
                                     @RequestParam(required = false, defaultValue = "asc") String sortDirection) {
        return ApiResponse.success(200, "Thành công", movieService.filterMovie(searchName, genre, minPrice, maxPrice, pageIndex, pageSize, orderType, sortDirection));
    }

    @GetMapping(EndPointConstant.PUBLIC + "/movie/{id}")
    public ApiResponse getMovieById(@PathVariable String id) {
        MovieResponse movie = movieService.getMovieById(id);
        if (movie == null) {
            return ApiResponse.success(200, "Không tìm thấy phim", null);
        }
        return ApiResponse.success(200, "Thành công", movie);
    }

    @GetMapping(EndPointConstant.PUBLIC + "/movie/by-date/{date}")
    public ApiResponse getMovieByDate(@PathVariable String date) {
        List<?> movies = movieService.getMovieByDate(date);
        String message = movies.isEmpty() ? "Không có phim nào trong ngày này" : "Lấy các phim chiếu theo ngày thành công";
        return ApiResponse.success(200, message, movies);
    }


    @GetMapping(EndPointConstant.PUBLIC + "/movie/showing")
    public ApiResponse getMovieShowing(
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "40") int pageSize
    ){
        return ApiResponse.success(200, "Thành công", movieService.getMovieShowing(pageIndex, pageSize));
    }

    @GetMapping(EndPointConstant.PUBLIC + "/movie/upcoming")
    public ApiResponse getUpcomingMovies(
            @RequestParam(required = false, defaultValue = "1") int pageIndex,
            @RequestParam(required = false, defaultValue = "40") int pageSize
    ){
        return ApiResponse.success(200, "Thành công", movieService.getUpcomingMovie(pageIndex, pageSize));
    }
}
