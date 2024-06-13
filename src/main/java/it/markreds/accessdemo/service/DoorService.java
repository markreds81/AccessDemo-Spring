package it.markreds.accessdemo.service;

import it.markreds.accessdemo.domain.Door;
import it.markreds.accessdemo.domain.EventLog;
import it.markreds.accessdemo.domain.Permission;
import it.markreds.accessdemo.domain.Person;
import it.markreds.accessdemo.exception.DoorNotFoundException;
import it.markreds.accessdemo.exception.PersonNotFoundException;
import it.markreds.accessdemo.repository.DoorRepository;
import it.markreds.accessdemo.repository.EventLogRepository;
import it.markreds.accessdemo.repository.PersonRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class DoorService {
    private final AtomicLong counter = new AtomicLong();
    private final DoorRepository doorRepo;
    private final PersonRepository personRepo;
    private final EventLogRepository eventRepo;

    public DoorService(DoorRepository doorRepo, PersonRepository personRepo, EventLogRepository eventRepo) {
        this.doorRepo = doorRepo;
        this.personRepo = personRepo;
        this.eventRepo = eventRepo;
    }

    public List<Door> findAll() {
        return doorRepo.findAll();
    }

    public Door findOne(Long id) {
        return doorRepo.findById(id).orElseThrow(() -> new DoorNotFoundException(id));
    }

    public Door create(Door door) {
        return doorRepo.save(door);
    }

    public Door upsert(Door door, Long id) {
        return doorRepo.findById(id)
                .map(target -> {
                    target.setDisplayName(door.getDisplayName());
                    target.setMacAddress(door.getMacAddress());
                    target.setWorkTime(door.getWorkTime());
                    return doorRepo.save(target);
                })
                .orElseGet(() -> {
                    door.setId(id);
                    return doorRepo.save(door);
                });
    }

    public void delete(Long id) {
        doorRepo.deleteById(id);
    }

    public Permission openingRequest(String macAddress, String keyCode) {
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

    public EventLog stateChanged(String macAddress, int state) {
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
