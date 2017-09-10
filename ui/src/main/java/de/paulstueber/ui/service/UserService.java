package de.paulstueber.ui.service;

import de.paulstueber.ui.exception.EntityAlreadyExistsException;
import de.paulstueber.ui.model.Role;
import de.paulstueber.ui.model.User;
import de.paulstueber.ui.model.dto.UserCreateForm;
import de.paulstueber.ui.repository.UserRepository;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;


/**
 * User service to retrieve, update or process user related date from DB.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MessagingService camelUtils;

    /**
     * Get user by Id
     *
     * @param id user id
     * @return Optional of user, Optional.empty() if not found.
     */
    public final Optional<User> getById(final String id) {
        return Optional.ofNullable(userRepository.findOne(id));
    }

    /**
     * Get user by Email
     *
     * @param email user email
     * @return Optional of user, Optional.empty() if not found.
     */
    public final Optional<User> getByEmail(final String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Get all user entities stored in the DB
     *
     * @return List containing all available users.
     */
    @Secured("ROLE_SUPER_ADMIN")
    public final List<User> getAll() {
        return userRepository.findAll();
    }

    /**
     * Save or update an existing user
     *
     * @param user User object to store.
     * @return user instance
     */
    public final User save(final User user) {
        User myUser = userRepository.save(user);
        camelUtils.save(myUser);
        return myUser;
    }

    /**
     * create a new user from a 'create form' (web DTO) and save him to DB
     *
     * @param userCreateForm form with all user details
     * @return newly created user
     * @throws EntityAlreadyExistsException thrown if the user to create already exists (email)
     */
    @Secured("ROLE_SUPER_ADMIN")
    public final User create(@NonNull final UserCreateForm userCreateForm) throws EntityAlreadyExistsException {
        if (this.userRepository.findByEmail(userCreateForm.getEmail()).isPresent()) {
            throw new EntityAlreadyExistsException(String.format("A user with the given email '%s' adress already exists!",
                    userCreateForm.getEmail()));
        }

        User user = User.builder()
                .name(userCreateForm.getName())
                .lastname(userCreateForm.getLastname())
                .email(userCreateForm.getEmail())
                .registrationDate(LocalDateTime.now())
                .passwordHash(new BCryptPasswordEncoder().encode(userCreateForm.getPassword()))
                .roles(Arrays.asList(userCreateForm.getRole()))
                .build();

        return this.save(user);
    }

    /**
     * create a new user and save him to DB
     *
     * @param email
     * @param password
     * @param role
     * @return
     * @throws EntityAlreadyExistsException thrown if the user to create already exists (email)
     */
    @Secured("ROLE_SUPER_ADMIN")
    public final User create(@NonNull final String email,
                             @NonNull final String password,
                             @NonNull final Role role) throws EntityAlreadyExistsException {
        if (this.userRepository.findByEmail(email).isPresent()) {
            throw new EntityAlreadyExistsException(String.format("A user with the given email '%s' adress already exists!", email));
        }

        User user = User.builder()
                .email(email)
                .registrationDate(LocalDateTime.now())
                .passwordHash(new BCryptPasswordEncoder().encode(password))
                .roles(Arrays.asList(role))
                .build();

        return this.save(user);
    }

    /**
     * delete an existing use from DB. This is only allowed to ADMINs
     * @param user
     */
    @Secured("ROLE_SUPER_ADMIN")
    public void deleteUser(final User user) {
        this.userRepository.delete(user);
        camelUtils.delete(user);
    }
    /**
     * delete an existing use from DB. This is only allowed to ADMINs
     * @param userId
     */
    @Secured("ROLE_SUPER_ADMIN")
    public void deleteUserById(@NonNull final String userId) {
        if (this.getById(userId).isPresent()) {
            camelUtils.delete(this.getById(userId).get());
            this.userRepository.delete(userId);
        }
    }
}
