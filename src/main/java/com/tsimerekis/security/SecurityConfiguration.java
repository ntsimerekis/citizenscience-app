package com.tsimerekis.security;

import com.tsimerekis.base.ui.LoginView;
import com.vaadin.flow.spring.security.NavigationAccessControlConfigurer;
import com.vaadin.flow.spring.security.VaadinAwareSecurityContextHolderStrategyConfiguration;
import com.vaadin.flow.spring.security.VaadinSecurityConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import com.vaadin.flow.spring.security.VaadinWebSecurity; // New Import!
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.provisioning.UserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
@Import(VaadinAwareSecurityContextHolderStrategyConfiguration.class)
public class SecurityConfiguration {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        var publicMatchers = VaadinWebSecurity.getDefaultHttpSecurityPermitMatcher();

        // Configure your static resources with public access
//        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers("/public/**").permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/vaadinServlet/**")).permitAll()
//                .requestMatchers(new AntPathRequestMatcher("/HILLA/push/**")).permitAll()
//                .requestMatchers(publicMatchers).permitAll()
//        );

        // Configure Vaadin's security using VaadinSecurityConfigurer
        http.with(VaadinSecurityConfigurer.vaadin(), configurer -> {
            // This is important to register your login view to the
            // navigation access control mechanism:
            configurer.loginView(LoginView.class);

            // You can add any possible extra configurations of your own
            // here (the following is just an example):
//            configurer.enableCsrfConfiguration(false);

            configurer.enableNavigationAccessControl(true);

        });

        return http.build();
    }

//    @Bean
//    public NavigationAccessControlConfigurer navigationAccessControlConfigurer() {
//        return new NavigationAccessControlConfigurer();
//
////                .withRoutePathAccessChecker();
//    }
    /**
     * Demo UserDetailsManager which only provides two hardcoded
     * in memory users and their roles.
     * NOTE: This shouldn't be used in real world applications.
     */
//    @Bean
//    public UserDetailsManager userDetailsService() {
//        UserDetails user =
//                User.withUsername("user")
//                        .password("{noop}user")
//                        .roles("USER")
//                        .build();
//        UserDetails admin =
//                User.withUsername("admin")
//                        .password("{noop}admin")
//                        .roles("ADMIN")
//                        .build();
//        return new InMemoryUserDetailsManager(user, admin);
//    }
@Bean
UserDetailsManager users(DataSource dataSource) {
    UserDetails user = User.builder()
            .username("user")
            .password("$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
            .roles("USER")
            .build();
    UserDetails admin = User.builder()
            .username("admin")
            .password("$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW")
            .roles("USER", "ADMIN")
            .build();
    JdbcUserDetailsManager users = new JdbcUserDetailsManager(dataSource);
//    users.createUser(user);
//    users.createUser(admin);
    return users;
}


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
//        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }
}