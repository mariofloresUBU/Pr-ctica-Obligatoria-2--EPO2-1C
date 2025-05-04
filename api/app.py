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

# Importar models para evitar referencias circulares
from models import Equipo, Partido

# Importar rutas después de crear la aplicación y la DB
from routes import *

# Bloque principal
if __name__ == '__main__':
    # Crear tablas de la base de datos
    with app.app_context():
        db.create_all()

        # Verificar si ya existen equipos
        if Equipo.query.count() == 0:
            # Crear equipos de la Liga Endesa 2024-2025
            equipos = [
                Equipo(nombre="Real Madrid", ciudad="Madrid", entrenador="Chus Mateo"),
                Equipo(nombre="FC Barcelona", ciudad="Barcelona", entrenador="Joan Peñarroya"),
                Equipo(nombre="Unicaja", ciudad="Málaga", entrenador="Ibon Navarro"),
                Equipo(nombre="Baskonia", ciudad="Vitoria", entrenador="Pablo Laso"),
                Equipo(nombre="Valencia Basket", ciudad="Valencia", entrenador="Pedro Martínez"),
                Equipo(nombre="UCAM Murcia", ciudad="Murcia", entrenador="Sito Alonso"),
                Equipo(nombre="Gran Canaria", ciudad="Las Palmas", entrenador="Jaka Lakovic"),
                Equipo(nombre="Joventut Badalona", ciudad="Badalona", entrenador="Carles Durán"),
                Equipo(nombre="Zaragoza", ciudad="Zaragoza", entrenador="Porfirio Fisac"),
                Equipo(nombre="Breogán", ciudad="Lugo", entrenador="Luis Casimiro"),
                Equipo(nombre="Bàsquet Girona", ciudad="Girona", entrenador="Fotis Katsikaris"),
                Equipo(nombre="MoraBanc Andorra", ciudad="Andorra", entrenador="Natxo Lezkano"),
                Equipo(nombre="La Laguna Tenerife", ciudad="Tenerife", entrenador="Txus Vidorreta"),
                Equipo(nombre="Coviran Granada", ciudad="Granada", entrenador="Pablo Pin"),
                Equipo(nombre="ICL Manresa", ciudad="Manresa", entrenador="Diego Ocampo"),
                Equipo(nombre="Leyma Coruña", ciudad="A Coruña", entrenador="Diego Epifanio"),
                Equipo(nombre="Força Lleida", ciudad="Lleida", entrenador="Gerard Encuentra"),
                Equipo(nombre="Bilbao Basket", ciudad="Bilbao", entrenador="Jaume Ponsarnau")
            ]

            # Añadir todos los equipos a la base de datos
            for equipo in equipos:
                db.session.add(equipo)

            # Guardar cambios
            db.session.commit()
            logger.info("Base de datos inicializada con los 18 equipos de la Liga Endesa 2024-2025")

        logger.info("Base de datos inicializada")

    # Iniciar el servidor
    port = int(os.environ.get("PORT", 5000))
    logger.info(f"Iniciando servidor en el puerto {port}")
    app.run(host='0.0.0.0', port=port, debug=True)