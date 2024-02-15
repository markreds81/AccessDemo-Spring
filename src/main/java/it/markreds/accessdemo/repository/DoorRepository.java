package it.markreds.accessdemo.repository;

import it.markreds.accessdemo.domain.Door;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DoorRepository extends JpaRepository<Door, Long> {
    List<Door> findByDisplayNameStartsWithIgnoreCase(String lastName);
    Door findByMacAddressEquals(String macAddress);
}
