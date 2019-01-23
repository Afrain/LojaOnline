package com.afrain.lojaonline.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.afrain.lojaonline.security.JWTFiltroAutenticacao;
import com.afrain.lojaonline.security.JWTFiltroAutorizacao;
import com.afrain.lojaonline.security.JWTUtil;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private Environment environment;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private JWTUtil jwtUtil;

	private static final String[] CAMINHO_PUBLICO = { 
			"/h2-console/**"
			};
	
	private static final String[] CAMINHO_PUBLICO_GET = { 
			"/produtos/**", 
			"/categorias/**" 
			};
	
	private static final String[] CAMINHO_PUBLICO_POST = {  
			"/categorias/**" 
			};
	
	private static final String[] CAMINHO_PUBLICO_PUT = {  
			"/categorias/**" 
			};
	
	private static final String[] CAMINHO_PUBLICO_DELETE = {  
			"/categorias/**" 
			};
	
	protected void configure(HttpSecurity http) throws Exception {
		
		if (Arrays.asList(environment.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		http.cors().and().csrf().disable();
		http.authorizeRequests()
		.antMatchers(CAMINHO_PUBLICO).permitAll()
		.antMatchers(HttpMethod.GET, CAMINHO_PUBLICO_GET).permitAll()
		.antMatchers(HttpMethod.POST, CAMINHO_PUBLICO_POST).permitAll()
		.antMatchers(HttpMethod.PUT, CAMINHO_PUBLICO_PUT).permitAll()
		.antMatchers(HttpMethod.DELETE, CAMINHO_PUBLICO_DELETE).permitAll()
		.anyRequest().authenticated();
		http.addFilter(new JWTFiltroAutenticacao(authenticationManager(), jwtUtil));
		http.addFilter(new JWTFiltroAutorizacao(authenticationManager(), jwtUtil, userDetailsService));
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	}
	
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

	//CONFIGURACAO DO H2-CONSOLE
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
	
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}