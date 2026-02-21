package site.secmega.secapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SecApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecApiApplication.class, args);
    }

}
