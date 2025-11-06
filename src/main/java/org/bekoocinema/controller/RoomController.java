package org.bekoocinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.request.room.CreateRoomRequest;
import org.bekoocinema.response.ApiResponse;
import org.bekoocinema.service.RoomService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class RoomController {

    private final RoomService roomService;

    @PostMapping("/room")
    public ApiResponse createRoom(@RequestBody @Valid CreateRoomRequest createRoomRequest) {
        roomService.newRoom(createRoomRequest);
        return ApiResponse.success(201, "Thêm thành công");
    }

    @GetMapping("/public-api/room/cinema/{cinemaId}")
    public ApiResponse getAllRoomByCinema(@PathVariable String cinemaId) {
        return ApiResponse.success(200, "Bạn đã lấy danh sách mã phòng chiếu", roomService.getRoomByCinema(cinemaId));
    }

    @GetMapping("/public-api/room/cinema/{cinemaId}/simple")
    public ApiResponse getAllRoomByCinemaSimple(@PathVariable String cinemaId) {
        return ApiResponse.success(200, "Bạn đã lấy danh sách mã và tên phòng chiếu", roomService.getRoomByCinemaSimple(cinemaId));
    }

    @Operation(summary = "Lấy chi tiết phòng chiếu")
    @GetMapping("/public-api/room/{roomId}")
    public ApiResponse getRoomById(@PathVariable String roomId) {
        return ApiResponse.success(200, "Lấy chi tiết phòng chiếu thành công", roomService.getRoom(roomId));
    }
}
