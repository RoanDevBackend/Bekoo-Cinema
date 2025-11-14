package org.bekoocinema.service;

import org.bekoocinema.entity.User;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.request.auth.SignInRequest;
import org.bekoocinema.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse getOtpSignIn(SignInRequest signInRequest);
    TokenResponse verifyOtpSignIn(String OTP, User user);
    void getOtpForgotPassword(String email);
    void verifyOtpForgotPassword(String OTP, String email) throws AppException;
    void changePasswordNoAuth(String newPassword, String email)
        throws AppException;
    void logout(String accessToken, String refreshToken);
    //    void getOtp(String mail) throws BeautyBoxException, MessagingException, UnsupportedEncodingException;
    //    void verifyOtp(String mail, String otp);
    //    void changePassword(ChangePassword changePassword, User user) throws BeautyBoxException;
}
