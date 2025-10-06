package org.bekoocinema.mapper;

import org.bekoocinema.entity.Cinema;
import org.bekoocinema.request.cinema.CreateCinemaRequest;
import org.bekoocinema.response.cinema.CinemaResponse;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class CinemaMapper {
    public abstract Cinema toCinema(CreateCinemaRequest createCinemaRequest);
    public abstract CinemaResponse toCinemaResponse(Cinema cinema);
}
