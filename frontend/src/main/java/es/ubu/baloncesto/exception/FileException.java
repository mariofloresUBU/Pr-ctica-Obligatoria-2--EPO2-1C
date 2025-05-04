package es.ubu.baloncesto.exception;

/**
 * Excepción personalizada para errores relacionados con operaciones de archivos.
 * Esta excepción se lanza cuando ocurre un error al abrir, leer, escribir o cerrar archivos.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */
public class FileException extends RuntimeException {

    /**
     * Identificador para la serialización.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Ruta del archivo que causó la excepción.
     */
    private final String filePath;

    /**
     * Constructor con mensaje de error y ruta del archivo.
     *
     * @param message Mensaje descriptivo del error
     * @param filePath Ruta del archivo que causó el error
     */
    public FileException(String message, String filePath) {
        // LLAMO AL CONSTRUCTOR DE LA CLASE PADRE CON EL MENSAJE
        super(message);
        // GUARDO LA RUTA DEL ARCHIVO
        this.filePath = filePath;
    }

    /**
     * Constructor con mensaje de error, causa y ruta del archivo.
     *
     * @param message Mensaje descriptivo del error
     * @param cause Excepción original que causó este error
     * @param filePath Ruta del archivo que causó el error
     */
    public FileException(String message, Throwable cause, String filePath) {
        // LLAMO AL CONSTRUCTOR DE LA CLASE PADRE CON EL MENSAJE Y LA CAUSA
        super(message, cause);
        // GUARDO LA RUTA DEL ARCHIVO
        this.filePath = filePath;
    }

    /**
     * Obtiene la ruta del archivo que causó la excepción.
     *
     * @return Ruta del archivo
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Devuelve una representación en cadena de esta excepción.
     *
     * @return Cadena con el mensaje y la ruta del archivo
     */
    @Override
    public String toString() {
        // CREO UN MENSAJE PERSONALIZADO QUE INCLUYA LA RUTA DEL ARCHIVO
        return "FileException: " + getMessage() + " [Archivo: " + filePath + "]";
    }
}