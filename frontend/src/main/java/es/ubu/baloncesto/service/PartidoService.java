package es.ubu.baloncesto.service;

import es.ubu.baloncesto.model.Equipo;
import es.ubu.baloncesto.model.Partido;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Interfaz que define las operaciones de servicio para la entidad Partido.
 * Proporciona métodos para manipular y consultar partidos.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */
public interface PartidoService {

    /**
     * Obtiene todos los partidos.
     *
     * @return Lista de partidos
     */
    List<Partido> findAll();

    /**
     * Obtiene un partido por su ID.
     *
     * @param id ID del partido
     * @return Partido si existe, Optional vacío si no
     */
    Optional<Partido> findById(Long id);

    /**
     * Busca partidos por equipo.
     *
     * @param equipo Equipo que participa en los partidos
     * @return Lista de partidos en los que participa el equipo
     */
    List<Partido> findByEquipo(Equipo equipo);

    /**
     * Busca partidos programados para una fecha específica.
     *
     * @param fecha Fecha de los partidos
     * @return Lista de partidos programados para esa fecha
     */
    List<Partido> findByFecha(LocalDateTime fecha);

    /**
     * Busca partidos finalizados.
     *
     * @return Lista de partidos finalizados
     */
    List<Partido> findByFinalizadoTrue();

    /**
     * Busca partidos pendientes.
     *
     * @return Lista de partidos pendientes
     */
    List<Partido> findByFinalizadoFalse();

    /**
     * Guarda un partido.
     *
     * @param partido Partido a guardar
     * @return Partido guardado
     */
    Partido save(Partido partido);

    /**
     * Elimina un partido por su ID.
     *
     * @param id ID del partido
     */
    void deleteById(Long id);

    /**
     * Registra el resultado de un partido.
     *
     * @param id ID del partido
     * @param puntosLocal Puntos anotados por el equipo local
     * @param puntosVisitante Puntos anotados por el equipo visitante
     * @return Partido actualizado
     */
    Partido registrarResultado(Long id, int puntosLocal, int puntosVisitante);
}