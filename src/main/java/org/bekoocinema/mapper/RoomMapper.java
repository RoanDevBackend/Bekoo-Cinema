package org.bekoocinema.mapper;

import org.bekoocinema.entity.Room;
import org.bekoocinema.response.room.RoomResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public abstract class RoomMapper {

    @Mapping(target = "roomId", source = "id")
    public abstract RoomResponse toResponse(Room room);
}
