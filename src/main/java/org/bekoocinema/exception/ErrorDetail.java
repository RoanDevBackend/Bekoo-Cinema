package org.bekoocinema.exception;

import lombok.Getter;

@Getter
public enum ErrorDetail {
    ERR_USER_EMAIL_EXISTED(400, "Email đã tồn tại")
    , ERR_USER_UN_AUTHENTICATE(401, "Sai tên đăng nhập hoặc mật khẩu")
    , ERR_USER_SESSION_EXPIRED(401, "Phiên xác thực đã hết hạn.")
    , ERR_USER_NOT_EXISTED(404, "Người dùng không tồn tại")
    , ERR_PASSWORD_NOT_MATCH(400, "Mật khẩu xác nhận không khớp")
    , ERR_GENRE_EXISTED(400, "Thể loại đã tồn tại")
    , ERR_GENRE_NOT_EXISTED(400, "Thể loại không tồn tại")
    , ERR_WHILE_UPLOAD(400, "Có lỗi trong khi upload ảnh")
    , ERR_ORDER_USER_NOT_CORRECT(400, "Thao tác này chỉ được thực hiện với dữ liệu của bạn")
    , ERR_IMAGE_NOT_EXISTED(400, "Không tìm thấy thông tin ảnh")
    , ERR_CINEMA_NOT_EXISTED(400, "Rạp phim không tồn tại")
    , ERR_ROOM_NOT_EXISTED(400, "Phòng chiếu không tồn tại")
    , ERR_MOVIE_NOT_EXISTED(400, "Bộ phim không tồn tại")
    , ERR_SHOWTIME_CONFLICT(409, "Phòng chiếu đã có lịch chiếu khác trong khung giờ này")
    , ERR_SHOWTIME_NOT_EXISTED(400, "Xuất chiếu không tồn tại")
    , ERR_SHOWTIME_HAS_BOOKINGS(400, "Không thể xóa xuất chiếu vì đã có người đặt vé")
    , ERR_BOOKING_NOT_EXISTED(404, "Mã vé đặt không tồn tại")
    , ERR_ORDER_TIME_VALID(400, "Đơn hàng đã qúa thời gian thanh toán")
    , ERR_OLD_PASSWORD_INCORRECT(400, "Mật khẩu cũ không chính xác")
    , ERR_NEW_PASSWORD_SAME_AS_OLD(400, "Mật khẩu mới phải khác mật khẩu cũ")
    , ERR_COMMENT_PARENT_NOT_EXISTED(400, "Bình luận bạn trả lời hiện không tồn tại")
    , ERR_SEAT_BOOKED(400, "Ghế đã được đặt")
    , ERR_INVALID_TIME(400, "Thời gian không hợp lệ")
    ;

    private final int code;
    private final String message;

    ErrorDetail(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
