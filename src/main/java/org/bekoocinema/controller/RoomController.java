package org.bekoocinema.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.bekoocinema.constant.EndPointConstant;
import org.bekoocinema.request.room.CreateRoomRequest;
import org.bekoocinema.request.room.UpdateRoomRequest;
import org.bekoocinema.response.ApiResponse;
import org.bekoocinema.response.room.RoomResponse;
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

    @Operation(
        summary = "Cập nhật phòng chiếu",
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
    )
    @PutMapping("/room/{roomId}")
    public ApiResponse updateRoom(@PathVariable String roomId, @RequestBody @Valid UpdateRoomRequest updateRoomRequest) {
        roomService.updateRoom(roomId, updateRoomRequest);
        return ApiResponse.success(200, "Cập nhật phòng chiếu thành công");
    }

    @Operation(
        summary = "Xóa phòng chiếu",
        security = {
                @SecurityRequirement(name = "bearerAuth")
        }
    )
    @DeleteMapping("/room/{roomId}")
    public ApiResponse deleteRoom(@PathVariable String roomId) {
        roomService.deleteRoom(roomId);
        return ApiResponse.success(200, "Xóa phòng chiếu thành công");
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
        RoomResponse room = roomService.getRoom(roomId);
        if(room == null) {
            return ApiResponse.success(200, "Không tìm thấy phòng chiếu", null);
        }
        return ApiResponse.success(200, "Lấy chi tiết phòng chiếu thành công", room);
    }
}
