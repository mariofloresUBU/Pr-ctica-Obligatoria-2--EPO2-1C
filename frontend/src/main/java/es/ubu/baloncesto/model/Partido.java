package es.ubu.baloncesto.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import java.time.LocalDateTime;

/**
 * Clase que representa un partido de baloncesto en el sistema.
 * Esta entidad se almacenará en la base de datos y contendrá
 * toda la información relevante sobre un partido.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */
@Entity
@Table(name = "partidos")
public class Partido {

    /**
     * Identificador único del partido en la base de datos.
     * Se genera automáticamente al crear un nuevo registro.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Equipo que juega como local en este partido.
     * Relación muchos a uno con la entidad Equipo.
     */
    @ManyToOne
    @JoinColumn(name = "equipo_local_id")
    private Equipo equipoLocal;

    /**
     * Equipo que juega como visitante en este partido.
     * Relación muchos a uno con la entidad Equipo.
     */
    @ManyToOne
    @JoinColumn(name = "equipo_visitante_id")
    private Equipo equipoVisitante;

    /**
     * Puntos anotados por el equipo local.
     * Inicialmente es 0 hasta que el partido finaliza.
     */
    private int puntosLocal;

    /**
     * Puntos anotados por el equipo visitante.
     * Inicialmente es 0 hasta que el partido finaliza.
     */
    private int puntosVisitante;

    /**
     * Fecha y hora programada para el partido.
     */
    private LocalDateTime fecha;

    /**
     * Indica si el partido ya ha finalizado.
     * true si ya se ha jugado, false si está pendiente.
     */
    private boolean finalizado;

    /**
     * Constructor por defecto requerido por JPA.
     * No inicializa ningún campo.
     */
    public Partido() {
        // CONSTRUCTOR VACÍO NECESARIO PARA JPA
    }

    /**
     * Constructor con parámetros básicos para crear un partido.
     *
     * @param equipoLocal Equipo que juega como local
     * @param equipoVisitante Equipo que juega como visitante
     * @param fecha Fecha y hora programada para el partido
     */
    public Partido(Equipo equipoLocal, Equipo equipoVisitante, LocalDateTime fecha) {
        // VERIFICO QUE LOS EQUIPOS NO SEAN NULOS
        if (equipoLocal == null || equipoVisitante == null) {
            throw new IllegalArgumentException("Los equipos no pueden ser nulos");
        }

        // VERIFICO QUE LOS EQUIPOS NO SEAN EL MISMO
        if (equipoLocal.equals(equipoVisitante)) {
            throw new IllegalArgumentException("El equipo local y visitante no pueden ser el mismo");
        }

        // INICIALIZO LOS CAMPOS CON LOS VALORES PROPORCIONADOS
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.fecha = fecha;

        // INICIALIZO LOS PUNTOS A CERO Y EL ESTADO COMO NO FINALIZADO
        this.puntosLocal = 0;
        this.puntosVisitante = 0;
        this.finalizado = false;
    }

    // MÉTODOS GETTER Y SETTER

    /**
     * Obtiene el identificador único del partido.
     *
     * @return El ID del partido
     */
    public Long getId() {
        return id;
    }

    /**
     * Establece el identificador único del partido.
     * Normalmente no se usa directamente ya que es generado por la base de datos.
     *
     * @param id El nuevo ID del partido
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtiene el equipo que juega como local.
     *
     * @return El equipo local
     */
    public Equipo getEquipoLocal() {
        return equipoLocal;
    }

    /**
     * Establece el equipo que juega como local.
     *
     * @param equipoLocal El nuevo equipo local
     */
    public void setEquipoLocal(Equipo equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    /**
     * Obtiene el equipo que juega como visitante.
     *
     * @return El equipo visitante
     */
    public Equipo getEquipoVisitante() {
        return equipoVisitante;
    }

    /**
     * Establece el equipo que juega como visitante.
     *
     * @param equipoVisitante El nuevo equipo visitante
     */
    public void setEquipoVisitante(Equipo equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    /**
     * Obtiene los puntos anotados por el equipo local.
     *
     * @return Los puntos del equipo local
     */
    public int getPuntosLocal() {
        return puntosLocal;
    }

    /**
     * Establece los puntos anotados por el equipo local.
     *
     * @param puntosLocal Los nuevos puntos del equipo local
     */
    public void setPuntosLocal(int puntosLocal) {
        this.puntosLocal = puntosLocal;
    }

    /**
     * Obtiene los puntos anotados por el equipo visitante.
     *
     * @return Los puntos del equipo visitante
     */
    public int getPuntosVisitante() {
        return puntosVisitante;
    }

    /**
     * Establece los puntos anotados por el equipo visitante.
     *
     * @param puntosVisitante Los nuevos puntos del equipo visitante
     */
    public void setPuntosVisitante(int puntosVisitante) {
        this.puntosVisitante = puntosVisitante;
    }

    /**
     * Obtiene la fecha y hora programada para el partido.
     *
     * @return La fecha y hora del partido
     */
    public LocalDateTime getFecha() {
        return fecha;
    }

    /**
     * Establece la fecha y hora programada para el partido.
     *
     * @param fecha La nueva fecha y hora del partido
     */
    public void setFecha(LocalDateTime fecha) {
        this.fecha = fecha;
    }

    /**
     * Indica si el partido ya ha finalizado.
     *
     * @return true si el partido ha finalizado, false en caso contrario
     */
    public boolean isFinalizado() {
        return finalizado;
    }

    /**
     * Establece si el partido ha finalizado.
     *
     * @param finalizado El nuevo estado de finalización del partido
     */
    public void setFinalizado(boolean finalizado) {
        this.finalizado = finalizado;
    }

    /**
     * Registra el resultado final del partido y actualiza las estadísticas
     * de los equipos (victorias y derrotas).
     *
     * @param puntosLocal Puntos anotados por el equipo local
     * @param puntosVisitante Puntos anotados por el equipo visitante
     */
    public void registrarResultado(int puntosLocal, int puntosVisitante) {
        // VERIFICO QUE LOS PUNTOS SEAN POSITIVOS
        if (puntosLocal < 0 || puntosVisitante < 0) {
            throw new IllegalArgumentException("Los puntos no pueden ser negativos");
        }

        // ESTABLEZCO LOS PUNTOS
        this.puntosLocal = puntosLocal;
        this.puntosVisitante = puntosVisitante;

        // MARCO EL PARTIDO COMO FINALIZADO
        this.finalizado = true;

        // ACTUALIZO LAS ESTADÍSTICAS DE LOS EQUIPOS
        if (puntosLocal > puntosVisitante) {
            // GANA EL EQUIPO LOCAL
            equipoLocal.registrarVictoria();
            equipoVisitante.registrarDerrota();
        } else if (puntosVisitante > puntosLocal) {
            // GANA EL EQUIPO VISITANTE
            equipoVisitante.registrarVictoria();
            equipoLocal.registrarDerrota();
        }
        // NO CONTEMPLO EMPATES EN BALONCESTO
    }

    /**
     * Verifica si el partido ya se puede jugar (la fecha programada ya ha pasado).
     *
     * @return true si la fecha del partido ya ha pasado, false en caso contrario
     */
    public boolean estaListo() {
        // COMPRUEBO SI LA FECHA DEL PARTIDO YA HA PASADO
        return LocalDateTime.now().isAfter(fecha);
    }

    /**
     * Obtiene el equipo ganador del partido.
     *
     * @return El equipo ganador, o null si el partido no ha finalizado o hay empate
     */
    public Equipo getGanador() {
        // SI EL PARTIDO NO HA FINALIZADO, NO HAY GANADOR
        if (!finalizado) {
            return null;
        }

        // DETERMINO EL GANADOR SEGÚN LOS PUNTOS
        if (puntosLocal > puntosVisitante) {
            return equipoLocal;
        } else if (puntosVisitante > puntosLocal) {
            return equipoVisitante;
        } else {
            // EN CASO DE EMPATE (IMPROBABLE EN BALONCESTO)
            return null;
        }
    }

    /**
     * Obtiene la diferencia de puntos entre los equipos.
     *
     * @return La diferencia de puntos en valor absoluto
     */
    public int getDiferenciaPuntos() {
        // CALCULO EL VALOR ABSOLUTO DE LA DIFERENCIA
        return Math.abs(puntosLocal - puntosVisitante);
    }

    /**
     * Devuelve una representación en cadena de este partido.
     *
     * @return Cadena con los nombres de los equipos y el resultado si está finalizado
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(equipoLocal.getNombre())
                .append(" vs ")
                .append(equipoVisitante.getNombre());

        if (finalizado) {
            sb.append(" (")
                    .append(puntosLocal)
                    .append(" - ")
                    .append(puntosVisitante)
                    .append(")");
        }

        return sb.toString();
    }
}