package com.be.custom.common.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.context.SecurityContextPersistenceFilter;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Slf4j
@RequiredArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthenticationProvider authenticationProvider;
    private final HeaderAuthenticationFilter headerAuthenticationFilter;

    private static final String[] IGNORE_PATHS = {
            "/libs/**", "/custom/**", "/js/**", "/icon/**", "/images/**", "/favicon.ico/**"
            , "libs/**", "custom/**", "js/**", "icon/**", "images/**", "favicon.ico/**"
            , "/ws/**",  "ws/**"
    };


    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addExposedHeader("authorization");
        configuration.addAllowedMethod("*");
        configuration.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
    }

    @Bean
    public StrictHttpFirewall httpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowedHeaderNames(header -> true);
        firewall.setAllowedHeaderValues(header -> true);
        firewall.setAllowedParameterNames(parameter -> true);
        return firewall;
    }

    @Override
    public void configure(WebSecurity web) {
        web.httpFirewall(httpFirewall());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(IGNORE_PATHS).permitAll()
                .antMatchers("/question/get-question").permitAll()
                .antMatchers("/answer/**").permitAll()
                .antMatchers("/swagger-ui.html").hasRole("SYSTEM_ADMIN")
                .anyRequest().permitAll();
        http.formLogin()
                .usernameParameter("email")
                .passwordParameter("password")
                .loginProcessingUrl("/login");
        http.cors().and().csrf().disable();
        http.authenticationProvider(authenticationProvider);
        http.addFilterAfter(headerAuthenticationFilter, SecurityContextPersistenceFilter.class);

    }

}

