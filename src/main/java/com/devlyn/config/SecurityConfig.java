package com.devlyn.config;

import java.util.Arrays;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	 private static final String[] PUBLIC_MATCHERS = {
		        "/h2-console/**"
		    };
	 
	 private static final String[] PUBLIC_MATCHERS_GET = {
		        "/produtos/**",
		        "/categories/**"
		    };

		    @Bean
		    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		    	
		    	// Libera frameOptions se estiver no profile "test"
		    	/*
		        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
		            http.headers().frameOptions().disable();
		        }
		    	*/
		        http
		            .cors()
		            .and()
		            .csrf().disable()
		            .sessionManagement()
		            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		            .and()
		            .authorizeHttpRequests(authz -> authz
		                .requestMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
		                .requestMatchers(PUBLIC_MATCHERS).permitAll()
		                .anyRequest().authenticated()
		            );

		        // Caso use H2-console, outra forma de desabilitar o frameOptions para permitir o acesso via iframe
		        //http.headers().frameOptions().disable();

		        return http.build();
		    }

		    @Bean
		    public CorsConfigurationSource corsConfigurationSource() {
		        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();

		        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		        source.registerCorsConfiguration("/**", config);
		        return source;
		    }
}
