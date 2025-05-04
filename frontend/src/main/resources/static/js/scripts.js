/**
 * Script principal para la aplicación de baloncesto.
 * Contiene funcionalidades comunes utilizadas en varias páginas.
 *
 * @author Mario Flores
 * @version 1.0
 * @since 2025-05-04
 */

document.addEventListener('DOMContentLoaded', function() {
    // INICIALIZO LA APLICACIÓN
    console.log('Aplicación de Baloncesto inicializada');

    // CONFIGURO LOS MENSAJES DE ALERTA PARA QUE SE OCULTEN DESPUÉS DE 5 SEGUNDOS
    setupAlertDismissal();

    // CONFIGURO LA VALIDACIÓN DE FORMULARIOS
    setupFormValidation();
});

/**
 * Configura los mensajes de alerta para que se oculten automáticamente.
 */
function setupAlertDismissal() {
    const alerts = document.querySelectorAll('.alert');

    alerts.forEach(function(alert) {
        // DESPUÉS DE 5 SEGUNDOS, OCULTO LA ALERTA CON UNA ANIMACIÓN
        setTimeout(function() {
            alert.style.opacity = '0';

            // CUANDO TERMINA LA ANIMACIÓN, ELIMINO LA ALERTA DEL DOM
            setTimeout(function() {
                alert.remove();
            }, 500);
        }, 5000);
    });
}

/**
 * Configura la validación básica de formularios.
 */
function setupFormValidation() {
    const forms = document.querySelectorAll('form');

    forms.forEach(function(form) {
        form.addEventListener('submit', function(event) {
            // VERIFICO SI EL FORMULARIO TIENE CAMPOS REQUERIDOS SIN COMPLETAR
            const requiredFields = form.querySelectorAll('[required]');
            let isValid = true;

            requiredFields.forEach(function(field) {
                if (!field.value.trim()) {
                    isValid = false;
                    // AÑADO UNA CLASE DE ERROR AL CAMPO
                    field.classList.add('error');

                    // CREO UN MENSAJE DE ERROR
                    const errorMessage = document.createElement('div');
                    errorMessage.className = 'field-error';
                    errorMessage.textContent = 'Este campo es obligatorio';

                    // AÑADO EL MENSAJE DESPUÉS DEL CAMPO
                    field.parentNode.appendChild(errorMessage);
                }
            });

            // SI HAY CAMPOS INVÁLIDOS, PREVENGO EL ENVÍO DEL FORMULARIO
            if (!isValid) {
                event.preventDefault();
            }
        });
    });
}

/**
 * Formatea una fecha para mostrarla en un formato legible.
 *
 * @param {Date} date - Objeto Date a formatear
 * @return {string} - Fecha formateada como DD/MM/AAAA HH:MM
 */
function formatDate(date) {
    if (!(date instanceof Date)) {
        date = new Date(date);
    }

    const day = String(date.getDate()).padStart(2, '0');
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const year = date.getFullYear();
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${day}/${month}/${year} ${hours}:${minutes}`;
}

/**
 * Muestra un mensaje de confirmación antes de realizar una acción destructiva.
 *
 * @param {string} message - Mensaje de confirmación
 * @return {boolean} - true si el usuario confirma, false en caso contrario
 */
function confirmAction(message) {
    return confirm(message || '¿Está seguro de realizar esta acción?');
}