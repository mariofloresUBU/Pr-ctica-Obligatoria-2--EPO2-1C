package es.ubu.baloncesto.controller;

import es.ubu.baloncesto.exception.DatabaseException;
import es.ubu.baloncesto.model.Equipo;
import es.ubu.baloncesto.model.Partido;
import es.ubu.baloncesto.service.EquipoService;
import es.ubu.baloncesto.service.PartidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Controlador para la gestión de partidos.
 * Maneja las operaciones CRUD para partidos y su visualización.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */
@Controller
@RequestMapping("/partidos")
public class PartidoController {

    /**
     * Logger para registrar operaciones.
     */
    private static final Logger LOGGER = Logger.getLogger(PartidoController.class.getName());

    /**
     * Servicio de partidos.
     */
    private final PartidoService partidoService;

    /**
     * Servicio de equipos.
     */
    private final EquipoService equipoService;

    /**
     * Constructor que inicializa los servicios mediante inyección de dependencias.
     *
     * @param partidoService Servicio de partidos
     * @param equipoService Servicio de equipos
     */
    @Autowired
    public PartidoController(PartidoService partidoService, EquipoService equipoService) {
        // GUARDO LOS SERVICIOS INYECTADOS
        this.partidoService = partidoService;
        this.equipoService = equipoService;
    }

    /**
     * Muestra la lista de partidos.
     *
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la vista a mostrar (partidos.html)
     */
    @GetMapping
    public String listarPartidos(Model model) {
        // OBTENGO TODOS LOS PARTIDOS
        List<Partido> partidos = partidoService.findAll();

        // AÑADO LOS PARTIDOS AL MODELO
        model.addAttribute("partidos", partidos);
        model.addAttribute("titulo", "Lista de Partidos");

        // DEVUELVO LA VISTA DE PARTIDOS
        return "partidos";
    }

    /**
     * Muestra el formulario para crear un nuevo partido.
     *
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la vista a mostrar (form-partido.html)
     */
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        // CREO UN PARTIDO VACÍO
        Partido partido = new Partido();

        // OBTENGO TODOS LOS EQUIPOS PARA EL FORMULARIO
        List<Equipo> equipos = equipoService.findAll();

        // AÑADO LOS DATOS AL MODELO
        model.addAttribute("partido", partido);
        model.addAttribute("equipos", equipos);
        model.addAttribute("titulo", "Nuevo Partido");
        model.addAttribute("accion", "guardar");

        // DEVUELVO LA VISTA DEL FORMULARIO
        return "form-partido";
    }

    /**
     * Guarda un nuevo partido.
     *
     * @param equipoLocalId ID del equipo local
     * @param equipoVisitanteId ID del equipo visitante
     * @param fecha Fecha y hora del partido
     * @return Redirección a la lista de partidos
     */
    @PostMapping("/guardar")
    public String guardarPartido(
            @RequestParam("equipoLocalId") Long equipoLocalId,
            @RequestParam("equipoVisitanteId") Long equipoVisitanteId,
            @RequestParam("fecha") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fecha) {

        try {
            // VERIFICO QUE LOS EQUIPOS NO SEAN EL MISMO
            if (equipoLocalId.equals(equipoVisitanteId)) {
                throw new IllegalArgumentException("El equipo local y visitante no pueden ser el mismo");
            }

            // OBTENGO LOS EQUIPOS
            Optional<Equipo> equipoLocalOpt = equipoService.findById(equipoLocalId);
            Optional<Equipo> equipoVisitanteOpt = equipoService.findById(equipoVisitanteId);

            // VERIFICO QUE AMBOS EQUIPOS EXISTAN
            if (equipoLocalOpt.isEmpty() || equipoVisitanteOpt.isEmpty()) {
                throw new DatabaseException("No se encontró uno de los equipos");
            }

            // CREO EL PARTIDO
            Partido partido = new Partido();
            partido.setEquipoLocal(equipoLocalOpt.get());
            partido.setEquipoVisitante(equipoVisitanteOpt.get());
            partido.setFecha(fecha);

            // GUARDO EL PARTIDO
            partidoService.save(partido);

            // REDIRIJO A LA LISTA DE PARTIDOS
            return "redirect:/partidos";
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al guardar partido: " + e.getMessage());
            throw new DatabaseException("Error al guardar partido: " + e.getMessage(), e);
        }
    }

    /**
     * Muestra el formulario para registrar el resultado de un partido.
     *
     * @param id ID del partido
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la vista a mostrar (form-resultado.html)
     */
    @GetMapping("/resultado/{id}")
    public String mostrarFormularioResultado(@PathVariable Long id, Model model) {
        // BUSCO EL PARTIDO
        Optional<Partido> partidoOpt = partidoService.findById(id);

        if (partidoOpt.isPresent()) {
            // SI EL PARTIDO EXISTE, LO AÑADO AL MODELO
            Partido partido = partidoOpt.get();

            model.addAttribute("partido", partido);
            model.addAttribute("titulo", "Registrar Resultado");

            // DEVUELVO LA VISTA DEL FORMULARIO
            return "form-resultado";
        } else {
            // SI EL PARTIDO NO EXISTE, LANZO UNA EXCEPCIÓN
            throw new DatabaseException("No se encontró el partido con ID: " + id);
        }
    }

    /**
     * Registra el resultado de un partido.
     *
     * @param id ID del partido
     * @param puntosLocal Puntos anotados por el equipo local
     * @param puntosVisitante Puntos anotados por el equipo visitante
     * @return Redirección a la lista de partidos
     */
    @PostMapping("/resultado/{id}")
    public String registrarResultado(
            @PathVariable Long id,
            @RequestParam("puntosLocal") int puntosLocal,
            @RequestParam("puntosVisitante") int puntosVisitante) {

        try {
            // VERIFICO QUE LOS PUNTOS SEAN POSITIVOS
            if (puntosLocal < 0 || puntosVisitante < 0) {
                throw new IllegalArgumentException("Los puntos no pueden ser negativos");
            }

            // REGISTRO EL RESULTADO
            partidoService.registrarResultado(id, puntosLocal, puntosVisitante);

            // REDIRIJO A LA LISTA DE PARTIDOS
            return "redirect:/partidos";
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al registrar resultado: " + e.getMessage());
            throw new DatabaseException("Error al registrar resultado: " + e.getMessage(), e);
        }
    }

    /**
     * Elimina un partido.
     *
     * @param id ID del partido
     * @return Redirección a la lista de partidos
     */
    @GetMapping("/eliminar/{id}")
    public String eliminarPartido(@PathVariable Long id) {
        try {
            // ELIMINO EL PARTIDO
            partidoService.deleteById(id);

            // REDIRIJO A LA LISTA DE PARTIDOS
            return "redirect:/partidos";
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al eliminar partido: " + e.getMessage());
            throw new DatabaseException("Error al eliminar partido: " + e.getMessage(), e);
        }
    }
}