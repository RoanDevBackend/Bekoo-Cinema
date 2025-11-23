package org.bekoocinema;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.bekoocinema.entity.User;
import org.bekoocinema.mapper.UserMapper;
import org.bekoocinema.repository.UserRepository;
import org.bekoocinema.request.auth.RegisterUserRequest;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import org.springframework.core.env.Environment;

@Log4j2
@SpringBootApplication(exclude = {ElasticsearchRestClientAutoConfiguration.class})
@RequiredArgsConstructor
@EnableScheduling
public class BekooCinemaApplication implements CommandLineRunner {

    public static void main(String[] args) {
        Environment env = SpringApplication.run(BekooCinemaApplication.class, args).getEnvironment();
        String appName = env.getProperty("spring.application.name");
        if (appName != null) {
            appName = appName.toUpperCase();
        }
        String url = env.getProperty("spring.url");
        log.info("-------------------------START {} Application------------------------------", appName);
        log.info("   Application         : {}", appName);
        log.info("   Url swagger-ui      : {}/swagger-ui.html", url);
        log.info("-------------------------START SUCCESS {} Application------------------------------", appName);
    }

    final UserRepository userRepository;
    final EntityManager entityManager;
    final PasswordEncoder passwordEncoder;
    final UserMapper userMapper;

    @Override
    @SneakyThrows
    @Transactional
    public void run(String... args) {
        if(!userRepository.existsByEmail("bekoo.cinema@gmail.com")) {
            RegisterUserRequest registerRequest = new RegisterUserRequest();
            registerRequest.setEmail("bekoo.cinema@gmail.com");
            registerRequest.setFirstName("Bekoo Cinema");
            registerRequest.setLastName("Admin");
            String password = passwordEncoder.encode("Admin@1234");
            registerRequest.setPassword(password);
            registerRequest.setPhone("Undefined");
            User user = userMapper.toUser(registerRequest);
            user.setRole("ROLE_ADMIN");
            userRepository.save(user);
        }
        Search.session(entityManager)
                .massIndexer()
                .startAndWait();
    }

}
