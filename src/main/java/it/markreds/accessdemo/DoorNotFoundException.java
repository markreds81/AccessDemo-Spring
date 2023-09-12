package it.markreds.accessdemo;

class DoorNotFoundException extends RuntimeException {

    DoorNotFoundException(Long id) {
        super("Could not find door by id " + id);
    }

    DoorNotFoundException(String macAddress) {
        super("Could not find door by mac address " + macAddress);
    }
}
