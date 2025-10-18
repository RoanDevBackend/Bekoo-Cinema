package org.bekoocinema.mapper;

import org.bekoocinema.entity.Seat;
import org.bekoocinema.request.room.CreateSeatRequest;
import org.bekoocinema.response.room.SeatResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class SeatMapper {
    public abstract Seat toSeat(CreateSeatRequest createSeatRequest);
    @Mapping(target = "seatId", source = "id")
    public abstract SeatResponse toResponse(Seat seat);
}
