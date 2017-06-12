package de.paulstueber.ui.model.dto;

import de.paulstueber.ui.model.Role;
import lombok.Data;

/**
 * Simple entity containing all details passed during an authentication request
 */
@Data
public class UserCreateForm {

    private String email = "";
    private String name;
    private String lastname;
    private String password = "";
    private Role role = Role.USER;

}
