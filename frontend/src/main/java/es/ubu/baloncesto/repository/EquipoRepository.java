package es.ubu.baloncesto.repository;

import es.ubu.baloncesto.model.Equipo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositorio para la entidad Equipo.
 * Proporciona métodos de acceso a la base de datos para equipos.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */
@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Long> {

    /**
     * Busca equipos cuyo nombre contiene la cadena especificada.
     *
     * @param nombre Cadena a buscar en el nombre del equipo
     * @return Lista de equipos que cumplen con el criterio
     */
    List<Equipo> findByNombreContaining(String nombre);

    /**
     * Busca un equipo por su nombre exacto.
     *
     * @param nombre Nombre exacto del equipo
     * @return Equipo encontrado o null si no existe
     */
    Equipo findByNombre(String nombre);

    /**
     * Busca equipos por la ciudad a la que pertenecen.
     *
     * @param ciudad Ciudad a buscar
     * @return Lista de equipos de esa ciudad
     */
    List<Equipo> findByCiudad(String ciudad);

    /**
     * Encuentra equipos ordenados por número de victorias (descendente).
     *
     * @return Lista de equipos ordenados por victorias
     */
    List<Equipo> findAllByOrderByVictoriasDesc();
}