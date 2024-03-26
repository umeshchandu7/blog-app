package com.mountblue.blogapp.security;

import com.mountblue.blogapp.Service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.thymeleaf.extras.springsecurity6.dialect.SpringSecurityDialect;

@Configuration
public class SecurityConfig {

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserService userService) {
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
        auth.setUserDetailsService(userService);
        auth.setPasswordEncoder(customPasswordEncoder());
        return auth;
    }
    @Bean
    public PasswordEncoder customPasswordEncoder() {
        return NoOpPasswordEncoder.getInstance();
    }
    @Bean
    public SpringSecurityDialect springSecurityDialect() {
        return new SpringSecurityDialect();
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http.authorizeHttpRequests(configurer ->
                        configurer
                                .requestMatchers("/readForm","/","/register","/processUser**","/api/Post","/filter/**").permitAll()
                                .requestMatchers(HttpMethod.POST,"/api/filter/**").permitAll()
                                .requestMatchers(HttpMethod.PUT,"/api/Posts").hasAnyRole("AUTHOR","ADMIN")
                                .requestMatchers(HttpMethod.POST,"/api/Posts").hasAnyRole("AUTHOR","ADMIN")
                                .requestMatchers(HttpMethod.DELETE,"/api/Posts/").hasAnyRole("AUTHOR","ADMIN")
                                .requestMatchers(HttpMethod.GET,"/api/Posts").permitAll()
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form
                                .loginPage("/loginUser")
                                .loginProcessingUrl("/authenticateTheUser")
                                .permitAll()
                )
                .logout(logout -> logout.permitAll()
                )
                .exceptionHandling(configurer ->
                        configurer.accessDeniedPage("/access-denied")
                );
http.httpBasic(Customizer.withDefaults()); http.csrf(csrf -> csrf.disable());
        return http.build();
    }
}
