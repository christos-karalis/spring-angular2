package com.neurocom.crud.domain;

import org.hibernate.annotations.Formula;

import javax.persistence.*;
import java.util.List;

/**
 * Created by c.karalis on 3/22/2016.
 */
@Entity
public class Directory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(columnDefinition = "INTEGER GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1)")
    private Long id;

    @Column
    private String title;

    @Column
    private String description;

    @OneToMany(mappedBy = "directory", fetch = FetchType.LAZY)
    private List<Thread> threads;

    @Formula(" (select count(t.DIRECTORY_ID) from DIRECTORY d " +
            "left join THREAD t on t.DIRECTORY_ID = d.ID " +
            "where d.ID = ID group by t.DIRECTORY_ID) ")
    private Integer countOfTopics;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Thread> getThreads() {
        return threads;
    }

    public void setThreads(List<Thread> threads) {
        this.threads = threads;
    }

    public Integer getCountOfTopics() {
        return countOfTopics;
    }

    public void setCountOfTopics(Integer countOfTopics) {
        this.countOfTopics = countOfTopics;
    }
}
