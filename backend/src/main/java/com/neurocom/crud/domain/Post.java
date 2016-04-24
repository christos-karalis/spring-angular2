package com.neurocom.crud.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by c.karalis on 4/13/2015.
 */
@Entity
public class Post implements TimedEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1024)
    private String body;

    @ManyToOne
    @JoinColumn(name = "THREAD_ID")
    private Thread thread;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Column
    private Date since;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Thread getThread() {
        return thread;
    }

    public void setThread(Thread thread) {
        this.thread = thread;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getSince() {
        return since;
    }

    public void setSince(Date since) {
        this.since = since;
    }
}
