#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Script para inicializar la base de datos con datos de equipos.
Este script crea varios equipos de ejemplo para poder usar la aplicación.

Author: Mario Flores
Version: 1.0
Date: 2025-05-04
"""

from __init__ import create_app, db, logger
from models import Equipo

def init_db():
    """
    Inicializa la base de datos con datos de equipos de ejemplo.
    """
    logger.info("Inicializando base de datos con equipos...")

    # Lista de equipos para añadir
    equipos = [
        {"nombre": "Real Madrid", "ciudad": "Madrid", "entrenador": "Pablo Laso"},
        {"nombre": "FC Barcelona", "ciudad": "Barcelona", "entrenador": "Sarunas Jasikevicius"},
        {"nombre": "Unicaja", "ciudad": "Málaga", "entrenador": "Ibon Navarro"},
        {"nombre": "Valencia Basket", "ciudad": "Valencia", "entrenador": "Pedro Martínez"},
        {"nombre": "Baskonia", "ciudad": "Vitoria", "entrenador": "Joan Peñarroya"},
        {"nombre": "Gran Canaria", "ciudad": "Las Palmas", "entrenador": "Jaka Lakovic"},
        {"nombre": "UCAM Murcia", "ciudad": "Murcia", "entrenador": "Sito Alonso"},
        {"nombre": "Joventut", "ciudad": "Badalona", "entrenador": "Carles Duran"}
    ]

    # Verificar si ya existen equipos
    existing_count = Equipo.query.count()
    if existing_count > 0:
        logger.info(f"Ya existen {existing_count} equipos en la base de datos.")
        return

    # Añade los equipos a la base de datos
    for equipo_data in equipos:
        equipo = Equipo(
            nombre=equipo_data["nombre"],
            ciudad=equipo_data["ciudad"],
            entrenador=equipo_data["entrenador"]
        )
        db.session.add(equipo)

    # Guarda los cambios
    db.session.commit()

    logger.info(f"Base de datos inicializada con {len(equipos)} equipos.")

if __name__ == "__main__":
    app = create_app()
    with app.app_context():
        init_db()