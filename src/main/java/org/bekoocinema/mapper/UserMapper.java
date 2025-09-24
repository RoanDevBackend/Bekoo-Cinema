package org.bekoocinema.mapper;

import org.bekoocinema.entity.User;
import org.bekoocinema.request.auth.RegisterUserRequest;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public abstract class UserMapper {

    public abstract User toUser(RegisterUserRequest registerUserRequest);
}
