package es.ubu.baloncesto.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Manejador global de excepciones para la aplicación.
 * Esta clase intercepta todas las excepciones no manejadas y proporciona
 * respuestas apropiadas según el tipo de excepción y el tipo de solicitud.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Logger para registrar las excepciones.
     */
    private static final Logger LOGGER = Logger.getLogger(GlobalExceptionHandler.class.getName());

    /**
     * Maneja excepciones relacionadas con archivos.
     *
     * @param ex Excepción capturada
     * @param request Solicitud HTTP
     * @return Vista o respuesta JSON según el tipo de solicitud
     */
    @ExceptionHandler(FileException.class)
    public Object handleFileException(FileException ex, HttpServletRequest request) {
        // REGISTRO LA EXCEPCIÓN EN EL LOG
        LOGGER.log(Level.SEVERE, "Error de archivo: " + ex.getMessage(), ex);

        // COMPRUEBO SI LA SOLICITUD ESPERA UNA RESPUESTA JSON
        if (isAjaxRequest(request)) {
            // PARA SOLICITUDES AJAX, DEVUELVO UNA RESPUESTA JSON
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Error de archivo");
            response.put("message", ex.getMessage());
            response.put("file", ex.getFilePath());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } else {
            // PARA SOLICITUDES NORMALES, DEVUELVO UNA VISTA DE ERROR
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("errorTitle", "Error de archivo");
            mav.addObject("errorMessage", ex.getMessage());
            mav.addObject("errorDetails", "Archivo: " + ex.getFilePath());
            mav.addObject("timestamp", System.currentTimeMillis());
            mav.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return mav;
        }
    }

    /**
     * Maneja excepciones relacionadas con la base de datos.
     *
     * @param ex Excepción capturada
     * @param request Solicitud HTTP
     * @return Vista o respuesta JSON según el tipo de solicitud
     */
    @ExceptionHandler(DatabaseException.class)
    public Object handleDatabaseException(DatabaseException ex, HttpServletRequest request) {
        // REGISTRO LA EXCEPCIÓN EN EL LOG
        LOGGER.log(Level.SEVERE, "Error de base de datos: " + ex.getMessage(), ex);

        // COMPRUEBO SI LA SOLICITUD ESPERA UNA RESPUESTA JSON
        if (isAjaxRequest(request)) {
            // PARA SOLICITUDES AJAX, DEVUELVO UNA RESPUESTA JSON
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Error de base de datos");
            response.put("message", ex.getMessage());
            if (ex.getSqlCode() != null) {
                response.put("sqlCode", ex.getSqlCode());
            }

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } else {
            // PARA SOLICITUDES NORMALES, DEVUELVO UNA VISTA DE ERROR
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("errorTitle", "Error de base de datos");
            mav.addObject("errorMessage", ex.getMessage());
            if (ex.getSqlCode() != null) {
                mav.addObject("errorDetails", "SQL: " + ex.getSqlCode());
            }
            mav.addObject("timestamp", System.currentTimeMillis());
            mav.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return mav;
        }
    }

    /**
     * Maneja excepciones relacionadas con llamadas a APIs de terceros.
     *
     * @param ex Excepción capturada
     * @param request Solicitud HTTP
     * @return Vista o respuesta JSON según el tipo de solicitud
     */
    @ExceptionHandler(ApiException.class)
    public Object handleApiException(ApiException ex, HttpServletRequest request) {
        // REGISTRO LA EXCEPCIÓN EN EL LOG
        LOGGER.log(Level.SEVERE, "Error de API: " + ex.getMessage(), ex);

        // DETERMINO EL CÓDIGO DE ESTADO HTTP A DEVOLVER
        HttpStatus status = HttpStatus.BAD_GATEWAY; // POR DEFECTO, ERROR DE PUERTA DE ENLACE
        if (ex.getStatusCode() != null) {
            if (ex.getStatusCode() == 404) {
                status = HttpStatus.NOT_FOUND;
            } else if (ex.getStatusCode() == 401 || ex.getStatusCode() == 403) {
                status = HttpStatus.FORBIDDEN;
            } else if (ex.getStatusCode() >= 500) {
                status = HttpStatus.BAD_GATEWAY;
            }
        }

        // COMPRUEBO SI LA SOLICITUD ESPERA UNA RESPUESTA JSON
        if (isAjaxRequest(request)) {
            // PARA SOLICITUDES AJAX, DEVUELVO UNA RESPUESTA JSON
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Error de API");
            response.put("message", ex.getMessage());
            response.put("apiUrl", ex.getApiUrl());
            if (ex.getStatusCode() != null) {
                response.put("statusCode", ex.getStatusCode());
            }

            return ResponseEntity.status(status).body(response);
        } else {
            // PARA SOLICITUDES NORMALES, DEVUELVO UNA VISTA DE ERROR
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("errorTitle", "Error de API");
            mav.addObject("errorMessage", ex.getMessage());
            StringBuilder details = new StringBuilder("API: ").append(ex.getApiUrl());
            if (ex.getStatusCode() != null) {
                details.append(", Estado: ").append(ex.getStatusCode());
            }
            mav.addObject("errorDetails", details.toString());
            mav.addObject("timestamp", System.currentTimeMillis());
            mav.addObject("status", status.value());

            return mav;
        }
    }

    /**
     * Maneja cualquier excepción no controlada por los otros manejadores.
     *
     * @param ex Excepción capturada
     * @param request Solicitud HTTP
     * @return Vista o respuesta JSON según el tipo de solicitud
     */
    @ExceptionHandler(Exception.class)
    public Object handleGenericException(Exception ex, HttpServletRequest request) {
        // REGISTRO LA EXCEPCIÓN EN EL LOG
        LOGGER.log(Level.SEVERE, "Error inesperado: " + ex.getMessage(), ex);

        // COMPRUEBO SI LA SOLICITUD ESPERA UNA RESPUESTA JSON
        if (isAjaxRequest(request)) {
            // PARA SOLICITUDES AJAX, DEVUELVO UNA RESPUESTA JSON
            Map<String, Object> response = new HashMap<>();
            response.put("error", "Error interno");
            response.put("message", "Ha ocurrido un error inesperado. Por favor, inténtelo de nuevo más tarde.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        } else {
            // PARA SOLICITUDES NORMALES, DEVUELVO UNA VISTA DE ERROR
            ModelAndView mav = new ModelAndView("error");
            mav.addObject("errorTitle", "Error interno");
            mav.addObject("errorMessage", "Ha ocurrido un error inesperado. Por favor, inténtelo de nuevo más tarde.");
            mav.addObject("errorDetails", ex.getClass().getSimpleName() + ": " + ex.getMessage());
            mav.addObject("timestamp", System.currentTimeMillis());
            mav.addObject("status", HttpStatus.INTERNAL_SERVER_ERROR.value());

            return mav;
        }
    }

    /**
     * Determina si la solicitud espera una respuesta JSON (AJAX).
     *
     * @param request Solicitud HTTP
     * @return true si es una solicitud AJAX, false en caso contrario
     */
    private boolean isAjaxRequest(HttpServletRequest request) {
        // COMPRUEBO SI LA SOLICITUD TIENE LA CABECERA X-REQUESTED-WITH
        String requestedWith = request.getHeader("X-Requested-With");
        // TAMBIÉN COMPRUEBO SI LA SOLICITUD ACEPTA JSON
        String accept = request.getHeader("Accept");

        // ES UNA SOLICITUD AJAX SI TIENE LA CABECERA X-REQUESTED-WITH O ACEPTA JSON
        return "XMLHttpRequest".equals(requestedWith) ||
                (accept != null && accept.contains("application/json"));
    }
}