package it.markreds.accessdemo.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
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
}
