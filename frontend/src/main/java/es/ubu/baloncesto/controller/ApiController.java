package es.ubu.baloncesto.controller;

import es.ubu.baloncesto.exception.ApiException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * Controlador que simula invocaciones a APIs de terceros.
 * Proporciona una interfaz para probar llamadas a la API de Pokémon.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */
@Controller
@RequestMapping("/api-test")
public class ApiController {

    /**
     * URL base de la API de Pokémon.
     */
    @Value("${pokemon.api.url:https://pokeapi.co/api/v2/}")
    private String pokemonApiUrl;

    /**
     * Cliente REST para hacer peticiones HTTP.
     */
    private final RestTemplate restTemplate;

    /**
     * Constructor que inicializa el cliente REST.
     */
    public ApiController() {
        // INICIALIZO EL CLIENTE REST
        this.restTemplate = new RestTemplate();
    }

    /**
     * Muestra la página para probar llamadas a la API de Pokémon.
     *
     * @param model Modelo para pasar datos a la vista
     * @return Nombre de la vista a mostrar (api-test.html)
     */
    @GetMapping
    public String showApiTestPage(Model model) {
        // AÑADO LA URL DE LA API DE POKÉMON AL MODELO
        model.addAttribute("apiUrl", pokemonApiUrl);

        // DEVUELVO LA VISTA DE PRUEBA DE API
        return "api-test";
    }

    /**
     * Obtiene información de un Pokémon por su nombre o ID.
     *
     * @param pokemonId Nombre o ID del Pokémon
     * @return Respuesta con la información del Pokémon
     */
    @GetMapping("/pokemon/{pokemonId}")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> getPokemon(@PathVariable String pokemonId) {
        try {
            // CONSTRUYO LA URL DE LA API
            String url = pokemonApiUrl + "pokemon/" + pokemonId.toLowerCase();

            // HAGO LA PETICIÓN HTTP
            ResponseEntity<Map> response = restTemplate.getForEntity(url, Map.class);

            // EXTRAIGO LOS DATOS RELEVANTES
            Map<String, Object> pokemonData = new HashMap<>();
            Map<String, Object> responseBody = response.getBody();

            if (responseBody != null) {
                pokemonData.put("id", responseBody.get("id"));
                pokemonData.put("name", responseBody.get("name"));
                pokemonData.put("height", responseBody.get("height"));
                pokemonData.put("weight", responseBody.get("weight"));

                // EXTRAIGO LOS TIPOS
                if (responseBody.containsKey("types")) {
                    Object[] types = (Object[]) responseBody.get("types");
                    String[] typeNames = new String[types.length];

                    for (int i = 0; i < types.length; i++) {
                        Map<String, Object> typeData = (Map<String, Object>) types[i];
                        Map<String, Object> type = (Map<String, Object>) typeData.get("type");
                        typeNames[i] = (String) type.get("name");
                    }

                    pokemonData.put("types", typeNames);
                }

                // EXTRAIGO LA URL DE LA IMAGEN
                if (responseBody.containsKey("sprites")) {
                    Map<String, Object> sprites = (Map<String, Object>) responseBody.get("sprites");
                    pokemonData.put("image", sprites.get("front_default"));
                }
            }

            // DEVUELVO LOS DATOS DEL POKÉMON
            return ResponseEntity.ok(pokemonData);
        } catch (RestClientException e) {
            // SI HAY UN ERROR, LANZO UNA EXCEPCIÓN PERSONALIZADA
            throw new ApiException("Error al obtener datos del Pokémon: " + e.getMessage(),
                    e, pokemonApiUrl + "pokemon/" + pokemonId);
        }
    }

    /**
     * Simula un error en la API para probar el manejo de excepciones.
     *
     * @param errorCode Código de error HTTP (opcional)
     * @return Nunca devuelve una respuesta, siempre lanza una excepción
     */
    @PostMapping("/simulate-error")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> simulateError(@RequestParam(required = false) Integer errorCode) {
        // DETERMINO EL CÓDIGO DE ERROR
        int statusCode = (errorCode != null) ? errorCode : 500;

        // LANZO UNA EXCEPCIÓN PERSONALIZADA
        throw new ApiException("Error simulado para pruebas",
                null, "https://api.example.com/simulated-error", statusCode);
    }
}