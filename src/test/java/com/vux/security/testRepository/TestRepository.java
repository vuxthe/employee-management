package com.vux.security.testRepository;

import com.vux.security.entity.User;
import com.vux.security.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;


@ExtendWith(SpringExtension.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TestRepository {

    @Autowired
    UserRepository userRepository;

    @Test
    public void saveAndReturn() {
        User user = User.builder()
                .name("user123")
                .email("mail@mail.com")
                .password("1234")
                .build();

        userRepository.save(user);
        Optional<User> savedUser = userRepository.findByEmail(user.getEmail());
        savedUser.ifPresent(value -> Assertions.assertThat(value.getName()).isEqualTo(user.getName()));
    }
}
