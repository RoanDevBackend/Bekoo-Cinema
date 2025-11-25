package org.bekoocinema.request.room;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateShowtimeRequest {
    @NotBlank(message = "Room ID không được để trống")
    String roomId;

    @NotBlank(message = "Movie ID không được để trống")
    String movieId;

    @NotBlank(message = "Thời gian bắt đầu không được để trống")
    @Schema(example = "2025-11-26T11:00:00")
    String startTime;

    @NotBlank(message = "Thời gian kết thúc không được để trống")
    @Schema(example = "2026-11-26T14:00:00")
    String endTime;
}
