#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Script para ejecutar la aplicación.

Author: Mario Flores
Version: 1.0
Date: 2025-05-04
"""

from app import app, db
from models import Equipo, Partido
import routes

if __name__ == '__main__':
    # Crear tablas
    with app.app_context():
        db.create_all()

    # Ejecutar la aplicación
    app.run(host='0.0.0.0', port=5000, debug=True)