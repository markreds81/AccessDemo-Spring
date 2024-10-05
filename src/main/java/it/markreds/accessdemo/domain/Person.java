package it.markreds.accessdemo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.util.StringUtils;

@Entity
@Data
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @Id
    @GeneratedValue
    private Long id;

    private String firstName;

    private String lastName;

    @Column(unique = true)
    private String keyCode;

    private boolean enabled = true;

    public String getFullName() {
        boolean hasFirstName = StringUtils.hasText(firstName);
        boolean hasLastName = StringUtils.hasText(lastName);
        if (hasFirstName && hasLastName) {
            return String.format("%s %s", firstName, lastName);
        } else if (hasFirstName) {
            return firstName;
        } else if (hasLastName) {
            return lastName;
        } else return "";
    }
}
