package it.markreds.accessdemo.domain;

import java.util.Date;

public record Permission(Long id, Date timestamp, Boolean allowed, Integer workTime) {
}
