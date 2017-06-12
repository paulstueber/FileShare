package de.paulstueber.ui.repository;

import de.paulstueber.ui.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

/**
 * Interface defining necessary User related methods.
 */
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Find user in DB by his email address
     * @param email
     * @return IOptional of User
     */
    Optional<User> findByEmail(final String email);

    /**
     * get a list of all available users
     * @return list of available users
     */
    List<User> findAll();
}
