package com.agonyengine.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.inject.Inject;
import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private UserDetailsManager userDetailsManager;

    @Inject
    public SecurityConfiguration(DataSource dataSource, AuthenticationManagerBuilder auth) throws Exception {
        super();

        this.userDetailsManager = auth
            .jdbcAuthentication()
            .dataSource(dataSource)
            .getUserDetailsService();
    }

    @Bean
    public SecurityContextLogoutHandler getSecurityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    @Bean
    public UserDetailsManager getUserDetailsManager() {
        return userDetailsManager;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
            .authorizeRequests()
            .antMatchers(
                "/",
                "/favicon.ico",
                "/public/**",
                "/login/**",
                "/webjars/**",
                "/img/**",
                "/css/**",
                "/js/**",
                "/robots.txt")
            .permitAll()
            .anyRequest().authenticated()
            .and().logout().logoutSuccessUrl("/").permitAll()
            .and().formLogin().loginPage("/login").defaultSuccessUrl("/play", true);
    }
}
