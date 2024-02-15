package it.markreds.accessdemo.controller;

import it.markreds.accessdemo.domain.Door;
import it.markreds.accessdemo.domain.EventLog;
import it.markreds.accessdemo.domain.Person;
import it.markreds.accessdemo.exception.DoorNotFoundException;
import it.markreds.accessdemo.exception.PersonNotFoundException;
import it.markreds.accessdemo.repository.DoorRepository;
import it.markreds.accessdemo.repository.EventLogRepository;
import it.markreds.accessdemo.repository.PersonRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

@RestController
public class MainController {
    private final AtomicLong counter = new AtomicLong();
    private final DoorRepository doorRepo;
    private final PersonRepository personRepo;
    private final EventLogRepository eventRepo;

    public record Permission(long id, Date timestamp, boolean allowed, int workTime) { }

    public MainController(DoorRepository doorRepo, PersonRepository personRepo, EventLogRepository eventRepo) {
        this.doorRepo = doorRepo;
        this.personRepo = personRepo;
        this.eventRepo = eventRepo;
    }

    @GetMapping("/api/v1/can-open")
    public Permission openingRequest(
            @RequestParam(value = "door") String macAddress,
            @RequestParam(value = "keycode") String keyCode) {
        Door door = doorRepo.findByMacAddressEquals(macAddress);
        if (door == null) {
            eventRepo.save(EventLog.doorNotFound());
            throw new DoorNotFoundException(macAddress);
        }

        Person person = personRepo.findByKeyCodeEquals(keyCode);
        if (person == null) {
            eventRepo.save(EventLog.userNotFound(door));
            throw new PersonNotFoundException(keyCode);
        }

        eventRepo.save(EventLog.userLogin(door, person));

        if (!person.isEnabled()) {
            eventRepo.save(EventLog.userNotGranted(door, person));
        } else {
            eventRepo.save(EventLog.doorOpening(door, person));
        }

        return new Permission(counter.incrementAndGet(), new Date(), person.isEnabled(), door.getWorkTime());
    }

    @GetMapping("/api/v1/state-changed")
    public EventLog stateChanged(
            @RequestParam(value = "door") String macAddress,
            @RequestParam(value = "open") int state) {
        Door door = doorRepo.findByMacAddressEquals(macAddress);
        if (door == null) {
            eventRepo.save(EventLog.doorNotFound());
            throw new DoorNotFoundException(macAddress);
        }

        door.setOpen(state > 0);
        doorRepo.save(door);

        EventLog eventLog = new EventLog(new Date(), door.isOpen() ? EventLog.ItemType.DOOR_OPEN : EventLog.ItemType.DOOR_CLOSE, door);
        eventRepo.save(eventLog);

        return eventLog;
    }
}
