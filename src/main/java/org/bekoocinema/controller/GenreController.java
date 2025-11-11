package org.bekoocinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.constant.EndPointConstant;
import org.bekoocinema.request.genre.UpdateGenreRequest;
import org.bekoocinema.response.ApiResponse;
import org.bekoocinema.service.GenreService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class GenreController {

    final GenreService genreService;

    @PostMapping("/genre/{genreName}")
    public ApiResponse addGenre(@PathVariable String genreName) {
        genreService.addGenre(genreName);
        return ApiResponse.success(201, "Đã thêm thành công");
    }

    @Operation(
        summary = "Cập nhật thể loại phim",
        security = { @SecurityRequirement(name = "bearerAuth") }
    )
    @PutMapping("/genre/{genreId}")
    public ApiResponse updateGenre(@PathVariable String genreId, @RequestBody @Valid UpdateGenreRequest request) {
        genreService.updateGenre(genreId, request.getName());
        return ApiResponse.success(200, "Đã cập nhật thể loại phim thành công");
    }

    @GetMapping(EndPointConstant.PUBLIC + "/genre")
    public ApiResponse getAllGenres(@RequestParam(required = false, defaultValue = "") String genreName) {
        return ApiResponse.success(200, "Tìm kiếm thành công", genreService.getAllGenres(genreName));
    }

    @DeleteMapping("/genre/{genreId}")
    public ApiResponse deleteGenre(@PathVariable String genreId) {
        genreService.deleteGenre(genreId);
        return ApiResponse.success(200, "Đã xoá thể loại phim này");
    }
}
