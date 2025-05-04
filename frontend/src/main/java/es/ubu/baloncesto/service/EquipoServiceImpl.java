package es.ubu.baloncesto.service;

import es.ubu.baloncesto.exception.DatabaseException;
import es.ubu.baloncesto.model.Equipo;
import es.ubu.baloncesto.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Implementación del servicio para la entidad Equipo.
 * Proporciona la lógica de negocio para manipular equipos.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */
@Service
public class EquipoServiceImpl implements EquipoService {

    /**
     * Logger para registrar operaciones.
     */
    private static final Logger LOGGER = Logger.getLogger(EquipoServiceImpl.class.getName());

    /**
     * Repositorio de equipos.
     */
    private final EquipoRepository equipoRepository;

    /**
     * Constructor que inicializa el repositorio mediante inyección de dependencias.
     *
     * @param equipoRepository Repositorio de equipos
     */
    @Autowired
    public EquipoServiceImpl(EquipoRepository equipoRepository) {
        // GUARDO EL REPOSITORIO INYECTADO
        this.equipoRepository = equipoRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Equipo> findAll() {
        // OBTENGO TODOS LOS EQUIPOS DEL REPOSITORIO
        try {
            LOGGER.info("Obteniendo todos los equipos");
            return equipoRepository.findAll();
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al obtener todos los equipos: " + e.getMessage());
            throw new DatabaseException("Error al obtener todos los equipos", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Equipo> findById(Long id) {
        // OBTENGO UN EQUIPO POR SU ID
        try {
            LOGGER.info("Buscando equipo con ID: " + id);
            return equipoRepository.findById(id);
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al buscar equipo con ID " + id + ": " + e.getMessage());
            throw new DatabaseException("Error al buscar equipo con ID " + id, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Equipo> findByNombreContaining(String nombre) {
        // BUSCO EQUIPOS POR NOMBRE
        try {
            LOGGER.info("Buscando equipos con nombre que contiene: " + nombre);
            return equipoRepository.findByNombreContaining(nombre);
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al buscar equipos por nombre '" + nombre + "': " + e.getMessage());
            throw new DatabaseException("Error al buscar equipos por nombre '" + nombre + "'", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Equipo save(Equipo equipo) {
        // GUARDO UN NUEVO EQUIPO
        try {
            LOGGER.info("Guardando equipo: " + equipo.getNombre());
            return equipoRepository.save(equipo);
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al guardar equipo '" + equipo.getNombre() + "': " + e.getMessage());
            throw new DatabaseException("Error al guardar equipo '" + equipo.getNombre() + "'", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        // ELIMINO UN EQUIPO POR SU ID
        try {
            LOGGER.info("Eliminando equipo con ID: " + id);
            equipoRepository.deleteById(id);
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al eliminar equipo con ID " + id + ": " + e.getMessage());
            throw new DatabaseException("Error al eliminar equipo con ID " + id, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Equipo update(Long id, Equipo equipoDetails) {
        // ACTUALIZO UN EQUIPO EXISTENTE
        try {
            LOGGER.info("Actualizando equipo con ID: " + id);

            // BUSCO EL EQUIPO A ACTUALIZAR
            Optional<Equipo> equipoOpt = equipoRepository.findById(id);

            if (equipoOpt.isPresent()) {
                // SI EL EQUIPO EXISTE, LO ACTUALIZO
                Equipo equipo = equipoOpt.get();
                equipo.setNombre(equipoDetails.getNombre());
                equipo.setCiudad(equipoDetails.getCiudad());
                equipo.setEntrenador(equipoDetails.getEntrenador());

                // GUARDO LOS CAMBIOS
                return equipoRepository.save(equipo);
            } else {
                // SI EL EQUIPO NO EXISTE, LANZO UNA EXCEPCIÓN
                LOGGER.warning("No se encontró equipo con ID: " + id);
                throw new DatabaseException("No se encontró equipo con ID: " + id);
            }
        } catch (DatabaseException e) {
            // PROPAGO LA EXCEPCIÓN
            throw e;
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al actualizar equipo con ID " + id + ": " + e.getMessage());
            throw new DatabaseException("Error al actualizar equipo con ID " + id, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Equipo registrarVictoria(Long id) {
        // REGISTRO UNA VICTORIA PARA UN EQUIPO
        try {
            LOGGER.info("Registrando victoria para equipo con ID: " + id);

            // BUSCO EL EQUIPO
            Optional<Equipo> equipoOpt = equipoRepository.findById(id);

            if (equipoOpt.isPresent()) {
                // SI EL EQUIPO EXISTE, INCREMENTO SUS VICTORIAS
                Equipo equipo = equipoOpt.get();
                equipo.registrarVictoria();

                // GUARDO LOS CAMBIOS
                return equipoRepository.save(equipo);
            } else {
                // SI EL EQUIPO NO EXISTE, LANZO UNA EXCEPCIÓN
                LOGGER.warning("No se encontró equipo con ID: " + id);
                throw new DatabaseException("No se encontró equipo con ID: " + id);
            }
        } catch (DatabaseException e) {
            // PROPAGO LA EXCEPCIÓN
            throw e;
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al registrar victoria para equipo con ID " + id + ": " + e.getMessage());
            throw new DatabaseException("Error al registrar victoria para equipo con ID " + id, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Equipo registrarDerrota(Long id) {
        // REGISTRO UNA DERROTA PARA UN EQUIPO
        try {
            LOGGER.info("Registrando derrota para equipo con ID: " + id);

            // BUSCO EL EQUIPO
            Optional<Equipo> equipoOpt = equipoRepository.findById(id);

            if (equipoOpt.isPresent()) {
                // SI EL EQUIPO EXISTE, INCREMENTO SUS DERROTAS
                Equipo equipo = equipoOpt.get();
                equipo.registrarDerrota();

                // GUARDO LOS CAMBIOS
                return equipoRepository.save(equipo);
            } else {
                // SI EL EQUIPO NO EXISTE, LANZO UNA EXCEPCIÓN
                LOGGER.warning("No se encontró equipo con ID: " + id);
                throw new DatabaseException("No se encontró equipo con ID: " + id);
            }
        } catch (DatabaseException e) {
            // PROPAGO LA EXCEPCIÓN
            throw e;
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al registrar derrota para equipo con ID " + id + ": " + e.getMessage());
            throw new DatabaseException("Error al registrar derrota para equipo con ID " + id, e);
        }
    }
}