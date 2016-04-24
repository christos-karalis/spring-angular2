package com.neurocom.crud.domain;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;

/**
 * Created by c.karalis on 3/22/2016.
 */
@Entity
public class Thread {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @OneToMany(mappedBy = "thread")
    @OrderBy("since")
    private List<Post> posts;

    @ManyToOne
    @JoinColumn(name = "DIRECTORY_ID")
    private Directory directory;

    @Formula(" (select count(p.THREAD_ID) from THREAD t " +
            "left join POST p on p.THREAD_ID = t.ID " +
            "where t.ID = ID group by p.THREAD_ID) ")
    private Integer countOfPosts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Post> getPosts() {
        return posts;
    }

    public void setPosts(List<Post> posts) {
        this.posts = posts;
    }

    public Directory getDirectory() {
        return directory;
    }

    public void setDirectory(Directory directory) {
        this.directory = directory;
    }

    public Integer getCountOfPosts() {
        return countOfPosts;
    }

    public void setCountOfPosts(Integer countOfPosts) {
        this.countOfPosts = countOfPosts;
    }
}
