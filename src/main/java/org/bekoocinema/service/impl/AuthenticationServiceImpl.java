package org.bekoocinema.service.impl;

import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bekoocinema.entity.User;
import org.bekoocinema.exception.AppException;
import org.bekoocinema.exception.ErrorDetail;
import org.bekoocinema.repository.RedisRepository;
import org.bekoocinema.repository.UserRepository;
import org.bekoocinema.request.auth.SignInRequest;
import org.bekoocinema.response.TokenResponse;
import org.bekoocinema.service.AuthenticationService;
import org.bekoocinema.service.JwtService;
import org.bekoocinema.service.MailService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.UnsupportedEncodingException;
import java.sql.Timestamp;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {

    final UserRepository userRepository;
    final JwtService jwtService;
    final AuthenticationManager authenticationManager;
    final SpringTemplateEngine templateEngine;
    final MailService mailService;
    final RedisRepository redisRepository;

    @Override
    @SneakyThrows
    public TokenResponse getOtpSignIn(SignInRequest signInRequest) {
        User user = userRepository.findByUserName(signInRequest.getEmail());
        if (user == null)
        {
            throw new AppException(ErrorDetail.ERR_USER_UN_AUTHENTICATE);
        }
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    signInRequest.getEmail(),
                    signInRequest.getPassword()
            ));
        }catch (Exception e){
            throw new AppException(ErrorDetail.ERR_USER_UN_AUTHENTICATE);
        }
        this.getOtp(user.getEmail());
        final long TIME_TOKEN = 1000L * 60 * 2; // 2m
        var tokenContent = jwtService.generateToken(user, TIME_TOKEN);
        return TokenResponse.builder()
                .tokenContent(tokenContent)
                .expToken(new Timestamp(System.currentTimeMillis() + TIME_TOKEN))
                .build();
    }

    @Override
    public TokenResponse verifyOtpSignIn(String OTP, User user) {
        this.verifyOtp(user.getEmail(), OTP);
        final long TIME_TOKEN = 1000L * 60 * 60 * 24;
        var tokenContent = jwtService.generateToken(user, TIME_TOKEN);
        var refreshToken = jwtService.generateToken(user, TIME_TOKEN * 7);

        return TokenResponse.builder()
                .tokenContent(tokenContent)
                .refreshToken(refreshToken)
                .userId(user.getId())
                .userName(user.getEmail())
                .roleName(user.getRole())
                .expToken(new Timestamp(System.currentTimeMillis() + TIME_TOKEN))
                .expRefreshToken(new Timestamp(System.currentTimeMillis() + TIME_TOKEN * 2))
                .build();
    }

    private void getOtp(String mail) throws AppException, MessagingException, UnsupportedEncodingException {
        User user= userRepository.findByUserName(mail);
        if(user==null){
            throw new AppException(ErrorDetail.ERR_USER_NOT_EXISTED);
        }
        Random random= new Random();
        int code= random.nextInt(100000, 999999);

        Context context= new Context();
        context.setVariable("code", code);
        context.setVariable("name", user.getFirstName() + " " + user.getLastName());
        String content= templateEngine.process("mail-otp", context);
        String subject= "Mã xác thực tài khoản";
        mailService.sendMail(subject,mail, content, true);
        redisRepository.set(user.getEmail(), code);
        redisRepository.setTimeToLive(user.getEmail(),  60L * 2 * 1000); //2m
    }

    private void verifyOtp(String mail, String otp) {
        Object code = redisRepository.get(mail);
        if(code == null){
            throw new RuntimeException("OTP không chính xác");
        }
        if(otp == null || !otp.equals(code.toString())){
            throw new RuntimeException("OTP không chính xác");
        }
        redisRepository.set(mail, "verified");
        redisRepository.setTimeToLive(mail, 5 * 60L * 1000);
    }
}
