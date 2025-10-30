package org.bekoocinema.mapper;

import org.bekoocinema.entity.Booking;
import org.bekoocinema.response.booking.BookingResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class BookingMapper {
    public abstract BookingResponse toResponse(Booking booking);
}
