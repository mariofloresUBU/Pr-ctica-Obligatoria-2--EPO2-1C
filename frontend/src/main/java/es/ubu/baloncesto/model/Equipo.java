package es.ubu.baloncesto.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un equipo de baloncesto en el sistema.
 * Esta entidad se almacenará en la base de datos y contendrá
 * toda la información relevante sobre un equipo.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */
@Entity
@Table(name = "equipos")
public class Equipo {

    /**
     * Identificador único del equipo en la base de datos.
     * Se genera automáticamente al crear un nuevo registro.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Nombre oficial del equipo de baloncesto.
     * Por ejemplo: "Lakers", "Celtics", etc.
     */
    private String nombre;

    /**
     * Ciudad a la que pertenece el equipo.
     * Por ejemplo: "Los Angeles", "Boston", etc.
     */
    private String ciudad;

    /**
     * Nombre del entrenador principal del equipo.
     */
    private String entrenador;

    /**
     * Número de victorias conseguidas por el equipo
     * en la temporada actual.
     */
    private int victorias;

    /**
     * Número de derrotas sufridas por el equipo
     * en la temporada actual.
     */
    private int derrotas;

    /**
     * Lista de partidos en los que este equipo participa como local.
     * Esta relación es bidireccional con la entidad Partido.
     */
    @OneToMany(mappedBy = "equipoLocal")
    private List<Partido> partidosLocal = new ArrayList<>();

    /**
     * Lista de partidos en los que este equipo participa como visitante.
     * Esta relación es bidireccional con la entidad Partido.
     */
    @OneToMany(mappedBy = "equipoVisitante")
    private List<Partido> partidosVisitante = new ArrayList<>();

    /**
     * Constructor por defecto requerido por JPA.
     * No inicializa ningún campo.
     */
    public Equipo() {
        // CONSTRUCTOR VACÍO NECESARIO PARA JPA
    }

    /**
     * Constructor con parámetros básicos para crear un equipo.
     *
     * @param nombre Nombre del equipo
     * @param ciudad Ciudad a la que pertenece
     * @param entrenador Nombre del entrenador
     */
    public Equipo(String nombre, String ciudad, String entrenador) {
        // INICIALIZO LOS CAMPOS CON LOS VALORES PROPORCIONADOS
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.entrenador = entrenador;
        // INICIO LAS VICTORIAS Y DERROTAS A CERO
        this.victorias = 0;
        this.derrotas = 0;
    }

    // MÉTODOS GETTER Y SETTER

    /**
     * Obtiene el identificador único del equipo.
     *
     * @return El ID del equipo
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del equipo.
     * Normalmente no se usa directamente ya que es generado por la base de datos.
     *
     * @param id El nuevo ID del equipo
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del equipo.
     *
     * @return El nombre del equipo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del equipo.
     *
     * @param nombre El nuevo nombre del equipo
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la ciudad a la que pertenece el equipo.
     *
     * @return La ciudad del equipo
     */
    public String getCiudad() {
        return ciudad;
    }

    /**
     * Establece la ciudad a la que pertenece el equipo.
     *
     * @param ciudad La nueva ciudad del equipo
     */
    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    /**
     * Obtiene el nombre del entrenador del equipo.
     *
     * @return El nombre del entrenador
     */
    public String getEntrenador() {
        return entrenador;
    }

    /**
     * Establece el nombre del entrenador del equipo.
     *
     * @param entrenador El nuevo nombre del entrenador
     */
    public void setEntrenador(String entrenador) {
        this.entrenador = entrenador;
    }

    /**
     * Obtiene el número de victorias del equipo.
     *
     * @return El número de victorias
     */
    public int getVictorias() {
        return victorias;
    }

    /**
     * Establece el número de victorias del equipo.
     *
     * @param victorias El nuevo número de victorias
     */
    public void setVictorias(int victorias) {
        this.victorias = victorias;
    }

    /**
     * Obtiene el número de derrotas del equipo.
     *
     * @return El número de derrotas
     */
    public int getDerrotas() {
        return derrotas;
    }

    /**
     * Establece el número de derrotas del equipo.
     *
     * @param derrotas El nuevo número de derrotas
     */
    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }

    /**
     * Obtiene la lista de partidos donde este equipo juega como local.
     *
     * @return Lista de partidos como local
     */
    public List<Partido> getPartidosLocal() {
        return partidosLocal;
    }

    /**
     * Establece la lista de partidos donde este equipo juega como local.
     *
     * @param partidosLocal Nueva lista de partidos como local
     */
    public void setPartidosLocal(List<Partido> partidosLocal) {
        this.partidosLocal = partidosLocal;
    }

    /**
     * Obtiene la lista de partidos donde este equipo juega como visitante.
     *
     * @return Lista de partidos como visitante
     */
    public List<Partido> getPartidosVisitante() {
        return partidosVisitante;
    }

    /**
     * Establece la lista de partidos donde este equipo juega como visitante.
     *
     * @param partidosVisitante Nueva lista de partidos como visitante
     */
    public void setPartidosVisitante(List<Partido> partidosVisitante) {
        this.partidosVisitante = partidosVisitante;
    }

    /**
     * Registra una victoria para este equipo.
     * Incrementa el contador de victorias en uno.
     */
    public void registrarVictoria() {
        // INCREMENTO EL CONTADOR DE VICTORIAS
        this.victorias++;
    }

    /**
     * Registra una derrota para este equipo.
     * Incrementa el contador de derrotas en uno.
     */
    public void registrarDerrota() {
        // INCREMENTO EL CONTADOR DE DERROTAS
        this.derrotas++;
    }

    /**
     * Calcula el porcentaje de victorias del equipo.
     *
     * @return Porcentaje de victorias como un valor entre 0 y 1
     */
    public double calcularPorcentajeVictorias() {
        // SI NO HAY PARTIDOS JUGADOS, DEVUELVO 0
        int totalPartidos = victorias + derrotas;
        if (totalPartidos == 0) {
            return 0.0;
        }
        // CALCULO EL PORCENTAJE DE VICTORIAS
        return (double) victorias / totalPartidos;
    }

    /**
     * Devuelve una representación en cadena de este equipo.
     *
     * @return Cadena con el nombre y la ciudad del equipo
     */
    @Override
    public String toString() {
        // DEVUELVO UNA CADENA CON LA INFORMACIÓN BÁSICA DEL EQUIPO
        return nombre + " (" + ciudad + ")";
    }
}