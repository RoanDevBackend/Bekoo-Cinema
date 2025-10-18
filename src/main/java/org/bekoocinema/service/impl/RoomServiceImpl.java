package org.bekoocinema.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bekoocinema.entity.Cinema;
import org.bekoocinema.entity.Room;
import org.bekoocinema.entity.Seat;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.exception.ErrorDetail;
import org.bekoocinema.mapper.RoomMapper;
import org.bekoocinema.mapper.SeatMapper;
import org.bekoocinema.repository.CinemaRepository;
import org.bekoocinema.repository.RoomRepository;
import org.bekoocinema.repository.SeatRepository;
import org.bekoocinema.request.room.CreateRoomRequest;
import org.bekoocinema.request.room.CreateSeatRequest;
import org.bekoocinema.response.room.RoomResponse;
import org.bekoocinema.response.room.SeatResponse;
import org.bekoocinema.service.RoomService;
import org.springframework.stereotype.Service;

import java.text.MessageFormat;
import java.util.*;

@Service
@RequiredArgsConstructor
public class RoomServiceImpl implements RoomService {

    final RoomRepository roomRepository;
    final SeatRepository seatRepository;
    final CinemaRepository cinemaRepository;
    final SeatMapper seatMapper;
    final RoomMapper roomMapper;

    @Override
    @SneakyThrows
    @Transactional
    public void newRoom(CreateRoomRequest createRequest) {
        Cinema cinema = cinemaRepository.findById(createRequest.getCinemaId())
                .orElseThrow(() -> new AppException(ErrorDetail.ERR_CINEMA_NOT_EXISTED));
        int totalRow = createRequest.getTotalRow();
        int totalCol = createRequest.getTotalCol();
        Room room = new Room();
        room.setName(createRequest.getName());
        room.setTotalCol(totalCol);
        room.setTotalRow(totalRow);
        room.setCinema(cinema);
        roomRepository.save(room);
        Set<String> seatsExisted = new HashSet<>();
        for(CreateSeatRequest seatItemRequest : createRequest.getSeats()) {
            int seatItemRowIdx = seatItemRequest.getRowIdx();
            int seatItemColIdx = seatItemRequest.getColIdx();
            String key = seatItemRowIdx + "-" + seatItemColIdx;
            if(!seatsExisted.contains(key)) {
                if(seatItemRowIdx > totalRow || seatItemColIdx > totalCol) {
                    throw new RuntimeException(MessageFormat.format("Vị trí ghế không hợp lệ, hàng {0} và cột {1}", seatItemRowIdx, seatItemColIdx));
                }
                Seat seat = seatMapper.toSeat(seatItemRequest);
                seat.setRoom(room);
                seatRepository.save(seat);
                seatsExisted.add(key);
            }else {
                throw new RuntimeException(MessageFormat.format("Một vị trí chỉ được một ghế, trùng ghế tại hàng {0} và cột {1}", seatItemRowIdx, seatItemColIdx));
            }
        }
    }

    @Override
    @Transactional
    public RoomResponse getRoomById(String id) {
        Room room = roomRepository.findById(id).orElseThrow(
                () -> new RuntimeException("Phòng không tồn tại")
        );
        RoomResponse roomResponse = roomMapper.toResponse(room);
        List<SeatResponse> seatResponses = new ArrayList<>();
        for(Seat seat : room.getSeats()) {
            SeatResponse seatResponse = seatMapper.toResponse(seat);
            seatResponses.add(seatResponse);
        }
        roomResponse.setSeats(seatResponses);
        return roomResponse;
    }

    @Override
    public List<String> getRoomByCinema(String cinemaId) {
        return roomRepository.getRoomByCinema(cinemaId);
    }
}
