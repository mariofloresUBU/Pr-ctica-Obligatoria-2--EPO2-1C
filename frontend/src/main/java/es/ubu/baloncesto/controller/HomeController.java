package es.ubu.baloncesto.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controlador para la página principal de la aplicación.
 * Maneja las solicitudes a la raíz de la aplicación y muestra la página de inicio.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */
@Controller
public class HomeController {

    /**
     * Maneja las solicitudes a la raíz de la aplicación.
     * Muestra la página principal con información básica.
     *
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la vista a mostrar (index.html)
     */
    @GetMapping("/")
    public String home(Model model) {
        // GENERO LA FECHA Y HORA ACTUAL PARA MOSTRARLA EN LA PÁGINA
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String formattedDateTime = now.format(formatter);

        // AÑADO LA FECHA Y HORA AL MODELO
        model.addAttribute("currentDateTime", formattedDateTime);

        // AÑADO UN MENSAJE DE BIENVENIDA
        model.addAttribute("welcomeMessage", "Bienvenido a la aplicación de resultados de baloncesto");

        // DEVUELVO EL NOMBRE DE LA VISTA (index.html EN LA CARPETA TEMPLATES)
        return "index";
    }

    /**
     * Maneja las solicitudes a /about.
     * Muestra información sobre la aplicación.
     *
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la vista a mostrar (about.html)
     */
    @GetMapping("/about")
    public String about(Model model) {
        // AÑADO INFORMACIÓN SOBRE LA APLICACIÓN AL MODELO
        model.addAttribute("appName", "Aplicación de Resultados de Baloncesto");
        model.addAttribute("appVersion", "1.0");
        model.addAttribute("appAuthor", "Mario Flores");
        model.addAttribute("appYear", LocalDateTime.now().getYear());

        // DEVUELVO EL NOMBRE DE LA VISTA (ABOUT.HTML EN LA CARPETA TEMPLATES)
        return "about";
    }
}