package de.paulstueber.ui.controller;

import de.paulstueber.ui.exception.EntityAlreadyExistsException;
import de.paulstueber.ui.model.User;
import de.paulstueber.ui.model.dto.UserCreateForm;
import de.paulstueber.ui.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * Controller mapping all static views to their resources
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserService userService;

    /**
     * The start/index page available to any visitor
     * @return
     */
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public String settings() {
        return "/admin/users";
    }

    /**
     * User registration endpoint.
     *
     * @return name of the thymeleaf template
     * @throws EntityAlreadyExistsException thworn if the user credentials (email) already exists
     * */
    @RequestMapping(value = "/api/users", method = RequestMethod.GET)
    public ResponseEntity<List<User>> getUsers()
            throws EntityAlreadyExistsException {
        List<User> users = this.userService.getAll();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    /**
     * User registration endpoint.
     *
     * @param userCreateForm
     * @return name of the thymeleaf template
     * @throws EntityAlreadyExistsException thworn if the user credentials (email) already exists
     * */
    @RequestMapping(value = "/api/users", method = RequestMethod.POST)
    public ResponseEntity<User> handleRegisterUserForm(@RequestBody final UserCreateForm userCreateForm)
            throws EntityAlreadyExistsException {
        User user = this.userService.create(userCreateForm);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * User registration endpoint.
     *
     * @param userId id of user to be deleted
     * @return name of the thymeleaf template
     * @throws EntityAlreadyExistsException thworn if the user credentials (email) already exists
     * */
    @RequestMapping(value = "/api/users/{userId}", method = RequestMethod.DELETE)
    public ResponseEntity handleRegisterUserForm(@PathVariable final String userId)
            throws EntityAlreadyExistsException {
        this.userService.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
