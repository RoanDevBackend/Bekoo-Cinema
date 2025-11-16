package org.bekoocinema.request.comment;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class NewCommentRequest {
    String content;
    String movieId;
    String parentCommentId;
}
