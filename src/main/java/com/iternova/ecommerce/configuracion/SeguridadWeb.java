package com.iternova.ecommerce.configuracion;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity //va a autorizar paginas según el usuario
public class SeguridadWeb {

    @Autowired
    public UserDetailsService userDetailsService;

    @Autowired
    public BCryptPasswordEncoder bCrypt;

    //autenticación del usuario
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(bCrypt);
        return authenticationProvider;
    }

    //restricción segun los roles
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http
                .csrf().disable()
                .authorizeHttpRequests((req)-> {
                            try {
                                req
                                        .requestMatchers("/admin/**").hasRole("ADMIN")
                                        .requestMatchers("/productos/**").hasRole("ADMIN")
                                        .requestMatchers("/css/*", "/js/*", "/img/*", "/**","/login")
                                        .permitAll()
                                        .and()
                                        .formLogin()
                                        .loginPage("/usuario/login")
                                        .permitAll()
                                        .defaultSuccessUrl("/usuario/acceder")
                                        .and()
                                        .logout().logoutUrl("/usuario/cerrar").permitAll();
                            } catch (Exception e) {
                                throw new RuntimeException(e);
                            }
                        }
                );
            return http.build();
    }
}
