package de.paulstueber.ui.model;

import org.springframework.security.core.authority.AuthorityUtils;

import java.util.Collection;
import java.util.stream.Collectors;

/**
 * Implementation of Spring securities userdetails.User for authentication purpose
 */
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    /**
     * Constructor taking only an user  instance and calling super
     * @param user
     */
    public CurrentUser(final User user) {
        super(user.getEmail(),
                user.getPasswordHash(),
                AuthorityUtils.createAuthorityList(user.getRoles().stream()
                        .map(Role::name)
                        .collect(Collectors.toList())
                        .toArray(new String[]{})));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public String getId() {
        return user.getId();
    }

    public Collection<Role> getRoles() {
        return user.getRoles();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        if (!super.equals(o)) {
            return false;
        }

        CurrentUser that = (CurrentUser) o;

        return user != null ? user.equals(that.user) : that.user == null;

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (user != null ? user.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "CurrentUser{" +
                "user=" + user +
                "} " + super.toString();
    }
}
