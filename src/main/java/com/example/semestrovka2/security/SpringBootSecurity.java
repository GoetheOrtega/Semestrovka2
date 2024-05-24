package com.example.semestrovka2.security;

import com.example.semestrovka2.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
@Configuration
@EnableWebSecurity
public class SpringBootSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/administrador/**", "/productos/**").hasRole("ADMIN") // Acceso solo para ADMIN a las rutas específicas
                .antMatchers("/images/**", "/css/**", "/vendor/**").permitAll() // Permitir acceso a recursos estáticos
                .antMatchers("/usuario/registro", "/usuario/save").permitAll() // Permitir acceso a la página de registro y guardar usuario
                .anyRequest().authenticated() // Todas las demás solicitudes requieren autenticación
                .and()
                .formLogin()
                .loginPage("/usuario/login") // Página de inicio de sesión personalizada
                .permitAll() // Permitir acceso a la página de inicio de sesión para todos
                .defaultSuccessUrl("/usuario/acceder") // Redirigir después del inicio de sesión
                .and()
                .exceptionHandling()
                .accessDeniedPage("/error-403"); // Página de error personalizada para acceso denegado
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
