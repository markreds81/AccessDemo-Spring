package it.markreds.accessdemo.exception;

public class PersonNotFoundException extends RuntimeException {

    public PersonNotFoundException(Long id) {
        super("Could not find person by id " + id);
    }

    public PersonNotFoundException(String keyCode) {
        super("Could not find person by keyCode " + keyCode);
    }
}
