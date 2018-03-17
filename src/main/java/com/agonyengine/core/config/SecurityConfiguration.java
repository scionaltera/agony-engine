package com.agonyengine.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import javax.inject.Inject;
import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private DataSource dataSource;

    @Inject
    public SecurityConfiguration(DataSource dataSource) {
        super();

        this.dataSource = dataSource;
    }

    @Bean
    public SecurityContextLogoutHandler getSecurityContextLogoutHandler() {
        return new SecurityContextLogoutHandler();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.antMatcher("/**")
                .authorizeRequests()
                .antMatchers(
                        "/",
                        "/public/**",
                        "/login**",
                        "/webjars/**",
                        "/img/**",
                        "/css/**",
                        "/js/**",
                        "/robots.txt")
                .permitAll()
                .anyRequest().authenticated()
                .and().logout().logoutSuccessUrl("/").permitAll()
                .and().formLogin().loginPage("/login");
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        PasswordEncoder encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        UserDetails scion = User.builder()
                .username("Scion")
                .password("password")
                .passwordEncoder(encoder::encode)
                .roles("USER", "ADMIN")
                .build();

        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .withUser(scion);
    }
}
