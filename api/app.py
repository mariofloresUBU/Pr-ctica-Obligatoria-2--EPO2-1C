#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Aplicación principal de Flask para la API de baloncesto.

Author: Mario Flores
Version: 1.0
Date: 2025-05-04
"""

import os
from flask import Flask
from flask_cors import CORS
from flask_sqlalchemy import SQLAlchemy
import logging

# Configuración del logger
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# Inicializar la aplicación
app = Flask(__name__)
CORS(app)

# Configuración de la base de datos
app.config['SQLALCHEMY_DATABASE_URI'] = 'sqlite:///baloncesto.db'
app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

# Inicializar la base de datos
db = SQLAlchemy(app)

# Importar rutas después de crear la aplicación y la DB
# Esto evita las referencias circulares
if __name__ == '__main__':
    # Las importaciones deben estar dentro de la función main
    from models import Equipo, Partido
    from routes import *

    # Crear tablas de la base de datos
    with app.app_context():
        db.create_all()
        logger.info("Base de datos inicializada")

    # Iniciar el servidor
    port = int(os.environ.get("PORT", 5000))
    logger.info(f"Iniciando servidor en el puerto {port}")
    app.run(host='0.0.0.0', port=port, debug=True)