package es.ubu.baloncesto.service;

import es.ubu.baloncesto.exception.DatabaseException;
import es.ubu.baloncesto.model.Equipo;
import es.ubu.baloncesto.model.Partido;
import es.ubu.baloncesto.repository.PartidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

/**
 * Implementación del servicio para la entidad Partido.
 * Conecta con la API Flask para gestionar partidos.
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
     * URL de la API de Flask.
     */
    private final String apiUrl = "http://localhost:5000/api";

    /**
     * Cliente HTTP para hacer peticiones a la API.
     */
    private final RestTemplate restTemplate;

    /**
     * Repositorio de partidos (usado como respaldo cuando la API no está disponible).
     */
    private final PartidoRepository partidoRepository;

    /**
     * Constructor que inicializa el repositorio y el cliente HTTP.
     *
     * @param partidoRepository Repositorio de partidos
     */
    @Autowired
    public PartidoServiceImpl(PartidoRepository partidoRepository) {
        // GUARDO EL REPOSITORIO INYECTADO
        this.partidoRepository = partidoRepository;
        this.restTemplate = new RestTemplate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Partido> findAll() {
        try {
            LOGGER.info("Obteniendo todos los partidos desde la API Flask");

            // Hacer petición HTTP a la API Flask
            ResponseEntity<List<Map<String, Object>>> response = restTemplate.exchange(
                    apiUrl + "/partidos",
                    HttpMethod.GET,
                    null,
                    new ParameterizedTypeReference<List<Map<String, Object>>>() {}
            );

            // Convertir respuesta a objetos Partido
            List<Partido> partidos = new ArrayList<>();
            if (response.getBody() != null) {
                for (Map<String, Object> partidoMap : response.getBody()) {
                    Partido partido = mapToPartido(partidoMap);
                    partidos.add(partido);
                }
            }

            LOGGER.info("Se obtuvieron " + partidos.size() + " partidos desde la API Flask");
            return partidos;
        } catch (Exception e) {
            // Si hay un error con la API, intentar con el repositorio local
            LOGGER.warning("Error al obtener partidos desde la API Flask: " + e.getMessage());
            LOGGER.info("Intentando obtener partidos desde el repositorio local");

            try {
                return partidoRepository.findAll();
            } catch (Exception ex) {
                LOGGER.severe("Error al obtener partidos desde el repositorio local: " + ex.getMessage());
                throw new DatabaseException("Error al obtener todos los partidos", ex);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<Partido> findById(Long id) {
        try {
            LOGGER.info("Buscando partido con ID: " + id + " en la API Flask");

            // Hacer petición HTTP a la API Flask
            String url = apiUrl + "/partidos/" + id;
            ResponseEntity<Map> response = restTemplate.getForEntity(
                    url,
                    Map.class
            );

            // Convertir respuesta a objeto Partido
            if (response.getBody() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> partidoMap = response.getBody();
                Partido partido = mapToPartido(partidoMap);
                return Optional.of(partido);
            } else {
                return Optional.empty();
            }
        } catch (Exception e) {
            // Si hay un error con la API, intentar con el repositorio local
            LOGGER.warning("Error al buscar partido con ID " + id + " en la API Flask: " + e.getMessage());
            LOGGER.info("Intentando buscar partido en el repositorio local");

            try {
                return partidoRepository.findById(id);
            } catch (Exception ex) {
                LOGGER.severe("Error al buscar partido con ID " + id + " en el repositorio local: " + ex.getMessage());
                throw new DatabaseException("Error al buscar partido con ID " + id, ex);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Partido save(Partido partido) {
        try {
            LOGGER.info("Guardando partido entre " + partido.getEquipoLocal().getNombre() +
                    " y " + partido.getEquipoVisitante().getNombre() + " en la API Flask");

            // Preparar los datos para enviar a la API
            Map<String, Object> partidoData = new HashMap<>();
            partidoData.put("equipo_local_id", partido.getEquipoLocal().getId());
            partidoData.put("equipo_visitante_id", partido.getEquipoVisitante().getId());
            partidoData.put("fecha", partido.getFecha().toString());

            // Hacer petición HTTP a la API Flask
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(partidoData);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    apiUrl + "/partidos",
                    requestEntity,
                    Map.class
            );

            // Convertir respuesta a objeto Partido
            if (response.getBody() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> partidoMap = response.getBody();
                return mapToPartido(partidoMap);
            } else {
                throw new DatabaseException("No se recibió respuesta al guardar el partido");
            }
        } catch (Exception e) {
            // Si hay un error con la API, intentar con el repositorio local
            LOGGER.warning("Error al guardar partido en la API Flask: " + e.getMessage());
            LOGGER.info("Intentando guardar partido en el repositorio local");

            try {
                return partidoRepository.save(partido);
            } catch (Exception ex) {
                LOGGER.severe("Error al guardar partido en el repositorio local: " + ex.getMessage());
                throw new DatabaseException("Error al guardar partido", ex);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional
    public Partido registrarResultado(Long id, int puntosLocal, int puntosVisitante) {
        try {
            LOGGER.info("Registrando resultado para partido con ID: " + id + " en la API Flask");

            // Preparar los datos para enviar a la API
            Map<String, Object> resultadoData = new HashMap<>();
            resultadoData.put("puntos_local", puntosLocal);
            resultadoData.put("puntos_visitante", puntosVisitante);

            // Hacer petición HTTP a la API Flask
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(resultadoData);
            ResponseEntity<Map> response = restTemplate.postForEntity(
                    apiUrl + "/partidos/" + id + "/resultado",
                    requestEntity,
                    Map.class
            );

            // Convertir respuesta a objeto Partido
            if (response.getBody() != null) {
                @SuppressWarnings("unchecked")
                Map<String, Object> partidoMap = response.getBody();
                return mapToPartido(partidoMap);
            } else {
                throw new DatabaseException("No se recibió respuesta al registrar el resultado");
            }
        } catch (Exception e) {
            // Si hay un error con la API, intentar con el repositorio local
            LOGGER.warning("Error al registrar resultado en la API Flask: " + e.getMessage());
            LOGGER.info("Intentando registrar resultado en el repositorio local");

            try {
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
            } catch (Exception ex) {
                LOGGER.severe("Error al registrar resultado en el repositorio local: " + ex.getMessage());
                throw new DatabaseException("Error al registrar resultado para partido con ID " + id, ex);
            }
        }
    }

    /**
     * Convierte un mapa de datos a un objeto Partido.
     *
     * @param partidoMap Mapa con datos del partido
     * @return Objeto Partido
     */
    private Partido mapToPartido(Map<String, Object> partidoMap) {
        Partido partido = new Partido();

        // Extraer el ID como Long
        if (partidoMap.containsKey("id")) {
            Object idObj = partidoMap.get("id");
            if (idObj instanceof Integer) {
                partido.setId(((Integer) idObj).longValue());
            } else if (idObj instanceof Long) {
                partido.setId((Long) idObj);
            } else if (idObj instanceof String) {
                partido.setId(Long.parseLong((String) idObj));
            }
        }

        // Extraer la fecha
        if (partidoMap.containsKey("fecha")) {
            String fechaStr = (String) partidoMap.get("fecha");
            partido.setFecha(LocalDateTime.parse(fechaStr));
        }

        // Extraer equipos
        if (partidoMap.containsKey("equipo_local")) {
            Equipo equipoLocal = new Equipo();
            if (partidoMap.containsKey("equipo_local_id")) {
                Object idObj = partidoMap.get("equipo_local_id");
                if (idObj instanceof Integer) {
                    equipoLocal.setId(((Integer) idObj).longValue());
                } else if (idObj instanceof Long) {
                    equipoLocal.setId((Long) idObj);
                }
            }
            equipoLocal.setNombre((String) partidoMap.get("equipo_local"));
            partido.setEquipoLocal(equipoLocal);
        }

        if (partidoMap.containsKey("equipo_visitante")) {
            Equipo equipoVisitante = new Equipo();
            if (partidoMap.containsKey("equipo_visitante_id")) {
                Object idObj = partidoMap.get("equipo_visitante_id");
                if (idObj instanceof Integer) {
                    equipoVisitante.setId(((Integer) idObj).longValue());
                } else if (idObj instanceof Long) {
                    equipoVisitante.setId((Long) idObj);
                }
            }
            equipoVisitante.setNombre((String) partidoMap.get("equipo_visitante"));
            partido.setEquipoVisitante(equipoVisitante);
        }

        // Extraer puntuación
        if (partidoMap.containsKey("puntos_local")) {
            Object puntosObj = partidoMap.get("puntos_local");
            if (puntosObj instanceof Integer) {
                partido.setPuntosLocal((Integer) puntosObj);
            }
        }

        if (partidoMap.containsKey("puntos_visitante")) {
            Object puntosObj = partidoMap.get("puntos_visitante");
            if (puntosObj instanceof Integer) {
                partido.setPuntosVisitante((Integer) puntosObj);
            }
        }

        // Extraer finalizado
        if (partidoMap.containsKey("finalizado")) {
            Object finalizadoObj = partidoMap.get("finalizado");
            if (finalizadoObj instanceof Boolean) {
                partido.setFinalizado((Boolean) finalizadoObj);
            }
        }

        return partido;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Transactional(readOnly = true)
    public List<Partido> findByEquipo(Equipo equipo) {
        // BUSCO PARTIDOS POR EQUIPO (desde el repositorio local por simplicidad)
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
        // BUSCO PARTIDOS POR FECHA (desde el repositorio local por simplicidad)
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
        // BUSCO PARTIDOS FINALIZADOS (desde el repositorio local por simplicidad)
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
        // BUSCO PARTIDOS PENDIENTES (desde el repositorio local por simplicidad)
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
    public void deleteById(Long id) {
        try {
            LOGGER.info("Eliminando partido con ID: " + id + " en la API Flask");

            // Hacer petición HTTP a la API Flask
            restTemplate.delete(apiUrl + "/partidos/" + id);
        } catch (Exception e) {
            // Si hay un error con la API, intentar con el repositorio local
            LOGGER.warning("Error al eliminar partido en la API Flask: " + e.getMessage());
            LOGGER.info("Intentando eliminar partido en el repositorio local");

            try {
                partidoRepository.deleteById(id);
            } catch (Exception ex) {
                LOGGER.severe("Error al eliminar partido en el repositorio local: " + ex.getMessage());
                throw new DatabaseException("Error al eliminar partido con ID " + id, ex);
            }
        }
    }
}