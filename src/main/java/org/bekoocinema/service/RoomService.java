package org.bekoocinema.service;

import org.bekoocinema.request.room.CreateRoomRequest;
import org.bekoocinema.response.room.RoomResponse;
import org.bekoocinema.response.room.RoomSimpleResponse;

import java.util.List;

public interface RoomService {
    void newRoom(CreateRoomRequest createRequest);
    List<RoomResponse> getRoomByCinema(String cinemaId);
    List<RoomSimpleResponse> getRoomByCinemaSimple(String cinemaId);
    RoomResponse getRoom(String id);
}
