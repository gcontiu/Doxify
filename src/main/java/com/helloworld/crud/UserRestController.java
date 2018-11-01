package com.helloworld.crud;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import java.util.List;

@RestController
@RequestMapping("/user")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UserRestController {

    public static final Logger LOG = LoggerFactory.getLogger(UserRestController.class);

    private final UserRepository userRepository;

    @Inject
    public UserRestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/all")
    public Page<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @GetMapping("/find-by-username-containing")
    public List<User> findByUsernameContaining(@RequestParam("key") String keyword) {
        return userRepository.findUsersByUsernameContaining(keyword);
    }

    @PostMapping("/create-or-update")
    public User createOrUpdate(@RequestBody User user) {
        return userRepository.save(user);
    }

    @PostMapping("/delete")
    public ResponseEntity<String> delete(@RequestParam("id") Long id) {
        userRepository.deleteById(id);
        return ResponseEntity.ok("user removed from db");
    }

    @PostMapping("/timeSpentOnPage")
    public void test (@RequestBody String timeSpent) {
        LOG.info("Time spent on page: " + timeSpent);
    }

}
