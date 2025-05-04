package es.ubu.baloncesto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * Configuración de seguridad para la aplicación.
 * Define las reglas de acceso, el servicio de autenticación y los usuarios.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Configura el filtro de seguridad.
     * Define las reglas de acceso para cada URL de la aplicación.
     *
     * @param http Objeto HttpSecurity para configurar la seguridad
     * @return SecurityFilterChain configurado
     * @throws Exception Si hay un error en la configuración
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // CONFIGURO LAS REGLAS DE SEGURIDAD
        http
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                                // PERMITO ACCESO PÚBLICO A RECURSOS ESTÁTICOS Y PÁGINA PRINCIPAL
                                .requestMatchers("/css/**", "/js/**", "/", "/about", "/api/**").permitAll()
                                // REQUIERO AUTENTICACIÓN PARA LAS DEMÁS RUTAS
                                .anyRequest().authenticated()
                )
                .formLogin(formLogin ->
                        formLogin
                                // CONFIGURO LA PÁGINA DE LOGIN
                                .loginPage("/login")
                                .defaultSuccessUrl("/", true)
                                .permitAll()
                )
                .logout(logout ->
                        logout
                                // CONFIGURO EL LOGOUT
                                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                                .logoutSuccessUrl("/login?logout")
                                .invalidateHttpSession(true)
                                .clearAuthentication(true)
                                .permitAll()
                )
                .exceptionHandling(exceptionHandling ->
                        exceptionHandling
                                // CONFIGURO LA PÁGINA DE ACCESO DENEGADO
                                .accessDeniedPage("/access-denied")
                );

        // DESACTIVO CSRF PARA LAS LLAMADAS A LA API
        // ESTO ES NECESARIO PARA QUE FUNCIONEN LAS LLAMADAS DESDE POSTMAN O JAVASCRIPT
        http.csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"));

        return http.build();
    }

    /**
     * Configura el codificador de contraseñas.
     *
     * @return Instancia de BCryptPasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // USO BCRYPT PARA CODIFICAR LAS CONTRASEÑAS
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura el servicio de detalles de usuario.
     * En este caso, se utilizan usuarios en memoria para simplificar.
     *
     * @param passwordEncoder Codificador de contraseñas
     * @return Servicio de detalles de usuario configurado
     */
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        // CREO USUARIOS EN MEMORIA PARA PRUEBAS
        // EN UN ENTORNO REAL, ESTO SE CARGARÍA DESDE UNA BASE DE DATOS
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder.encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("USER", "ADMIN")
                .build();

        // DEVUELVO UN GESTOR DE USUARIOS EN MEMORIA
        return new InMemoryUserDetailsManager(user, admin);
    }
}