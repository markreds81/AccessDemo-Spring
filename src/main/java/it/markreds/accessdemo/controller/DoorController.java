package it.markreds.accessdemo.controller;

import it.markreds.accessdemo.domain.Door;
import it.markreds.accessdemo.domain.EventLog;
import it.markreds.accessdemo.domain.Permission;
import it.markreds.accessdemo.service.DoorService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/doors")
public class DoorController {
    private static final String TEMPLATE = "Door %s";

    private final DoorService service;

    public DoorController(DoorService service) {
        this.service = service;
    }

    @GetMapping()
    public List<Door> all() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public Door one(@PathVariable Long id) {
        return service.findOne(id);
    }

    @PostMapping()
    public Door create(@RequestBody Door door) {
        return service.create(door);
    }

    @PutMapping("/{id}")
    public Door update(@RequestBody Door door, @PathVariable Long id) {
        return service.upsert(door, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/can-open")
    public Permission openingRequest(
            @RequestParam(value = "door") String macAddress,
            @RequestParam(value = "keycode") String keyCode) {
        return service.openingRequest(macAddress, keyCode);
    }

    @GetMapping("/state-changed")
    public EventLog stateChanged(
            @RequestParam(value = "door") String macAddress,
            @RequestParam(value = "open") int state) {
        return service.stateChanged(macAddress, state);
    }
}
