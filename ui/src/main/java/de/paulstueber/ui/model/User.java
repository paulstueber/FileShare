package de.paulstueber.ui.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

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
public class User implements FSEntity {
    @Id
    private String id;
    private String email;
    private String name;
    private String lastname;
    @JsonIgnore
    private String passwordHash;
    private LocalDateTime registrationDate;

    private Collection<Role> roles;
}
