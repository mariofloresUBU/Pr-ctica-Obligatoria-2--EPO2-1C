package es.ubu.baloncesto.service;

import es.ubu.baloncesto.exception.DatabaseException;
import es.ubu.baloncesto.model.Equipo;
import es.ubu.baloncesto.model.Partido;
import es.ubu.baloncesto.repository.PartidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Implementación del servicio para la entidad Partido.
 * Proporciona la lógica de negocio para manipular partidos.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */
@Service
public class PartidoServiceImpl implements PartidoService {

    /**
     * Logger para registrar operaciones.
     */
    private static final Logger LOGGER = Logger.getLogger(PartidoServiceImpl.class.getName());

    /**
     * Repositorio de partidos.
     */
    private final PartidoRepository partidoRepository;

    /**
     * Constructor que inicializa el repositorio mediante inyección de dependencias.
     *
     * @param partidoRepository Repositorio de partidos
     */
    @Autowired
    public PartidoServiceImpl(PartidoRepository partidoRepository) {
        // GUARDO EL REPOSITORIO INYECTADO
        this.partidoRepository = partidoRepository;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Partido> findAll() {
        // OBTENGO TODOS LOS PARTIDOS DEL REPOSITORIO
        try {
            LOGGER.info("Obteniendo todos los partidos");
            return partidoRepository.findAll();
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al obtener todos los partidos: " + e.getMessage());
            throw new DatabaseException("Error al obtener todos los partidos", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Partido> findById(Long id) {
        // OBTENGO UN PARTIDO POR SU ID
        try {
            LOGGER.info("Buscando partido con ID: " + id);
            return partidoRepository.findById(id);
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al buscar partido con ID " + id + ": " + e.getMessage());
            throw new DatabaseException("Error al buscar partido con ID " + id, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Partido> findByEquipo(Equipo equipo) {
        // BUSCO PARTIDOS POR EQUIPO
        try {
            LOGGER.info("Buscando partidos del equipo: " + equipo.getNombre());
            return partidoRepository.findByEquipoLocalOrEquipoVisitante(equipo, equipo);
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al buscar partidos del equipo '" + equipo.getNombre() + "': " + e.getMessage());
            throw new DatabaseException("Error al buscar partidos del equipo '" + equipo.getNombre() + "'", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Partido> findByFecha(LocalDateTime fecha) {
        // BUSCO PARTIDOS POR FECHA
        try {
            LOGGER.info("Buscando partidos con fecha: " + fecha);

            // OBTENGO EL INICIO Y FIN DEL DÍA
            LocalDateTime inicioDia = fecha.toLocalDate().atStartOfDay();
            LocalDateTime finDia = fecha.toLocalDate().atTime(23, 59, 59);

            // BUSCO PARTIDOS EN ESE RANGO
            return partidoRepository.findByFechaBetween(inicioDia, finDia);
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al buscar partidos con fecha " + fecha + ": " + e.getMessage());
            throw new DatabaseException("Error al buscar partidos con fecha " + fecha, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Partido> findByFinalizadoTrue() {
        // BUSCO PARTIDOS FINALIZADOS
        try {
            LOGGER.info("Buscando partidos finalizados");
            return partidoRepository.findByFinalizadoTrue();
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al buscar partidos finalizados: " + e.getMessage());
            throw new DatabaseException("Error al buscar partidos finalizados", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Partido> findByFinalizadoFalse() {
        // BUSCO PARTIDOS PENDIENTES
        try {
            LOGGER.info("Buscando partidos pendientes");
            return partidoRepository.findByFinalizadoFalse();
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al buscar partidos pendientes: " + e.getMessage());
            throw new DatabaseException("Error al buscar partidos pendientes", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Partido save(Partido partido) {
        // GUARDO UN NUEVO PARTIDO
        try {
            LOGGER.info("Guardando partido entre " + partido.getEquipoLocal().getNombre() +
                    " y " + partido.getEquipoVisitante().getNombre());
            return partidoRepository.save(partido);
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al guardar partido: " + e.getMessage());
            throw new DatabaseException("Error al guardar partido", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public void deleteById(Long id) {
        // ELIMINO UN PARTIDO POR SU ID
        try {
            LOGGER.info("Eliminando partido con ID: " + id);
            partidoRepository.deleteById(id);
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al eliminar partido con ID " + id + ": " + e.getMessage());
            throw new DatabaseException("Error al eliminar partido con ID " + id, e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Partido registrarResultado(Long id, int puntosLocal, int puntosVisitante) {
        // REGISTRO EL RESULTADO DE UN PARTIDO
        try {
            LOGGER.info("Registrando resultado para partido con ID: " + id);

            // BUSCO EL PARTIDO
            Optional<Partido> partidoOpt = partidoRepository.findById(id);

            if (partidoOpt.isEmpty()) {
                // SI EL PARTIDO NO EXISTE, LANZO UNA EXCEPCIÓN
                LOGGER.warning("No se encontró partido con ID: " + id);
                throw new DatabaseException("No se encontró partido con ID: " + id);
            }

            Partido partido = partidoOpt.get();

            // VERIFICO QUE EL PARTIDO NO ESTÉ YA FINALIZADO
            if (partido.isFinalizado()) {
                LOGGER.warning("El partido con ID " + id + " ya está finalizado");
                throw new DatabaseException("El partido con ID " + id + " ya está finalizado");
            }

            // REGISTRO EL RESULTADO
            partido.registrarResultado(puntosLocal, puntosVisitante);

            // GUARDO LOS CAMBIOS
            return partidoRepository.save(partido);
        } catch (DatabaseException e) {
            // PROPAGO LA EXCEPCIÓN
            throw e;
        } catch (Exception e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            LOGGER.severe("Error al registrar resultado para partido con ID " + id + ": " + e.getMessage());
            throw new DatabaseException("Error al registrar resultado para partido con ID " + id, e);
        }
    }
}