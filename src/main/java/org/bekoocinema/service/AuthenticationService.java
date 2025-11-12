package org.bekoocinema.service;

import org.bekoocinema.entity.User;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.request.auth.SignInRequest;
import org.bekoocinema.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse getOtpSignIn(SignInRequest signInRequest);
    TokenResponse verifyOtp(String OTP, User user);
    TokenResponse getOtpForgotPassword(String email);
    void changePasswordNoAuth(String newPassword, User user) throws AppException;
//    void logout(String token);
//    void getOtp(String mail) throws BeautyBoxException, MessagingException, UnsupportedEncodingException;
//    void verifyOtp(String mail, String otp);
//    void changePassword(ChangePassword changePassword, User user) throws BeautyBoxException;
}
