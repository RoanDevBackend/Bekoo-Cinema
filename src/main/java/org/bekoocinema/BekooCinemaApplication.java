package org.bekoocinema;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.bekoocinema.entity.User;
import org.bekoocinema.mapper.UserMapper;
import org.bekoocinema.repository.UserRepository;
import org.bekoocinema.request.auth.RegisterUserRequest;
import org.hibernate.search.mapper.orm.Search;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchRestClientAutoConfiguration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@SpringBootApplication(exclude = {ElasticsearchRestClientAutoConfiguration.class})
@RequiredArgsConstructor
public class BekooCinemaApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(BekooCinemaApplication.class, args);
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
