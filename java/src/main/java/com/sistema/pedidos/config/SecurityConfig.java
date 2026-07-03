package com.sistema.pedidos.config;

import com.sistema.pedidos.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    
    private final CustomUserDetailsService userDetailsService;
    
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
    
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authz -> authz
                // Recursos públicos
                .requestMatchers("/webjars/**", "/css/**", "/js/**", "/images/**").permitAll()
                //nuevo
                .requestMatchers("/login", "/registro", "/error").permitAll()
                //.requestMatchers("/", "/login", "/registro", "/error").permitAll()
                .requestMatchers("/productos", "/productos/ver/**", "/comentarios/producto/**").permitAll()
                
                // Rutas de administrador
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/clientes/**").hasRole("ADMIN")
                .requestMatchers("/productos/nuevo", "/productos/editar/**", "/productos/eliminar/**").hasRole("ADMIN")
                .requestMatchers("/usuarios/**").hasRole("ADMIN")
                .requestMatchers("/comentarios", "/comentarios/eliminar/**").hasRole("ADMIN")
                
                // Rutas de cliente y admin
                .requestMatchers("/comentarios/agregar", "/comentarios/mis-comentarios").hasAnyRole("ADMIN", "CLIENTE")
                .requestMatchers("/preferencias/**", "/historial/**", "/perfil").hasAnyRole("ADMIN", "CLIENTE")
                .requestMatchers("/pedidos/nuevo", "/pedidos/guardar").hasAnyRole("ADMIN", "CLIENTE")
                
                // Todas las demás rutas requieren autenticación
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                //aqui se hiso un cambio / por -/dashboard
                .defaultSuccessUrl("/dashboard", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .permitAll()
            )
            .exceptionHandling(exception -> exception
                .accessDeniedPage("/acceso-denegado")
            )
            .authenticationProvider(authenticationProvider());
        
        return http.build();
    }
}
