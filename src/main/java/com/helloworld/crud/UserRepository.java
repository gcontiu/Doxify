package com.helloworld.crud;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * Find user by its username. The framework automatically generates
     * the implementation using the name of the method.
     *
     * @param username
     * @return
     */
    Optional<User> findUserByUsername(String username);

    List<User> findUsersByUsernameContaining(String keyword);
}
