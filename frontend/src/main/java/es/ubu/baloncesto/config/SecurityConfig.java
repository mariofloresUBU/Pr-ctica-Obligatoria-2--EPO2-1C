package es.ubu.baloncesto.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

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
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    /**
     * Configura el filtro de seguridad.
     * Define las reglas de acceso para cada URL de la aplicación.
     *
     * @param http Objeto HttpSecurity para configurar la seguridad
     * @throws Exception Si hay un error en la configuración
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // CONFIGURO LAS REGLAS DE SEGURIDAD
        http
                .authorizeRequests()
                // PERMITO ACCESO PÚBLICO A RECURSOS ESTÁTICOS Y PÁGINA PRINCIPAL
                .antMatchers("/css/**", "/js/**", "/", "/about", "/api/**").permitAll()
                // PIDO AUTENTICACIÓN PARA LAS DEMÁS RUTAS
                .anyRequest().authenticated()
                .and()
                .formLogin()
                // CONFIGURO LA PÁGINA DE LOGIN
                .loginPage("/login")
                .defaultSuccessUrl("/", true)
                .permitAll()
                .and()
                .logout()
                // CONFIGURO EL LOGOUT
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/login?logout")
                .invalidateHttpSession(true)
                .clearAuthentication(true)
                .permitAll()
                .and()
                .exceptionHandling()
                // CONFIGURO LA PÁGINA DE ACCESO DENEGADO
                .accessDeniedPage("/access-denied");

        // DESACTIVO CSRF PARA LAS LLAMADAS A LA API
        http.csrf().ignoringAntMatchers("/api/**");

        // ACTIVO CORS PARA PERMITIR PETICIONES DESDE FLASK (PUERTO 5000)
        http.cors();
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
     * @return Servicio de detalles de usuario configurado
     */
    @Bean
    @Override
    public UserDetailsService userDetailsService() {
        // CREO USUARIOS EN MEMORIA PARA PRUEBAS
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("password"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("USER", "ADMIN")
                .build();

        // DEVUELVO UN GESTOR DE USUARIOS EN MEMORIA
        return new InMemoryUserDetailsManager(user, admin);
    }

    /**
     * Filtro CORS global para permitir llamadas desde localhost:5000 (Flask)
     *
     * @return Instancia del filtro CORS configurado
     */
    @Bean
    public CorsFilter corsFilter() {
        // CONFIGURO EL FILTRO CORS PARA PERMITIR PETICIONES DESDE EL BACKEND FLASK
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.addAllowedOrigin("http://localhost:5000");
        config.addAllowedMethod("*");
        config.addAllowedHeader("*");
        config.setAllowCredentials(true);
        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}
