package org.bekoocinema.controller;

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
}
