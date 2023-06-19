package fr.fms.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Autowired
    private DataSource dataSource;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("SELECT username as principal, password as credentials , 1 FROM User WHERE username = ?")
                .authoritiesByUsernameQuery("SELECT u.username, r.role_name FROM User u " +
                        "JOIN USERS_ROLES ur ON u.id = ur.USER_ID " +
                        "JOIN Role r ON ur.ROLE_ID = r.id " +
                        "WHERE u.username = ?")
                .passwordEncoder(passwordEncoder())
                .rolePrefix("ROLE_");
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/").permitAll()
                .antMatchers("/login").permitAll()
                .and()
                .formLogin()
                .permitAll()
                .and()
                .csrf().disable();

        return http.build();
    }


}
