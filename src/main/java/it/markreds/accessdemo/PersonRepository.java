package it.markreds.accessdemo;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Long> {
    List<Person> findByFirstNameStartsWithIgnoreCaseOrLastNameStartsWithIgnoreCase(String firstName, String lastName);
    Person findByKeyCodeEquals(String keyCode);
}
