package com.tfg.scrumweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.servlet.util.matcher.MvcRequestMatcher;
import org.springframework.web.servlet.handler.HandlerMappingIntrospector;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    MvcRequestMatcher.Builder mvc(HandlerMappingIntrospector introspector) {
        return new MvcRequestMatcher.Builder(introspector);
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity, MvcRequestMatcher.Builder mvc) throws Exception {
    return httpSecurity
        .authorizeHttpRequests(requests -> requests
            .requestMatchers(
                mvc.pattern("/"),
                mvc.pattern("/css/**"),
                mvc.pattern("/img/**"),
                mvc.pattern("/js/**"),
                mvc.pattern("/login2"),
                mvc.pattern("/registrarse"))
            .permitAll()
            .anyRequest().authenticated()
        ).formLogin(Customizer.withDefaults())
        .csrf().disable()
        .formLogin(form -> form
            .successHandler(successHandler()).permitAll() //URL de redirección si éxito
        ) 
        
        .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS) 
            .maximumSessions(1)
            .expiredUrl("/login")
            .sessionRegistry(sessionRegistry())
        )
        
        .build();    
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    public AuthenticationSuccessHandler successHandler(){
        return ((request, response, authentication) -> {
            response.sendRedirect(("/rol"));
        });
    }
    
}