package it.markreds.accessdemo;

class PersonNotFoundException extends RuntimeException {

    PersonNotFoundException(Long id) {
        super("Could not find person by id " + id);
    }

    PersonNotFoundException(String keyCode) {
        super("Could not find person by keyCode " + keyCode);
    }
}
