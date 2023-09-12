package it.markreds.accessdemo;

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

//    @Bean
//    public CommandLineRunner loadUsers(PersonRepository repository) {
//        return (args -> {
//            LOG.info("Preloading " + repository.save(new Person("Marco", "Rossi")));
//            LOG.info("Preloading " + repository.save(new Person("Elisa", "Neri")));
//        });
//    }

//    @Bean
//    public CommandLineRunner loadDoors(DoorRepository repository) {
//        return (args -> {
//            LOG.info("Preloading " + repository.save(new Door("Ingresso principale", "00AAA")));
//            LOG.info("Preloading " + repository.save(new Door("Serranda", "DD240930")));
//        });
//    }

}
