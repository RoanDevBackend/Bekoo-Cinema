package org.bekoocinema.controller;

import lombok.RequiredArgsConstructor;
import org.bekoocinema.response.ApiResponse;
import org.bekoocinema.service.GenreService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/genre")
public class GenreController {

    final GenreService genreService;

    @PostMapping("/{genreName}")
    public ApiResponse addGenre(@PathVariable String genreName) {
        genreService.addGenre(genreName);
        return ApiResponse.success(201, "Đã thêm thành công");
    }

    @DeleteMapping("/{genreId}")
    public ApiResponse deleteGenre(@PathVariable String genreId) {
        genreService.deleteGenre(genreId);
        return ApiResponse.success(200, "Đã xoá thể loại phim này");
    }
}
