package com.neurocom.crud.repository;

import com.neurocom.crud.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Optional;

/**
 * Created by c.karalis on 4/14/2015.
 */
@RepositoryRestResource(path = "user", excerptProjection = UserRepository.UserProjection.class)
public interface UserRepository extends JpaRepository<User, Long>, QueryDslPredicateExecutor<User> {

    @Query("select u from User u where u.username = ?#{principal.username}")
    Optional<User> findCurrentUser();

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    Page<User> findAll(Pageable pageable);

    @Override
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    User findOne(Long aLong);

    public interface UserProjection {
        String getUsername();
        String getFirstName();
        String getLastName();
    }
}