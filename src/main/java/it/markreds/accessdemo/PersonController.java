package it.markreds.accessdemo;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class PersonController {
    private final PersonRepository repository;

    PersonController(PersonRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/v1/people")
    List<Person> all() {
        return repository.findAll();
    }

    @PostMapping("/api/v1/people")
    Person newPerson(@RequestBody Person person) {
        return repository.save(person);
    }

    @GetMapping("/api/v1/people/{id}")
    Person one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PersonNotFoundException(id));
    }

    @PutMapping("/api/v1/people/{id}")
    Person replacePerson(@RequestBody Person newPerson, @PathVariable Long id) {
        return repository.findById(id)
                .map(person -> {
                    person.setFirstName(newPerson.getFirstName());
                    person.setLastName(newPerson.getLastName());
                    person.setKeyCode(newPerson.getKeyCode());
                    person.setEnabled(newPerson.isEnabled());
                    return repository.save(person);
                })
                .orElseGet(() -> {
                    newPerson.setId(id);
                    return repository.save(newPerson);
                });
    }

    @DeleteMapping("/api/v1/people/{id}")
    void deletePerson(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
