package it.markreds.accessdemo.service;

import it.markreds.accessdemo.domain.Person;
import it.markreds.accessdemo.exception.PersonNotFoundException;
import it.markreds.accessdemo.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PersonService {
    private final PersonRepository repository;

    public PersonService(PersonRepository repository) {
        this.repository = repository;
    }

    public List<Person> findAll() {
        return repository.findAll();
    }

    public Person findOne(Long id) {
        return repository.findById(id).orElseThrow(() -> new PersonNotFoundException(id));
    }

    public Person create(Person person) {
        return repository.save(person);
    }

    public Person upsert(Person person, Long id) {
        return repository.findById(id)
                .map(target -> {
                    target.setFirstName(person.getFirstName());
                    target.setLastName(person.getLastName());
                    target.setKeyCode(person.getKeyCode());
                    target.setEnabled(person.isEnabled());
                    return repository.save(target);
                })
                .orElseGet(() -> {
                    person.setId(id);
                    return repository.save(person);
                });
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
