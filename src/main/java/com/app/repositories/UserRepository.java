package com.app.repositories;

import com.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserName(String username);

    /*
    //Otra forma de hacerlo
    @Query("SELECT u FROM User u WHERE u.username = ?")
    Optional<User> findUser(String username);*/
}

