package com.neurocom.crud.repository;

import com.neurocom.crud.domain.Post;
import com.neurocom.crud.domain.Thread;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QueryDslPredicateExecutor;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

/**
 * Created by c.karalis on 4/14/2015.
 */
@RepositoryRestResource(path = "thread", excerptProjection = ThreadRepository.ThreadProjection.class)
public interface ThreadRepository extends JpaRepository<Thread, Long>, QueryDslPredicateExecutor<Thread> {

    @Query("select p from Thread t left join t.posts p left join p.user u where u.username = ?#{principal.username}")
    Page<Post> findCurrentUserThreads(Pageable pageable);

    @Query("select distinct p from Post p " +
            "left join p.user u where u.username = ?#{principal.username}")
    Page<Post> findCurrentUserPosts(Pageable pageable);

    interface ThreadProjection {
        Long getId();
        String getTitle();
        Integer getCountOfPosts();
    }
}