package com.neurocom.crud.repository;

import com.neurocom.crud.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by c.karalis on 4/14/2015.
 */
@RepositoryRestResource(path = "post")//, excerptProjection = PostRepository.PostProjection.class)
public interface PostRepository extends JpaRepository<Post, Long>, QueryDslPredicateExecutor<Post> {
//    interface PostProjection {
//        Long getId();
//        String getBody();
//        User getUser();
//        Date getSince();
//    }
}