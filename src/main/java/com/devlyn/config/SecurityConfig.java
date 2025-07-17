package com.devlyn.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.devlyn.security.JWTAuthenticationFilter;
import com.devlyn.security.JWTAuthorizationFilter;
import com.devlyn.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) 
public class SecurityConfig {

	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private JWTUtil jwtUtil;
	@Autowired
	private Environment env;
	
	 private static final String[] PUBLIC_MATCHERS = {
		        "/h2-console/**"
		    };
	 
	 private static final String[] PUBLIC_MATCHERS_GET = {
		        "/produtos/**",
		        "/categories/**"
		    };

	 private static final String[] PUBLIC_MATCHERS_POST = {
		        "/clientes/**"
		    };
	 
		    @Bean
		    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		    	
		    	// Libera frameOptions se estiver no profile "test":
		    	/*
		        if (Arrays.asList(env.getActiveProfiles()).contains("test")) {
		            http.headers().frameOptions().disable();
		        }
		    	*/
		    	
		    	 // Cria o filtro de autenticação JWT
		        JWTAuthenticationFilter authFilter = new JWTAuthenticationFilter(
		            authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),
		            jwtUtil
		        );
		        authFilter.setFilterProcessesUrl("/login"); // Define o endpoint que o filtro vai interceptar
		    	
		        http
		            .cors()
		            .and()
		            .csrf().disable()
		            .formLogin().disable()
		            .sessionManagement()
		            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		            .and()
		            .authorizeHttpRequests(authz -> authz
		            	.requestMatchers(HttpMethod.POST, "/login").permitAll()
		                .requestMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll()
		                .requestMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll()
		                .requestMatchers(PUBLIC_MATCHERS).permitAll()
		                .anyRequest().authenticated()
		            ).authenticationProvider(authenticationProvider());
		        http.addFilter(authFilter);
		        http.addFilter(new JWTAuthorizationFilter(
		        		authenticationManager(http.getSharedObject(AuthenticationConfiguration.class)),
		        	    jwtUtil,
		        	    userDetailsService
		        	));

		        

		        // Caso use H2-console, outra forma de desabilitar o frameOptions para permitir o acesso via iframe
		        //http.headers().frameOptions().disable();

		        return http.build();
		    }
		    
		    @Bean
		    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		        return authConfig.getAuthenticationManager();
		    }
		    
		    @Bean
		    public DaoAuthenticationProvider authenticationProvider() {
		        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
		        authProvider.setUserDetailsService(userDetailsService);
		        authProvider.setPasswordEncoder(bCryptPasswordEncoder());
		        return authProvider;
		    }
		    
		    /* deprecated
		    @Override
		    public void configure(AuthenticationManagerBuilder auth) throws Exception{
		    	auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
		    }
		     */
		    @Bean
		    public CorsConfigurationSource corsConfigurationSource() {
		        CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();

		        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		        source.registerCorsConfiguration("/**", config);
		        return source;
		    }
		    
		    @Bean
		    public BCryptPasswordEncoder bCryptPasswordEncoder() {
		    	return new BCryptPasswordEncoder();
		    }
}
