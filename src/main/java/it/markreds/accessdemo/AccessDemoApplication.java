package it.markreds.accessdemo;

import it.markreds.accessdemo.domain.Door;
import it.markreds.accessdemo.domain.Person;
import it.markreds.accessdemo.repository.DoorRepository;
import it.markreds.accessdemo.repository.PersonRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class AccessDemoApplication {
    private static final Logger LOG = LoggerFactory.getLogger(AccessDemoApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(AccessDemoApplication.class, args);
    }

    @Bean
    public CommandLineRunner populateDoors(DoorRepository repository) {
        return (args -> {
            if (repository.count() == 0) {
                LOG.info("Preloading " + repository.save(new Door("Ingresso principale", "B0BE8349D36E", 2)));
                LOG.info("Preloading " + repository.save(new Door("Serranda", "368ABCF07480", 5)));
            }
        });
    }
    @Bean
    public CommandLineRunner populateUsers(PersonRepository repository) {
        return (args -> {
            if (repository.count() == 0) {
                LOG.info("Preloading " + repository.save(new Person("Marco", "Rossi", "B177B700A2000000")));
                LOG.info("Preloading " + repository.save(new Person("Elisa", "Neri", "9340D7008F000000")));
            }
        });
    }

}
