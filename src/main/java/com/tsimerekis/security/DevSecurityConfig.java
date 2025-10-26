package com.tsimerekis.security;

import com.vaadin.flow.spring.security.VaadinWebSecurity;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

@Configuration
@Profile("dev")
public class DevSecurityConfig extends VaadinWebSecurity {
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers("/**").permitAll()
//        );
//    }
}
