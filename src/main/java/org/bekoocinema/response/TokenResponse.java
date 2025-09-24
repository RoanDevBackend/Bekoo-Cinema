package org.bekoocinema.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import java.sql.Timestamp;


@Builder
@Setter
@Getter
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TokenResponse {
    String tokenContent ;
    String refreshToken ;
    String userId ;
    String userName ;
    String roleName ;
    Timestamp expToken ;
    Timestamp expRefreshToken ;
}
