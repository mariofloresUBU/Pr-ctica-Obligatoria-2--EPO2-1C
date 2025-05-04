package es.ubu.baloncesto.exception;

/**
 * Excepción personalizada para errores relacionados con operaciones de base de datos.
 * Esta excepción se lanza cuando ocurre un error al conectar, consultar o modificar la base de datos.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */
public class DatabaseException extends RuntimeException {

    /**
     * Identificador para la serialización.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Código SQL de la operación que causó el error (si está disponible).
     */
    private final String sqlCode;

    /**
     * Constructor con mensaje de error.
     *
     * @param message Mensaje descriptivo del error
     */
    public DatabaseException(String message) {
        // LLAMO AL CONSTRUCTOR DE LA CLASE PADRE CON EL MENSAJE
        super(message);
        // NO HAY CÓDIGO SQL DISPONIBLE
        this.sqlCode = null;
    }

    /**
     * Constructor con mensaje de error y causa.
     *
     * @param message Mensaje descriptivo del error
     * @param cause Excepción original que causó este error
     */
    public DatabaseException(String message, Throwable cause) {
        // LLAMO AL CONSTRUCTOR DE LA CLASE PADRE CON EL MENSAJE Y LA CAUSA
        super(message, cause);
        // NO HAY CÓDIGO SQL DISPONIBLE
        this.sqlCode = null;
    }

    /**
     * Constructor con mensaje de error, causa y código SQL.
     *
     * @param message Mensaje descriptivo del error
     * @param cause Excepción original que causó este error
     * @param sqlCode Código SQL que causó el error
     */
    public DatabaseException(String message, Throwable cause, String sqlCode) {
        // LLAMO AL CONSTRUCTOR DE LA CLASE PADRE CON EL MENSAJE Y LA CAUSA
        super(message, cause);
        // GUARDO EL CÓDIGO SQL
        this.sqlCode = sqlCode;
    }

    /**
     * Obtiene el código SQL que causó la excepción.
     *
     * @return Código SQL o null si no está disponible
     */
    public String getSqlCode() {
        return sqlCode;
    }

    /**
     * Devuelve una representación en cadena de esta excepción.
     *
     * @return Cadena con el mensaje y el código SQL si está disponible
     */
    @Override
    public String toString() {
        // CREO UN MENSAJE PERSONALIZADO QUE INCLUYA EL CÓDIGO SQL SI ESTÁ DISPONIBLE
        if (sqlCode != null) {
            return "DatabaseException: " + getMessage() + " [SQL: " + sqlCode + "]";
        } else {
            return "DatabaseException: " + getMessage();
        }
    }
}