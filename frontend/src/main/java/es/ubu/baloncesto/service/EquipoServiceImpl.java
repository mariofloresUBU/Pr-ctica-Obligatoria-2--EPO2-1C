package es.ubu.baloncesto.service;

import es.ubu.baloncesto.exception.DatabaseException;
import es.ubu.baloncesto.model.Equipo;
import es.ubu.baloncesto.repository.EquipoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Implementación del servicio para la entidad Equipo.
 * Conecta con la API Flask para obtener datos de equipos.
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
     * URL de la API de Flask.
     */
    private final String apiUrl = "http://localhost:5000/api";

    /**
     * Cliente HTTP para hacer peticiones a la API.
     */
    private final RestTemplate restTemplate;

    /**
     * Repositorio de equipos (usado como respaldo cuando la API no está disponible).
     */
    private final EquipoRepository equipoRepository;

    /**
     * Constructor que inicializa el repositorio y el cliente HTTP.
     *
     * @param equipoRepository Repositorio de equipos
     */
    @Autowired
    public EquipoServiceImpl(EquipoRepository equipoRepository) {
        this.equipoRepository = equipoRepository;
        this.restTemplate = new RestTemplate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Equipo> findAll() {
        try {
            LOGGER.info("Obteniendo todos los equipos desde la API Flask");

            // Hacer petición HTTP a la API Flask
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    apiUrl + "/equipos",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            // Convertir respuesta a objetos Equipo
            List<Equipo> equipos = new ArrayList<>();
            if (response.getBody() != null) {
                for (Map<String, Object> equipoMap : response.getBody()) {
                    Equipo equipo = mapToEquipo(equipoMap);
                    equipos.add(equipo);
                }
            }

            LOGGER.info("Se obtuvieron " + equipos.size() + " equipos desde la API Flask");
            return equipos;
        } catch (Exception e) {
            // Si hay un error con la API, intentar con el repositorio local
            LOGGER.warning("Error al obtener equipos desde la API Flask: " + e.getMessage());
            LOGGER.info("Intentando obtener equipos desde el repositorio local");

            try {
                return equipoRepository.findAll();
            } catch (Exception ex) {
                LOGGER.severe("Error al obtener equipos desde el repositorio local: " + ex.getMessage());
                throw new DatabaseException("Error al obtener todos los equipos", ex);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Equipo> findById(Long id) {
        try {
            LOGGER.info("Buscando equipo con ID: " + id + " en la API Flask");

            // Hacer petición HTTP a la API Flask
            String url = apiUrl + "/equipos/" + id;
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    url,
                    Map.class
            );

            // Convertir respuesta a objeto Equipo
            if (response.getBody() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> equipoMap = response.getBody();
                Equipo equipo = mapToEquipo(equipoMap);
                return Optional.of(equipo);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            // Si hay un error con la API, intentar con el repositorio local
            LOGGER.warning("Error al buscar equipo con ID " + id + " en la API Flask: " + e.getMessage());
            LOGGER.info("Intentando buscar equipo en el repositorio local");

            try {
                return equipoRepository.findById(id);
            } catch (Exception ex) {
                LOGGER.severe("Error al buscar equipo con ID " + id + " en el repositorio local: " + ex.getMessage());
                throw new DatabaseException("Error al buscar equipo con ID " + id, ex);
            }
        }
    }

    /**
     * Convierte un mapa de datos a un objeto Equipo.
     *
     * @param equipoMap Mapa con datos del equipo
     * @return Objeto Equipo
     */
    private Equipo mapToEquipo(Map<String, Object> equipoMap) {
        Equipo equipo = new Equipo();

        // Extraer el ID como Long
        if (equipoMap.containsKey("id")) {
            Object idObj = equipoMap.get("id");
            if (idObj instanceof Integer) {
                equipo.setId(((Integer) idObj).longValue());
            } else if (idObj instanceof Long) {
                equipo.setId((Long) idObj);
            } else if (idObj instanceof String) {
                equipo.setId(Long.parseLong((String) idObj));
            }
        }

        // Extraer otros campos
        if (equipoMap.containsKey("nombre")) {
            equipo.setNombre((String) equipoMap.get("nombre"));
        }

        if (equipoMap.containsKey("ciudad")) {
            equipo.setCiudad((String) equipoMap.get("ciudad"));
        }

        if (equipoMap.containsKey("entrenador")) {
            equipo.setEntrenador((String) equipoMap.get("entrenador"));
        }

        return equipo;
    }

    // El resto de métodos se mantienen igual utilizando el equipoRepository

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