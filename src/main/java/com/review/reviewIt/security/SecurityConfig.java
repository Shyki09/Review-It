package com.review.reviewIt.security;


import com.review.reviewIt.service.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JWTAuthFilter jwtAuthFilter;

    @Autowired
    private JwtAuthEntryPoint authEntryPoint;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .exceptionHandling(exception -> exception.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints
                        .requestMatchers("api/reviews/**", "api/comments/**", "api/restaurants/**", "/auth/**").permitAll()

                        // Authenticated endpoints
                        .requestMatchers("api/reviews/add", "api/comments/add").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers("api/users/get-user-reviews/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers("api/reviews/delete/**", "api/comments/delete/**",
                                "api/reviews/update-review/**", "api/comments/update-comment/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")

                        // Admin endpoints
                        .requestMatchers("api/restaurants/add-restaurant", "/api/restaurants/delete/**",
                                "/update/{restaurantId}").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers("api/users/get-by-id/**").hasAnyAuthority("ROLE_ADMIN", "ROLE_USER")
                        .requestMatchers("api/users/all").hasAnyAuthority("ROLE_ADMIN")
                        .requestMatchers("api/users/delete/**").hasAnyAuthority("ROLE_ADMIN" , "ROLE_USER")

                        // Default to authenticated
                        .anyRequest().authenticated()
                )
                .authenticationProvider(daoAuthenticationProvider())
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }


    @Bean
    public JWTAuthFilter authTokenFilter(){
        return new JWTAuthFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }


    @Bean
    public AuthenticationProvider daoAuthenticationProvider(){
        var authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(customUserDetailService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

}
