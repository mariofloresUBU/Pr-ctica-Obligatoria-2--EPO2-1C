#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Inicialización de la aplicación Flask.

Este módulo inicializa la aplicación Flask y sus componentes.

Author: Mario Flores
Version: 1.0
Date: 2025-05-04
"""

from flask import Flask
from flask_sqlalchemy import SQLAlchemy
from flask_cors import CORS
import logging
import os

# Configurar logging
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s'
)
logger = logging.getLogger(__name__)

# Crear la instancia de db
db = SQLAlchemy()

def create_app():
    """
    Crea y configura la aplicación Flask.
    """
    # Crear la aplicación Flask
    app = Flask(__name__)

    # Configurar CORS
    CORS(app)

    # Configurar la base de datos
    app.config['SQLALCHEMY_DATABASE_URI'] = os.environ.get('DATABASE_URL', 'sqlite:///baloncesto.db')
    app.config['SQLALCHEMY_TRACK_MODIFICATIONS'] = False

    # Inicializar la base de datos con la aplicación
    db.init_app(app)

    # Importar y registrar las rutas de la API
    from routes import api
    app.register_blueprint(api)

    return app