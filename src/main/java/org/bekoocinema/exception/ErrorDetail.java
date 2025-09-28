package org.bekoocinema.exception;

import lombok.Getter;

@Getter
public enum ErrorDetail {
    ERR_USER_EMAIL_EXISTED(400, "Email đã tồn tại")
    , ERR_USER_UN_AUTHENTICATE(401, "Sai tên đăng nhập hoặc mật khẩu")
    , ERR_USER_NOT_EXISTED(404, "Người dùng không tồn tại")
    , ERR_GENRE_EXISTED(400, "Thể loại đã tồn tại")
    , ERR_CATEGORY_NOT_EXISTED(400, "Thể loại không tồn tại")
    , ERR_WHILE_UPLOAD(400, "Có lỗi trong khi upload ảnh")
    , ERR_ORDER_USER_NOT_CORRECT(400, "Thao tác này chỉ được thực hiện với dữ liệu của bạn")
    , ERR_IMAGE_NOT_EXISTED(400, "Không tìm thấy thông tin ảnh")
    ;

    private final int code;
    private final String message;

    ErrorDetail(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
