package com.neurocom.crud.domain;

import java.util.Date;

/**
 * Created by c.karalis on 3/27/2016.
 */
public interface TimedEntity {
    User getUser();

    void setUser(User user);

    Date getSince();

    void setSince(Date since);
}
