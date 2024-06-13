package it.markreds.accessdemo.controller;

import it.markreds.accessdemo.domain.Person;
import it.markreds.accessdemo.exception.PersonNotFoundException;
import it.markreds.accessdemo.repository.PersonRepository;
import it.markreds.accessdemo.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class PersonController {
    private final PersonService service;

    PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping("/api/v1/people")
    List<Person> all() {
        return service.findAll();
    }

    @PostMapping("/api/v1/people")
    Person newPerson(@RequestBody Person person) {
        return service.create(person);
    }

    @GetMapping("/api/v1/people/{id}")
    Person one(@PathVariable Long id) {
        return service.findOne(id);
    }

    @PutMapping("/api/v1/people/{id}")
    Person replacePerson(@RequestBody Person person, @PathVariable Long id) {
        return service.upsert(person, id);
    }

    @DeleteMapping("/api/v1/people/{id}")
    void deletePerson(@PathVariable Long id) {
        service.delete(id);
    }
}
