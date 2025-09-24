package org.bekoocinema.service;

import org.bekoocinema.entity.User;
import org.bekoocinema.request.auth.SignInRequest;
import org.bekoocinema.response.TokenResponse;

public interface AuthenticationService {
    TokenResponse getOtpSignIn(SignInRequest signInRequest);
    TokenResponse verifyOtpSignIn(String OTP, User user);
//    void logout(String token);
//    void getOtp(String mail) throws BeautyBoxException, MessagingException, UnsupportedEncodingException;
//    void verifyOtp(String mail, String otp);
//    void changePasswordNoAuth(ChangePasswordNoAuth changePasswordNoAuth) throws BeautyBoxException;
//    void changePassword(ChangePassword changePassword, User user) throws BeautyBoxException;
}
