package za.co.invoke.solutions.naturalgasprices;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.security.SecureRandom;

@CrossOrigin(origins = "http://localhost:4200", allowedHeaders = "*")
@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        int strength = 10;
        return new BCryptPasswordEncoder(strength, new SecureRandom());
    }

}
