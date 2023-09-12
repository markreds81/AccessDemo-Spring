package it.markreds.accessdemo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import java.util.*;

@Entity
public class EventLog {
    @Id
    @GeneratedValue
    private Long id;

    private Date timestamp;

    private ItemType itemType;

    @ManyToOne
    private Door door;

    @ManyToOne
    private Person person;

    public EventLog() { }

    public EventLog(Date timestamp, ItemType itemType) {
        this.timestamp = timestamp;
        this.itemType = itemType;
    }

    public EventLog(Date timestamp, ItemType itemType, Door door) {
        this.timestamp = timestamp;
        this.itemType = itemType;
        this.door = door;
    }

    public EventLog(Date timestamp, ItemType itemType, Door door, Person person) {
        this.timestamp = timestamp;
        this.itemType = itemType;
        this.door = door;
        this.person = person;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventLog eventLog = (EventLog) o;
        return Objects.equals(id, eventLog.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("EventLog [id = %d, itemType = %s]", id, itemType);
    }

    public Long getId() {
        return id;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public void setItemType(ItemType itemType) {
        this.itemType = itemType;
    }

    public boolean hasDoor() {
        return door != null;
    }

    public Door getDoor() {
        return door;
    }

    public void setDoor(Door door) {
        this.door = door;
    }

    public boolean hasPerson() {
        return person != null;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public enum ItemType {
        DOOR_OPEN(0),
        DOOR_CLOSE(1),
        DOOR_OPENING(2),
        DOOR_NOT_FOUND(3),
        USER_LOGIN(4),
        USER_LOGOUT(5),
        USER_NOT_GRANTED(6),
        USER_NOT_FOUND(7);

        private final int id;

        private static final Map<Integer, ItemType> lookup = new HashMap<>();

        static {
            for (ItemType type : ItemType.values()) {
                lookup.put(type.id, type);
            }
        }

        public static ItemType valueOf(int value) {
            return lookup.get(value);
        }

        public static ItemType[] values(Comparator<ItemType> comparator) {
            ItemType[] result = values();
            Arrays.sort(result, comparator);
            return result;
        }

        ItemType(int id) {
            this.id = id;
        }

        public int value() {
            return id;
        }

        @Override
        public String toString() {
            switch (this) {
                case DOOR_OPEN -> {
                    return "Aperto";
                }
                case DOOR_CLOSE -> {
                    return "Chiuso";
                }
                case DOOR_OPENING -> {
                    return "Richiesta accettata";
                }
                case DOOR_NOT_FOUND -> {
                    return "Dispositivo sconosciuto";
                }
                case USER_LOGIN -> {
                    return "Utente in ingresso";
                }
                case USER_LOGOUT -> {
                    return "Utente in uscita";
                }
                case USER_NOT_GRANTED -> {
                    return "Richiesta negata";
                }
                case USER_NOT_FOUND -> {
                    return "Utente sconosciuto";
                }
                default -> {
                    return super.toString();
                }
            }
        }
    }

    public static EventLog doorOpen(Door door) {
        return new EventLog(new Date(), ItemType.DOOR_OPEN, door);
    }

    public static EventLog doorClose(Door door) {
        return new EventLog(new Date(), ItemType.DOOR_CLOSE, door);
    }

    public static EventLog doorOpening(Door door, Person person) {
        return new EventLog(new Date(), ItemType.DOOR_OPENING, door, person);
    }

    public static EventLog doorNotFound() {
        return new EventLog(new Date(), ItemType.DOOR_NOT_FOUND);
    }

    public static EventLog userLogin(Door door, Person person) {
        return new EventLog(new Date(), ItemType.USER_LOGIN, door, person);
    }

    public static EventLog userLogout(Door door, Person person) {
        return new EventLog(new Date(), ItemType.USER_LOGOUT, door, person);
    }

    public static EventLog userNotGranted(Door door, Person person) {
        return new EventLog(new Date(), ItemType.USER_NOT_GRANTED, door, person);
    }

    public static EventLog userNotFound(Door door) {
        return new EventLog(new Date(), ItemType.USER_NOT_FOUND, door);
    }
}
