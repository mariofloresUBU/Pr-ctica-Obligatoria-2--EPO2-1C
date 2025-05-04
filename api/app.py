#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Aplicación principal de baloncesto.

Este módulo define la aplicación Flask principal y su configuración.

Author: Mario Flores
Version: 1.0
Date: 2025-05-04
"""

from __init__ import create_app, db, logger
from models import Equipo, Partido

# Crear la aplicación
app = create_app()

# Función para crear la base de datos
def create_db():
    """
    Crea las tablas en la base de datos.
    """
    logger.info("Creando tablas en la base de datos...")
    db.create_all()
    logger.info("Tablas creadas correctamente.")

# Ejecutar la aplicación
if __name__ == '__main__':
    # Crear la base de datos si no existe
    with app.app_context():
        create_db()

    # Iniciar el servidor
    app.run(debug=True)