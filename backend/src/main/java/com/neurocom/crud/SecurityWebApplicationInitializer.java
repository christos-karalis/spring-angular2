package com.neurocom.crud;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.web.context.*;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityWebApplicationInitializer {

}