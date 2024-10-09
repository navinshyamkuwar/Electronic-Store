package com.pirates.electronic.store.config;

import com.pirates.electronic.store.services.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.InMemoryUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    //   @Bean
//    public UserDetailsService userDetailsService(){
//        //users create
//        UserDetails normal = User.builder()
//                .username("Baki")
//                .password(passwordEncoder().encode("Baki"))
//                .roles("Normal")
//                .build();
//
//
//        UserDetails admin = User.builder()
//                .username("Dev")
//                .password(passwordEncoder().encode("Dev"))
//                .roles("admin")
//                .build();
//
//
//        //InMemoryUserDetailsManager is an implementation of UserDetailsService
//        return new InMemoryUserDetailsManager(normal,admin);
//    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
                .csrf(csrf -> csrf.disable())  // Disable CSRF protection
                .cors(cors -> cors.disable())  // Disable CORS
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated()  // All requests require authentication
                )
                .httpBasic(withDefaults());  // Use basic HTTP authentication with defaults

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
