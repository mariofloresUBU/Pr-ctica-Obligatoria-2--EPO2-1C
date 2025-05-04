package es.ubu.baloncesto.repository;

import es.ubu.baloncesto.model.Equipo;
import es.ubu.baloncesto.model.Partido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Repositorio para la entidad Partido.
 * Proporciona métodos de acceso a la base de datos para partidos.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */
@Repository
public interface PartidoRepository extends JpaRepository<Partido, Long> {

    /**
     * Busca partidos donde un equipo participa como local o visitante.
     *
     * @param local Equipo local
     * @param visitante Equipo visitante (en la consulta, se busca el mismo equipo)
     * @return Lista de partidos donde el equipo participa
     */
    List<Partido> findByEquipoLocalOrEquipoVisitante(Equipo local, Equipo visitante);

    /**
     * Busca partidos donde un equipo específico juega como local.
     *
     * @param equipo Equipo local
     * @return Lista de partidos donde el equipo juega como local
     */
    List<Partido> findByEquipoLocal(Equipo equipo);

    /**
     * Busca partidos donde un equipo específico juega como visitante.
     *
     * @param equipo Equipo visitante
     * @return Lista de partidos donde el equipo juega como visitante
     */
    List<Partido> findByEquipoVisitante(Equipo equipo);

    /**
     * Busca partidos programados para una fecha específica.
     *
     * @param fecha Fecha exacta del partido
     * @return Lista de partidos programados para esa fecha
     */
    List<Partido> findByFecha(LocalDateTime fecha);

    /**
     * Busca partidos programados dentro de un rango de fechas.
     *
     * @param inicio Fecha de inicio del rango
     * @param fin Fecha de fin del rango
     * @return Lista de partidos programados dentro del rango
     */
    List<Partido> findByFechaBetween(LocalDateTime inicio, LocalDateTime fin);

    /**
     * Busca partidos que ya han finalizado.
     *
     * @return Lista de partidos finalizados
     */
    List<Partido> findByFinalizadoTrue();

    /**
     * Busca partidos que aún no han finalizado.
     *
     * @return Lista de partidos pendientes
     */
    List<Partido> findByFinalizadoFalse();

    /**
     * Encuentra partidos ordenados por fecha (ascendente).
     *
     * @return Lista de partidos ordenados por fecha
     */
    List<Partido> findAllByOrderByFechaAsc();
}