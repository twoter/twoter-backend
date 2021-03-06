package app.http.controllers;

import app.core.repos.UserRepository;
import app.http.pojos.CustomUserDetails;
import app.http.pojos.UserCreateResource;
import app.http.pojos.UserUpdateResource;
import app.pojo.User;
import app.services.FollowService;
import app.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@SuppressWarnings("unused")
@RestController
@RequestMapping(value = "/api/user")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final FollowService followService;

    @Autowired
    UserController(
            final UserService userService,
            final UserRepository userRepository,
            final FollowService followService
    ) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.followService = followService;
    }

    @RequestMapping(path = "/{userId}", method = RequestMethod.GET)
    public User getById(
            final @PathVariable int userId,
            final @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        final int loggedUserId = userDetails.getId();
        return userService.findById(userId, loggedUserId);
    }

    @RequestMapping(path = "/popular", method = RequestMethod.GET)
    public List<User> getById() {
        final List<User> users = userService.findTopPopular();

        return users;
    }

    @RequestMapping(method = RequestMethod.POST)
    public void add(final @Valid @RequestBody UserCreateResource userCreateData) {
        userService.create(userCreateData);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "", method = RequestMethod.PUT)
    public void update(
            final @Valid @RequestBody UserUpdateResource data,
            final @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        final int loggedUserId = userDetails.getId();
        userService.upate(data, loggedUserId);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/follow/{userId}", method = RequestMethod.POST)
    public void follow(
            final @PathVariable int userId,
            final @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        final int loggedUserId = userDetails.getId();
        followService.follow(loggedUserId, userId);
    }


    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(path = "/unfollow/{userId}", method = RequestMethod.POST)
    public void unfollow(
            final @PathVariable int userId,
            final @AuthenticationPrincipal CustomUserDetails userDetails
    ) {
        final int loggedUserId = userDetails.getId();
        followService.unfollow(loggedUserId, userId);
    }

}
