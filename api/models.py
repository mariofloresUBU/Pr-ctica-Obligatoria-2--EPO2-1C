#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Modelos de datos para la API de baloncesto.

Este módulo define las clases de modelo que representan las tablas
en la base de datos SQLite para la API de baloncesto.

Author: Mario Flores
Version: 1.0
Date: 2025-05-04
"""

from app import db
from datetime import datetime
import logging

# CONFIGURO EL LOGGER
logger = logging.getLogger(__name__)

class Equipo(db.Model):
    """
    Modelo que representa un equipo de baloncesto en la base de datos.

    Attributes:
        id (int): Identificador único del equipo
        nombre (str): Nombre del equipo
        ciudad (str): Ciudad a la que pertenece el equipo
        entrenador (str): Nombre del entrenador del equipo
        victorias (int): Número de victorias conseguidas
        derrotas (int): Número de derrotas sufridas
        fecha_creacion (datetime): Fecha y hora de creación del registro
    """

    __tablename__ = 'equipos'

    id = db.Column(db.Integer, primary_key=True)
    nombre = db.Column(db.String(100), nullable=False, unique=True)
    ciudad = db.Column(db.String(100), nullable=False)
    entrenador = db.Column(db.String(100), nullable=False)
    victorias = db.Column(db.Integer, default=0)
    derrotas = db.Column(db.Integer, default=0)
    fecha_creacion = db.Column(db.DateTime, default=datetime.utcnow)

    # RELACIÓN CON LOS PARTIDOS
    partidos_local = db.relationship('Partido', backref='equipo_local',
                                     foreign_keys='Partido.equipo_local_id',
                                     lazy='dynamic')
    partidos_visitante = db.relationship('Partido', backref='equipo_visitante',
                                         foreign_keys='Partido.equipo_visitante_id',
                                         lazy='dynamic')

    def __init__(self, nombre, ciudad, entrenador):
        """
        Inicializa un nuevo equipo con los datos proporcionados.

        Args:
            nombre (str): Nombre del equipo
            ciudad (str): Ciudad a la que pertenece
            entrenador (str): Nombre del entrenador
        """
        # ASIGNO LOS VALORES INICIALES
        self.nombre = nombre
        self.ciudad = ciudad
        self.entrenador = entrenador
        self.victorias = 0
        self.derrotas = 0
        logger.info(f"Creado nuevo equipo: {nombre} ({ciudad})")

    def registrar_victoria(self):
        """
        Incrementa el contador de victorias del equipo.

        Returns:
            int: El nuevo número de victorias
        """
        self.victorias += 1
        logger.info(f"Victoria registrada para {self.nombre}. Total: {self.victorias}")
        return self.victorias

    def registrar_derrota(self):
        """
        Incrementa el contador de derrotas del equipo.

        Returns:
            int: El nuevo número de derrotas
        """
        self.derrotas += 1
        logger.info(f"Derrota registrada para {self.nombre}. Total: {self.derrotas}")
        return self.derrotas

    def calcular_porcentaje_victorias(self):
        """
        Calcula el porcentaje de victorias del equipo.

        Returns:
            float: Porcentaje de victorias (0-1)
        """
        total_partidos = self.victorias + self.derrotas
        if total_partidos == 0:
            return 0.0
        return self.victorias / total_partidos

    def to_dict(self):
        """
        Convierte el equipo a un diccionario para la API.

        Returns:
            dict: Representación en diccionario del equipo
        """
        return {
            'id': self.id,
            'nombre': self.nombre,
            'ciudad': self.ciudad,
            'entrenador': self.entrenador,
            'victorias': self.victorias,
            'derrotas': self.derrotas,
            'porcentaje_victorias': self.calcular_porcentaje_victorias(),
            'fecha_creacion': self.fecha_creacion.isoformat() if self.fecha_creacion else None
        }

    def __repr__(self):
        """
        Devuelve una representación en string del equipo.

        Returns:
            str: Representación en string
        """
        return f"<Equipo {self.nombre} ({self.ciudad})>"


class Partido(db.Model):
    """
    Modelo que representa un partido de baloncesto en la base de datos.

    Attributes:
        id (int): Identificador único del partido
        equipo_local_id (int): ID del equipo local
        equipo_visitante_id (int): ID del equipo visitante
        puntos_local (int): Puntos anotados por el equipo local
        puntos_visitante (int): Puntos anotados por el equipo visitante
        fecha (datetime): Fecha y hora del partido
        finalizado (bool): Indica si el partido ha finalizado
        fecha_creacion (datetime): Fecha y hora de creación del registro
    """

    __tablename__ = 'partidos'

    id = db.Column(db.Integer, primary_key=True)
    equipo_local_id = db.Column(db.Integer, db.ForeignKey('equipos.id'), nullable=False)
    equipo_visitante_id = db.Column(db.Integer, db.ForeignKey('equipos.id'), nullable=False)
    puntos_local = db.Column(db.Integer, default=0)
    puntos_visitante = db.Column(db.Integer, default=0)
    fecha = db.Column(db.DateTime, nullable=False)
    finalizado = db.Column(db.Boolean, default=False)
    fecha_creacion = db.Column(db.DateTime, default=datetime.utcnow)

    def __init__(self, equipo_local_id, equipo_visitante_id, fecha):
        """
        Inicializa un nuevo partido con los datos proporcionados.

        Args:
            equipo_local_id (int): ID del equipo local
            equipo_visitante_id (int): ID del equipo visitante
            fecha (datetime): Fecha y hora del partido
        """
        # VERIFICO QUE LOS EQUIPOS NO SEAN EL MISMO
        if equipo_local_id == equipo_visitante_id:
            error_msg = "El equipo local y visitante no pueden ser el mismo"
            logger.error(error_msg)
            raise ValueError(error_msg)

        # ASIGNO LOS VALORES INICIALES
        self.equipo_local_id = equipo_local_id
        self.equipo_visitante_id = equipo_visitante_id
        self.fecha = fecha
        self.puntos_local = 0
        self.puntos_visitante = 0
        self.finalizado = False
        logger.info(f"Creado nuevo partido entre equipos {equipo_local_id} y {equipo_visitante_id}")

    def registrar_resultado(self, puntos_local, puntos_visitante):
        """
        Registra el resultado final del partido.

        Args:
            puntos_local (int): Puntos anotados por el equipo local
            puntos_visitante (int): Puntos anotados por el equipo visitante

        Returns:
            bool: True si se registró correctamente
        """
        # VERIFICO QUE LOS PUNTOS SEAN POSITIVOS
        if puntos_local < 0 or puntos_visitante < 0:
            error_msg = "Los puntos no pueden ser negativos"
            logger.error(error_msg)
            raise ValueError(error_msg)

        # ESTABLEZCO LOS PUNTOS
        self.puntos_local = puntos_local
        self.puntos_visitante = puntos_visitante
        self.finalizado = True

        logger.info(f"Resultado registrado: {puntos_local}-{puntos_visitante}")
        return True

    def get_ganador_id(self):
        """
        Obtiene el ID del equipo ganador.

        Returns:
            int: ID del equipo ganador, o None si el partido no ha finalizado o hay empate
        """
        if not self.finalizado:
            return None

        if self.puntos_local > self.puntos_visitante:
            return self.equipo_local_id
        elif self.puntos_visitante > self.puntos_local:
            return self.equipo_visitante_id
        else:
            return None  # Empate (improbable en baloncesto)

    def get_diferencia_puntos(self):
        """
        Obtiene la diferencia de puntos entre los equipos.

        Returns:
            int: La diferencia de puntos en valor absoluto
        """
        return abs(self.puntos_local - self.puntos_visitante)

    def to_dict(self):
        """
        Convierte el partido a un diccionario para la API.

        Returns:
            dict: Representación en diccionario del partido
        """
        return {
            'id': self.id,
            'equipo_local_id': self.equipo_local_id,
            'equipo_visitante_id': self.equipo_visitante_id,
            'puntos_local': self.puntos_local,
            'puntos_visitante': self.puntos_visitante,
            'fecha': self.fecha.isoformat() if self.fecha else None,
            'finalizado': self.finalizado,
            'ganador_id': self.get_ganador_id(),
            'diferencia_puntos': self.get_diferencia_puntos(),
            'fecha_creacion': self.fecha_creacion.isoformat() if self.fecha_creacion else None
        }

    def __repr__(self):
        """
        Devuelve una representación en string del partido.

        Returns:
            str: Representación en string
        """
        estado = "Finalizado" if self.finalizado else "Pendiente"
        if self.finalizado:
            return f"<Partido #{self.id} {estado}: Local {self.puntos_local} - Visitante {self.puntos_visitante}>"
        else:
            return f"<Partido #{self.id} {estado}: {self.fecha.strftime('%d/%m/%Y %H:%M')}>"