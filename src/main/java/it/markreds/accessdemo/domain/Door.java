package it.markreds.accessdemo.domain;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
public class Door {

    @Id
    @GeneratedValue
    private Long id;
    private String displayName;
    @Column(unique = true)
    private String macAddress;
    private int workTime;
    @Transient
    private boolean open;

    public Door() { }

    public Door(String displayName) {
        this.displayName = displayName;
    }

    public Door(String displayName, String macAddress, int workTime) {
        this.displayName = displayName;
        this.macAddress = macAddress;
        this.workTime = workTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Door door = (Door) o;
        return Objects.equals(id, door.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("Door [id = %d, displayName = %s]", id, displayName);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public int getWorkTime() {
        return workTime;
    }

    public void setWorkTime(int workTime) {
        this.workTime = workTime;
    }

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }
}
