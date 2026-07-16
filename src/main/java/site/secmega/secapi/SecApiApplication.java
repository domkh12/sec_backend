package site.secmega.secapi;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.TimeZone;

@EnableSpringDataWebSupport(pageSerializationMode = EnableSpringDataWebSupport.PageSerializationMode.VIA_DTO)
@SpringBootApplication
@EnableScheduling
public class SecApiApplication {

    @PostConstruct
    public void init(){
        TimeZone.setDefault(TimeZone.getTimeZone("Asia/Phnom_Penh"));
    }

    public static void main(String[] args) {

        SpringApplication.run(SecApiApplication.class, args);
    }

}
