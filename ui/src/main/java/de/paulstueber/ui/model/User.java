package de.paulstueber.ui.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Collection;

/**
 * User entity
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = "passwordHash")
public class User implements Serializable {
    @Id
    private String id;
    private String email;
    private String name;
    private String lastname;
    private String passwordHash;
    private LocalDateTime registrationDate;

    private Collection<Role> roles;
}
