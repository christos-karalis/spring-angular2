package com.neurocom.crud.repository;

import com.neurocom.crud.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

/**
 * Created by c.karalis on 4/14/2015.
 */
@RepositoryRestResource(path = "user")
public interface UserRepository extends JpaRepository<User, Long>, QueryDslPredicateExecutor<User> {

    @Query("select u from User u where u.username = ?#{principal.username}")
    Optional<User> findCurrentUser();

}