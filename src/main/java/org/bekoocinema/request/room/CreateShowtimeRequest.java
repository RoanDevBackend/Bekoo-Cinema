package org.bekoocinema.request.room;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateShowtimeRequest {
    @Schema(example = "1")
    String roomId;
    @Schema(example = "1")
    String movieId;
    @Schema(example = "2025-11-06T11:00:00")
    String startTime;
    @Schema(example = "2026-11-06T11:00:00")
    String endTime;
}
