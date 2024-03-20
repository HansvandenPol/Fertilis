package nl.fertilis.fertilis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class FertilisApplication {

  public static void main(String[] args) {
    SpringApplication.run(FertilisApplication.class, args);
  }
}
