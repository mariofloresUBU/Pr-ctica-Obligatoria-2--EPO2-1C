package es.ubu.baloncesto.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * Controlador que maneja las operaciones relacionadas con la autenticación.
 * Gestiona el login, logout y recuperación de contraseña.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */
@Controller
public class LoginController {

    /**
     * Maneja las solicitudes a la página de login.
     * Si el usuario ya está autenticado, lo redirige a la página principal.
     *
     * @param model Modelo para pasar datos a la vista
     * @param error Parámetro opcional que indica si ha habido un error de autenticación
     * @param logout Parámetro opcional que indica si el usuario ha cerrado sesión
     * @return Nombre de la vista a mostrar (login.html) o redirección a la página principal
     */
    @GetMapping("/login")
    public String login(Model model, String error, String logout) {
        // COMPRUEBO SI EL USUARIO YA ESTÁ AUTENTICADO
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth instanceof AnonymousAuthenticationToken)) {
            // SI EL USUARIO YA ESTÁ AUTENTICADO, LO REDIRIJO A LA PÁGINA PRINCIPAL
            return "redirect:/";
        }

        // SI HAY UN MENSAJE DE ERROR, LO AÑADO AL MODELO
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos. Por favor, inténtelo de nuevo.");
        }

        // SI HAY UN MENSAJE DE LOGOUT, LO AÑADO AL MODELO
        if (logout != null) {
            model.addAttribute("message", "Ha cerrado sesión correctamente.");
        }

        // DEVUELVO LA VISTA DE LOGIN
        return "login";
    }

    /**
     * Maneja las solicitudes a la página de acceso denegado.
     *
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la vista a mostrar (access-denied.html)
     */
    @GetMapping("/access-denied")
    public String accessDenied(Model model) {
        // AÑADO UN MENSAJE DE ERROR AL MODELO
        model.addAttribute("errorMessage", "No tiene permiso para acceder a esta página.");

        // DEVUELVO LA VISTA DE ACCESO DENEGADO
        // NOTA: NECESITARÁS CREAR ESTE ARCHIVO HTML EN LA CARPETA TEMPLATES
        return "access-denied";
    }
}