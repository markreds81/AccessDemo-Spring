package it.markreds.accessdemo.exception;

public class DoorNotFoundException extends RuntimeException {

    public DoorNotFoundException(Long id) {
        super("Could not find door by id " + id);
    }

    public DoorNotFoundException(String macAddress) {
        super("Could not find door by mac address " + macAddress);
    }
}
