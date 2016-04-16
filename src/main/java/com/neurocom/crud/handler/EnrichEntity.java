package com.neurocom.crud.handler;

import com.neurocom.crud.domain.TimedEntity;
import com.neurocom.crud.domain.User;
import com.neurocom.crud.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.core.annotation.HandleBeforeCreate;
import org.springframework.data.rest.core.annotation.RepositoryEventHandler;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Optional;

/**
 * Created by c.karalis on 11/11/2014.
 */
@Component
@RepositoryEventHandler(TimedEntity.class)
public class EnrichEntity {

    @Autowired
    private UserRepository userRepository;

    @HandleBeforeCreate(TimedEntity.class)
    public void handleBeforeCreate(TimedEntity p) {
        Optional<User> user = userRepository.findCurrentUser();
        p.setUser(user.get());
        p.setSince(new Date());
    }

}
