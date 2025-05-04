#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Modelos de datos para la aplicación de baloncesto.

Este módulo define los modelos SQLAlchemy para los equipos y partidos de baloncesto.

Author: Mario Flores
Version: 1.0
Date: 2025-05-04
"""

from __init__ import db
from datetime import datetime


class Equipo(db.Model):
    """
    Modelo para representar un equipo de baloncesto.
    """
    __tablename__ = 'equipos'

    id = db.Column(db.Integer, primary_key=True)
    nombre = db.Column(db.String(100), nullable=False)
    ciudad = db.Column(db.String(100), nullable=False)
    entrenador = db.Column(db.String(100), nullable=False)
    fecha_creacion = db.Column(db.DateTime, default=datetime.now)

    # Relaciones
    partidos_local = db.relationship('Partido', foreign_keys='Partido.equipo_local_id', backref='equipo_local')
    partidos_visitante = db.relationship('Partido', foreign_keys='Partido.equipo_visitante_id', backref='equipo_visitante')

    def __init__(self, nombre, ciudad, entrenador):
        self.nombre = nombre
        self.ciudad = ciudad
        self.entrenador = entrenador

    def to_dict(self):
        """
        Convierte el equipo a un diccionario para ser serializado a JSON.
        """
        return {
            'id': self.id,
            'nombre': self.nombre,
            'ciudad': self.ciudad,
            'entrenador': self.entrenador,
            'fecha_creacion': self.fecha_creacion.isoformat() if self.fecha_creacion else None
        }

    def __repr__(self):
        return f'<Equipo {self.nombre} ({self.ciudad})>'


class Partido(db.Model):
    """
    Modelo para representar un partido de baloncesto.
    """
    __tablename__ = 'partidos'

    id = db.Column(db.Integer, primary_key=True)
    equipo_local_id = db.Column(db.Integer, db.ForeignKey('equipos.id'), nullable=False)
    equipo_visitante_id = db.Column(db.Integer, db.ForeignKey('equipos.id'), nullable=False)
    fecha = db.Column(db.DateTime, nullable=False)
    puntos_local = db.Column(db.Integer, nullable=True)
    puntos_visitante = db.Column(db.Integer, nullable=True)
    finalizado = db.Column(db.Boolean, default=False)
    fecha_creacion = db.Column(db.DateTime, default=datetime.now)

    def __init__(self, equipo_local_id, equipo_visitante_id, fecha):
        self.equipo_local_id = equipo_local_id
        self.equipo_visitante_id = equipo_visitante_id
        self.fecha = fecha

    def registrar_resultado(self, puntos_local, puntos_visitante):
        """
        Registra el resultado de un partido.
        """
        self.puntos_local = puntos_local
        self.puntos_visitante = puntos_visitante
        self.finalizado = True

    def to_dict(self):
        """
        Convierte el partido a un diccionario para ser serializado a JSON.
        """
        return {
            'id': self.id,
            'equipo_local_id': self.equipo_local_id,
            'equipo_visitante_id': self.equipo_visitante_id,
            'equipo_local': self.equipo_local.nombre if self.equipo_local else None,
            'equipo_visitante': self.equipo_visitante.nombre if self.equipo_visitante else None,
            'fecha': self.fecha.isoformat() if self.fecha else None,
            'puntos_local': self.puntos_local,
            'puntos_visitante': self.puntos_visitante,
            'finalizado': self.finalizado,
            'ganador': self._obtener_ganador(),
            'fecha_creacion': self.fecha_creacion.isoformat() if self.fecha_creacion else None
        }

    def _obtener_ganador(self):
        """
        Determina el ganador del partido.
        """
        if not self.finalizado:
            return None

        if self.puntos_local > self.puntos_visitante:
            return self.equipo_local.nombre
        elif self.puntos_visitante > self.puntos_local:
            return self.equipo_visitante.nombre
        else:
            return "Empate"

    def __repr__(self):
        return f'<Partido {self.id}: {self.equipo_local.nombre if self.equipo_local else "?"} vs {self.equipo_visitante.nombre if self.equipo_visitante else "?"} ({self.fecha})>'