package com.neurocom.crud;

import com.neurocom.crud.config.WebSecurityConfig;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

/**
 * Created by c.karalis on 5/3/2015.
 */
public class SecurityWebMvcInitializer extends
        AbstractSecurityWebApplicationInitializer {

    public SecurityWebMvcInitializer() {
        super(WebSecurityConfig.class, SecurityWebApplicationInitializer.class);
    }
}
