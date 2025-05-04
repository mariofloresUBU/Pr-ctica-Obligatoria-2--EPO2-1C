#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Aplicación principal de Flask para la API de baloncesto.

Este módulo configura la aplicación Flask, carga las rutas y
establece la conexión con la base de datos.

Author: Mario Flores
Version: 1.0
Date: 2025-05-04
"""

import os
from flask import Flask, jsonify
from flask_cors import CORS
from flask_sqlalchemy import SQLAlchemy
import logging

# INICIALIZO LA APLICACIÓN FLASK
app = Flask(__name__)

# CONFIGURO CORS PARA PERMITIR PETICIONES DESDE EL FRONTEND
CORS(app)

# CONFIGURO EL LOGGER
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.FileHandler("api.log"),
        logging.StreamHandler()
    ]
)
logger = logging.getLogger(__name__)

# CARGO LA CONFIGURACIÓN DESDE config.py
app.config.from_pyfile('config.py')

# INICIALIZO LA BASE DE DATOS
db = SQLAlchemy(app)

# IMPORTO LOS MODELOS Y RUTAS DESPUÉS DE CREAR db
# PARA EVITAR REFERENCIAS CIRCULARES
from models import *
from routes import *

@app.errorhandler(404)
def not_found(error):
    """
    Manejador de error 404 (Recurso no encontrado).

    Args:
        error: El error que ha ocurrido

    Returns:
        Una respuesta JSON con el mensaje de error y el código 404
    """
    logger.warning(f"Recurso no encontrado: {error}")
    return jsonify({'error': 'Recurso no encontrado'}), 404

@app.errorhandler(500)
def internal_server_error(error):
    """
    Manejador de error 500 (Error interno del servidor).

    Args:
        error: El error que ha ocurrido

    Returns:
        Una respuesta JSON con el mensaje de error y el código 500
    """
    logger.error(f"Error interno del servidor: {error}")
    return jsonify({'error': 'Error interno del servidor'}), 500

@app.route('/health', methods=['GET'])
def health_check():
    """
    Endpoint para comprobar si la API está funcionando correctamente.

    Returns:
        Una respuesta JSON indicando que la API está activa
    """
    logger.info("Verificación de salud de la API realizada")
    return jsonify({'status': 'API activa', 'message': 'La API de baloncesto está funcionando correctamente'})

# SI ESTE ARCHIVO SE EJECUTA DIRECTAMENTE
if __name__ == '__main__':
    # CREO TODAS LAS TABLAS EN LA BASE DE DATOS
    with app.app_context():
        db.create_all()
        logger.info("Base de datos inicializada")

    # INICIO EL SERVIDOR FLASK
    port = int(os.environ.get("PORT", 5000))
    logger.info(f"Iniciando servidor en el puerto {port}")
    app.run(host='0.0.0.0', port=port, debug=True)