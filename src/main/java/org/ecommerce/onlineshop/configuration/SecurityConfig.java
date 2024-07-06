package org.ecommerce.onlineshop.configuration;

import org.ecommerce.onlineshop.repository.RoleRepository;
import org.ecommerce.onlineshop.repository.UserRepository;
import org.ecommerce.onlineshop.service.EmailService;
import org.ecommerce.onlineshop.service.UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    private static final int SEVEN_DAYS_IN_SECONDS = 604800;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;

    public SecurityConfig(UserRepository userRepository, RoleRepository roleRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(authorize ->
                authorize.requestMatchers("/registration/**").permitAll()
                    .requestMatchers("/images/perfumes/**").permitAll()
                    .requestMatchers("/").permitAll()
                    .requestMatchers("/forgot-password").permitAll()
                    .requestMatchers("/reset-password").permitAll()
                    .requestMatchers("/perfumes/**").permitAll()
                    .requestMatchers("/account/**").hasRole("USER")
                    .requestMatchers("/cart/**").hasRole("USER")
                    .requestMatchers("/order/**").hasRole("USER")
                    .requestMatchers("/admin/**").hasRole("ADMIN")
                    .requestMatchers("/admin/superAdmin/**").hasRole("SUPER_ADMIN")
            ).formLogin(
                form -> form
                    .loginPage("/login")
                    .loginProcessingUrl("/login")
                    .defaultSuccessUrl("/account")
                    .permitAll()
            ).logout(
                logout -> logout
                    .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                    .logoutUrl("/logout")
                    .logoutSuccessUrl("/login?logout")
                    .permitAll()
            ).sessionManagement(
                httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                    .invalidSessionUrl("/login")
            ).rememberMe(
                remember -> remember
                    .userDetailsService(userDetailsService())
                    .key("myAppRememberMeKey")
                    .rememberMeParameter("remember-me")
                    .tokenValiditySeconds(SEVEN_DAYS_IN_SECONDS));

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService(emailService, userRepository, roleRepository, bCryptPasswordEncoder());
    }

    @Bean
    public DaoAuthenticationProvider authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
        authProvider.setUserDetailsService(userDetailsService());

        return authProvider;
    }

    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_SUPER_ADMIN > ROLE_ADMIN > ROLE_USER");

        return roleHierarchy;
    }
}
