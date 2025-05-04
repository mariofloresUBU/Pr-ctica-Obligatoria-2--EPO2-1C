package es.ubu.baloncesto.exception;

/**
 * Excepción personalizada para errores relacionados con llamadas a APIs de terceros.
 * Esta excepción se lanza cuando ocurre un error al comunicarse con APIs externas.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */
public class ApiException extends RuntimeException {

  /**
   * Identificador para la serialización.
   */
  private static final long serialVersionUID = 1L;

  /**
   * URL de la API que causó la excepción.
   */
  private final String apiUrl;

  /**
   * Código de estado HTTP recibido (si está disponible).
   */
  private final Integer statusCode;

  /**
   * Constructor con mensaje de error y URL de la API.
   *
   * @param message Mensaje descriptivo del error
   * @param apiUrl URL de la API que causó el error
   */
  public ApiException(String message, String apiUrl) {
    // LLAMO AL CONSTRUCTOR DE LA CLASE PADRE CON EL MENSAJE
    super(message);
    // GUARDO LA URL DE LA API
    this.apiUrl = apiUrl;
    // NO HAY CÓDIGO DE ESTADO DISPONIBLE
    this.statusCode = null;
  }

  /**
   * Constructor con mensaje de error, causa y URL de la API.
   *
   * @param message Mensaje descriptivo del error
   * @param cause Excepción original que causó este error
   * @param apiUrl URL de la API que causó el error
   */
  public ApiException(String message, Throwable cause, String apiUrl) {
    // LLAMO AL CONSTRUCTOR DE LA CLASE PADRE CON EL MENSAJE Y LA CAUSA
    super(message, cause);
    // GUARDO LA URL DE LA API
    this.apiUrl = apiUrl;
    // NO HAY CÓDIGO DE ESTADO DISPONIBLE
    this.statusCode = null;
  }

  /**
   * Constructor completo con mensaje, causa, URL y código de estado.
   *
   * @param message Mensaje descriptivo del error
   * @param cause Excepción original que causó este error
   * @param apiUrl URL de la API que causó el error
   * @param statusCode Código de estado HTTP recibido
   */
  public ApiException(String message, Throwable cause, String apiUrl, Integer statusCode) {
    // LLAMO AL CONSTRUCTOR DE LA CLASE PADRE CON EL MENSAJE Y LA CAUSA
    super(message, cause);
    // GUARDO LA URL DE LA API Y EL CÓDIGO DE ESTADO
    this.apiUrl = apiUrl;
    this.statusCode = statusCode;
  }

  /**
   * Obtiene la URL de la API que causó la excepción.
   *
   * @return URL de la API
   */
  public String getApiUrl() {
    return apiUrl;
  }

  /**
   * Obtiene el código de estado HTTP recibido.
   *
   * @return Código de estado o null si no está disponible
   */
  public Integer getStatusCode() {
    return statusCode;
  }

  /**
   * Devuelve una representación en cadena de esta excepción.
   *
   * @return Cadena con el mensaje, la URL y el código de estado si está disponible
   */
  @Override
  public String toString() {
    // CREO UN MENSAJE PERSONALIZADO QUE INCLUYA LA URL Y EL CÓDIGO DE ESTADO SI ESTÁ DISPONIBLE
    StringBuilder sb = new StringBuilder("ApiException: ");
    sb.append(getMessage());
    sb.append(" [API: ").append(apiUrl);

    if (statusCode != null) {
      sb.append(", Estado: ").append(statusCode);
    }

    sb.append("]");
    return sb.toString();
  }
}