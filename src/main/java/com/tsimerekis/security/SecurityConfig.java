package com.tsimerekis.security;

import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
@Profile("production")
class SecurityConfig {

//    @Bean
//    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        // Configure Vaadin's security using VaadinSecurityConfigurer
//        http.with(VaadinSecurityConfigurer.vaadin(), configurer -> {
//            // TODO Configure the login view
//        });
//        return http.build();
//    }
//
//    @Bean
//    public UserDetailsManager userDetailsManager() {
//        LoggerFactory.getLogger(SecurityConfig.class)
//                .warn("NOT FOR PRODUCTION: Using in-memory user details manager!");
//        var user = User.withUsername("user")
//                .password("{noop}user")
//                .roles("USER")
//                .build();
//        var admin = User.withUsername("admin")
//                .password("{noop}admin")
//                .roles("ADMIN")
//                .build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }
}

