#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Configuración de la aplicación Flask para la API de baloncesto.

Este módulo define variables de configuración como la conexión a la
base de datos, claves secretas, y otros ajustes de la aplicación.

Author: Mario Flores
Version: 1.0
Date: 2025-05-04
"""

import os
import tempfile

# OBTENGO EL DIRECTORIO DONDE SE ENCUENTRA ESTE ARCHIVO
basedir = os.path.abspath(os.path.dirname(__file__))

# CONFIGURO EL MODO DEBUG
# EN PRODUCCIÓN ESTO DEBERÍA SER FALSE
DEBUG = True

# CLAVE SECRETA PARA SESIONES Y TOKENS
# ESTO DEBE SER UNA CADENA ALEATORIA LARGA EN PRODUCCIÓN
SECRET_KEY = 'mi_clave_secreta_para_desarrollo'

# CONFIGURACIÓN DE LA BASE DE DATOS SQLITE
# EN DESARROLLO USO UN ARCHIVO TEMPORAL
# EN PRODUCCIÓN ESTO DEBERÍA SER UNA BASE DE DATOS REAL
DB_FILE = os.path.join(basedir, 'baloncesto.db')
SQLALCHEMY_DATABASE_URI = f'sqlite:///{DB_FILE}'

# CONFIGURO OTRAS OPCIONES DE SQLALCHEMY
SQLALCHEMY_TRACK_MODIFICATIONS = False
SQLALCHEMY_ECHO = True  # MUESTRA LAS CONSULTAS SQL EN LA CONSOLA

# TAMAÑO MÁXIMO DE LAS PETICIONES
MAX_CONTENT_LENGTH = 16 * 1024 * 1024  # 16 MB

# TIEMPO DE CADUCIDAD DE LOS TOKENS JWT (24 HORAS)
JWT_EXPIRATION_DELTA = 24 * 60 * 60  # EN SEGUNDOS

# URL DE LA API DE POKÉMON (PARA SIMULAR LLAMADAS A APIS DE TERCEROS)
POKEMON_API_URL = 'https://pokeapi.co/api/v2/'

# CONFIGURACIÓN DE CORS
CORS_HEADERS = 'Content-Type'