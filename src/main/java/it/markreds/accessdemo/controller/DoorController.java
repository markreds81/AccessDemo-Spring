package it.markreds.accessdemo.controller;

import it.markreds.accessdemo.domain.Door;
import it.markreds.accessdemo.exception.DoorNotFoundException;
import it.markreds.accessdemo.repository.DoorRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
class DoorController {
    private static final String TEMPLATE = "Door %s";
    private final DoorRepository repository;

    DoorController(DoorRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/api/v1/doors")
    List<Door> all() {
        return repository.findAll();
    }

    @PostMapping("/api/v1/doors")
    Door newDoor(@RequestBody Door door) {
        return repository.save(door);
    }

    @GetMapping("/api/v1/doors/{id}")
    Door one(@PathVariable Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new DoorNotFoundException(id));
    }

    @PutMapping("/api/v1/doors/{id}")
    Door replaceDoor(@RequestBody Door newDoor, @PathVariable Long id) {
        return repository.findById(id)
                .map(door -> {
                    door.setDisplayName(newDoor.getDisplayName());
                    door.setMacAddress(newDoor.getMacAddress());
                    door.setWorkTime(newDoor.getWorkTime());
                    return repository.save(door);
                })
                .orElseGet(() -> {
                    newDoor.setId(id);
                    return repository.save(newDoor);
                });
    }

    @DeleteMapping("/api/v1/doors/{id}")
    void deleteDoor(@PathVariable Long id) {
        repository.deleteById(id);
    }
}
