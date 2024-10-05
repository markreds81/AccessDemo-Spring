package it.markreds.accessdemo.controller;

import it.markreds.accessdemo.domain.Person;
import it.markreds.accessdemo.service.PersonService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/people")
class PersonController {
    private final PersonService service;

    PersonController(PersonService service) {
        this.service = service;
    }

    @GetMapping
    List<Person> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    Person one(@PathVariable Long id) {
        return service.findOne(id);
    }

    @PostMapping
    Person create(@RequestBody Person person) {
        return service.create(person);
    }

    @PutMapping("/{id}")
    Person update(@RequestBody Person person, @PathVariable Long id) {
        return service.upsert(person, id);
    }

    @DeleteMapping("/{id}")
    void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
