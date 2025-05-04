package es.ubu.baloncesto.service;

import es.ubu.baloncesto.model.Equipo;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de servicio para la entidad Equipo.
 * Proporciona métodos para manipular y consultar equipos.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */
public interface EquipoService {

    /**
     * Obtiene todos los equipos.
     *
     * @return Lista de equipos
     */
    List<Equipo> findAll();

    /**
     * Obtiene un equipo por su ID.
     *
     * @param id ID del equipo
     * @return Equipo si existe, Optional vacío si no
     */
    Optional<Equipo> findById(Long id);

    /**
     * Busca equipos por nombre.
     *
     * @param nombre Nombre o parte del nombre del equipo
     * @return Lista de equipos que coinciden con el criterio
     */
    List<Equipo> findByNombreContaining(String nombre);

    /**
     * Guarda un equipo.
     *
     * @param equipo Equipo a guardar
     * @return Equipo guardado
     */
    Equipo save(Equipo equipo);

    /**
     * Elimina un equipo por su ID.
     *
     * @param id ID del equipo
     */
    void deleteById(Long id);

    /**
     * Actualiza la información de un equipo.
     *
     * @param id ID del equipo
     * @param equipoDetails Detalles actualizados del equipo
     * @return Equipo actualizado
     */
    Equipo update(Long id, Equipo equipoDetails);

    /**
     * Registra una victoria para un equipo.
     *
     * @param id ID del equipo
     * @return Equipo actualizado
     */
    Equipo registrarVictoria(Long id);

    /**
     * Registra una derrota para un equipo.
     *
     * @param id ID del equipo
     * @return Equipo actualizado
     */
    Equipo registrarDerrota(Long id);
}