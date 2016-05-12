package com.neurocom.crud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by c.karalis on 5/3/2015.
 */
@Configuration
@PropertySource("classpath:config.properties")
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    public static final String ROLE_USER = "ROLE_USER";
    public static final String ROLE_ANONYMOUS = "ROLE_ANONYMOUS";
    public static final EmptyAuthenticationSuccessHandler SUCCESS_HANDLER = new EmptyAuthenticationSuccessHandler();
    public static final EmptyAuthenticationFailureHandler FAILURE_HANDLER = new EmptyAuthenticationFailureHandler();

    @Value("${csrf.enabled}")
    private String csrfEnabled;

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER")
                .authorities(new GrantedAuthority[]{new SimpleGrantedAuthority(ROLE_USER)});
    }

    /**
     * Trick to activate to activate {@link org.springframework.security.web.authentication.Http403ForbiddenEntryPoint}
     * instead of {@link org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint}, since we allow
     * anonymous access to '/' and
     * @param http
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        if (!csrfEnabled.equals("true")) {
            http.csrf().disable();
        }
        http
                .httpBasic()
                .and()
                .logout()
                .logoutSuccessHandler(SUCCESS_HANDLER)
                .and()
                .formLogin()
                .loginProcessingUrl("/authenticate")
                .successHandler(SUCCESS_HANDLER)
                .failureHandler(FAILURE_HANDLER)
                .and().anonymous().principal(new User("anonymous", "",
                Arrays.asList(new GrantedAuthority[]{new SimpleGrantedAuthority(ROLE_ANONYMOUS)})));
    }


    private static class EmptyAuthenticationSuccessHandler implements AuthenticationSuccessHandler, LogoutSuccessHandler {
        @Override
        public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_OK);
        }

        @Override
        public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
            response.setStatus(HttpServletResponse.SC_OK);
        }
    }

    private static class EmptyAuthenticationFailureHandler implements AuthenticationFailureHandler {
        @Override
        public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
            response.setHeader("error.code", exception.getMessage());
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }
}
