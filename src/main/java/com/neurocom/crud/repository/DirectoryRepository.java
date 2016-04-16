package com.neurocom.crud.repository;

import com.neurocom.crud.domain.Directory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by c.karalis on 4/14/2015.
 */
@RepositoryRestResource(path = "directory")
public interface DirectoryRepository extends JpaRepository<Directory, Long>, QueryDslPredicateExecutor<Directory> {
}