#!/usr/bin/env python
# -*- coding: utf-8 -*-

"""
Rutas de la API de baloncesto.

Este módulo define los endpoints de la API REST para acceder a los
datos de equipos y partidos de baloncesto.

Author: Mario Flores
Version: 1.0
Date: 2025-05-04
"""

from flask import jsonify, request, Blueprint
from app import app, db
from models import Equipo, Partido
import logging
import requests
import traceback
from datetime import datetime

# CONFIGURO EL LOGGER
logger = logging.getLogger(__name__)

# CREO UN BLUEPRINT PARA LA API
api = Blueprint('api', __name__, url_prefix='/api')

@api.route('/healthcheck', methods=['GET'])
def healthcheck():
    """
    Endpoint para verificar que la API está funcionando correctamente.

    Returns:
        Respuesta JSON indicando el estado de la API
    """
    logger.info("Petición de healthcheck recibida")
    return jsonify({
        'status': 'ok',
        'message': 'API de baloncesto funcionando correctamente',
        'timestamp': datetime.now().isoformat()
    })

@api.route('/pokemon/<pokemon_id>', methods=['GET'])
def get_pokemon(pokemon_id):
    """
    Obtiene información de un Pokémon por su ID o nombre.

    Args:
        pokemon_id: ID o nombre del Pokémon

    Returns:
        Datos del Pokémon en formato JSON
    """
    try:
        # URL de la API de Pokémon
        url = f"https://pokeapi.co/api/v2/pokemon/{pokemon_id.lower()}"

        # Realizo la petición HTTP
        import requests
        response = requests.get(url)
        response.raise_for_status()  # Lanza una excepción si hay error

        # Extraigo los datos relevantes
        data = response.json()
        pokemon_data = {
            'id': data['id'],
            'name': data['name'],
            'height': data['height'],
            'weight': data['weight'],
            'types': [t['type']['name'] for t in data['types']],
            'image': data['sprites']['front_default']
        }

        return jsonify(pokemon_data)
    except Exception as e:
        return jsonify({'error': str(e)}), 500

# ENDPOINTS PARA EQUIPOS

@api.route('/equipos', methods=['GET'])
def get_equipos():
    """
    Obtiene todos los equipos.

    Returns:
        Lista de equipos en formato JSON
    """
    try:
        logger.info("Obteniendo todos los equipos")
        equipos = Equipo.query.all()
        return jsonify([equipo.to_dict() for equipo in equipos])
    except Exception as e:
        logger.error(f"Error al obtener equipos: {str(e)}")
        return jsonify({'error': 'Error al obtener equipos', 'details': str(e)}), 500

@api.route('/equipos/<int:id>', methods=['GET'])
def get_equipo(id):
    """
    Obtiene un equipo por su ID.

    Args:
        id: ID del equipo

    Returns:
        Datos del equipo en formato JSON
    """
    try:
        logger.info(f"Obteniendo equipo con ID: {id}")
        equipo = Equipo.query.get(id)

        if equipo:
            return jsonify(equipo.to_dict())
        else:
            return jsonify({'error': 'Equipo no encontrado'}), 404
    except Exception as e:
        logger.error(f"Error al obtener equipo {id}: {str(e)}")
        return jsonify({'error': 'Error al obtener equipo', 'details': str(e)}), 500

@api.route('/equipos', methods=['POST'])
def create_equipo():
    """
    Crea un nuevo equipo.

    Returns:
        Datos del equipo creado en formato JSON
    """
    try:
        logger.info("Creando nuevo equipo")
        data = request.get_json()

        # VERIFICO QUE LOS DATOS REQUERIDOS ESTÉN PRESENTES
        if not all(k in data for k in ['nombre', 'ciudad', 'entrenador']):
            return jsonify({'error': 'Faltan datos requeridos (nombre, ciudad, entrenador)'}), 400

        # CREO Y GUARDO EL NUEVO EQUIPO
        equipo = Equipo(
            nombre=data['nombre'],
            ciudad=data['ciudad'],
            entrenador=data['entrenador']
        )

        db.session.add(equipo)
        db.session.commit()

        logger.info(f"Equipo creado con ID: {equipo.id}")
        return jsonify(equipo.to_dict()), 201
    except Exception as e:
        db.session.rollback()
        logger.error(f"Error al crear equipo: {str(e)}")
        return jsonify({'error': 'Error al crear equipo', 'details': str(e)}), 500

@api.route('/equipos/<int:id>', methods=['PUT'])
def update_equipo(id):
    """
    Actualiza un equipo existente.

    Args:
        id: ID del equipo

    Returns:
        Datos del equipo actualizado en formato JSON
    """
    try:
        logger.info(f"Actualizando equipo con ID: {id}")
        equipo = Equipo.query.get(id)

        if not equipo:
            return jsonify({'error': 'Equipo no encontrado'}), 404

        data = request.get_json()

        # ACTUALIZO LOS CAMPOS DEL EQUIPO
        if 'nombre' in data:
            equipo.nombre = data['nombre']
        if 'ciudad' in data:
            equipo.ciudad = data['ciudad']
        if 'entrenador' in data:
            equipo.entrenador = data['entrenador']

        db.session.commit()

        logger.info(f"Equipo {id} actualizado correctamente")
        return jsonify(equipo.to_dict())
    except Exception as e:
        db.session.rollback()
        logger.error(f"Error al actualizar equipo {id}: {str(e)}")
        return jsonify({'error': 'Error al actualizar equipo', 'details': str(e)}), 500

@api.route('/equipos/<int:id>', methods=['DELETE'])
def delete_equipo(id):
    """
    Elimina un equipo.

    Args:
        id: ID del equipo

    Returns:
        Mensaje de confirmación
    """
    try:
        logger.info(f"Eliminando equipo con ID: {id}")
        equipo = Equipo.query.get(id)

        if not equipo:
            return jsonify({'error': 'Equipo no encontrado'}), 404

        db.session.delete(equipo)
        db.session.commit()

        logger.info(f"Equipo {id} eliminado correctamente")
        return jsonify({'message': f'Equipo {id} eliminado correctamente'})
    except Exception as e:
        db.session.rollback()
        logger.error(f"Error al eliminar equipo {id}: {str(e)}")
        return jsonify({'error': 'Error al eliminar equipo', 'details': str(e)}), 500

# ENDPOINTS PARA PARTIDOS

@api.route('/partidos', methods=['GET'])
def get_partidos():
    """
    Obtiene todos los partidos.

    Returns:
        Lista de partidos en formato JSON
    """
    try:
        logger.info("Obteniendo todos los partidos")
        partidos = Partido.query.all()
        return jsonify([partido.to_dict() for partido in partidos])
    except Exception as e:
        logger.error(f"Error al obtener partidos: {str(e)}")
        return jsonify({'error': 'Error al obtener partidos', 'details': str(e)}), 500

@api.route('/partidos/<int:id>', methods=['GET'])
def get_partido(id):
    """
    Obtiene un partido por su ID.

    Args:
        id: ID del partido

    Returns:
        Datos del partido en formato JSON
    """
    try:
        logger.info(f"Obteniendo partido con ID: {id}")
        partido = Partido.query.get(id)

        if partido:
            return jsonify(partido.to_dict())
        else:
            return jsonify({'error': 'Partido no encontrado'}), 404
    except Exception as e:
        logger.error(f"Error al obtener partido {id}: {str(e)}")
        return jsonify({'error': 'Error al obtener partido', 'details': str(e)}), 500

@api.route('/partidos', methods=['POST'])
def create_partido():
    """
    Crea un nuevo partido.

    Returns:
        Datos del partido creado en formato JSON
    """
    try:
        logger.info("Creando nuevo partido")
        data = request.get_json()

        # VERIFICO QUE LOS DATOS REQUERIDOS ESTÉN PRESENTES
        if not all(k in data for k in ['equipo_local_id', 'equipo_visitante_id', 'fecha']):
            return jsonify({'error': 'Faltan datos requeridos (equipo_local_id, equipo_visitante_id, fecha)'}), 400

        # VERIFICO QUE LOS EQUIPOS NO SEAN EL MISMO
        if data['equipo_local_id'] == data['equipo_visitante_id']:
            return jsonify({'error': 'El equipo local y visitante no pueden ser el mismo'}), 400

        # CREO Y GUARDO EL NUEVO PARTIDO
        partido = Partido(
            equipo_local_id=data['equipo_local_id'],
            equipo_visitante_id=data['equipo_visitante_id'],
            fecha=datetime.fromisoformat(data['fecha'])
        )

        db.session.add(partido)
        db.session.commit()

        logger.info(f"Partido creado con ID: {partido.id}")
        return jsonify(partido.to_dict()), 201
    except Exception as e:
        db.session.rollback()
        logger.error(f"Error al crear partido: {str(e)}")
        return jsonify({'error': 'Error al crear partido', 'details': str(e)}), 500

@api.route('/partidos/<int:id>/resultado', methods=['POST'])
def registrar_resultado(id):
    """
    Registra el resultado de un partido.

    Args:
        id: ID del partido

    Returns:
        Datos del partido actualizado en formato JSON
    """
    try:
        logger.info(f"Registrando resultado para partido con ID: {id}")
        partido = Partido.query.get(id)

        if not partido:
            return jsonify({'error': 'Partido no encontrado'}), 404

        # VERIFICO QUE EL PARTIDO NO ESTÉ YA FINALIZADO
        if partido.finalizado:
            return jsonify({'error': 'El partido ya está finalizado'}), 400

        data = request.get_json()

        # VERIFICO QUE LOS DATOS REQUERIDOS ESTÉN PRESENTES
        if not all(k in data for k in ['puntos_local', 'puntos_visitante']):
            return jsonify({'error': 'Faltan datos requeridos (puntos_local, puntos_visitante)'}), 400

        # VERIFICO QUE LOS PUNTOS SEAN POSITIVOS
        if data['puntos_local'] < 0 or data['puntos_visitante'] < 0:
            return jsonify({'error': 'Los puntos no pueden ser negativos'}), 400

        # REGISTRO EL RESULTADO
        partido.registrar_resultado(data['puntos_local'], data['puntos_visitante'])
        db.session.commit()

        logger.info(f"Resultado registrado para partido {id}")
        return jsonify(partido.to_dict())
    except Exception as e:
        db.session.rollback()
        logger.error(f"Error al registrar resultado para partido {id}: {str(e)}")
        return jsonify({'error': 'Error al registrar resultado', 'details': str(e)}), 500

@api.route('/partidos/<int:id>', methods=['DELETE'])
def delete_partido(id):
    """
    Elimina un partido.

    Args:
        id: ID del partido

    Returns:
        Mensaje de confirmación
    """
    try:
        logger.info(f"Eliminando partido con ID: {id}")
        partido = Partido.query.get(id)

        if not partido:
            return jsonify({'error': 'Partido no encontrado'}), 404

        db.session.delete(partido)
        db.session.commit()

        logger.info(f"Partido {id} eliminado correctamente")
        return jsonify({'message': f'Partido {id} eliminado correctamente'})
    except Exception as e:
        db.session.rollback()
        logger.error(f"Error al eliminar partido {id}: {str(e)}")
        return jsonify({'error': 'Error al eliminar partido', 'details': str(e)}), 500

# ENDPOINTS PARA SIMULAR EXCEPCIONES

@api.route('/exceptions/file', methods=['GET'])
def file_exception():
    """
    Simula una excepción de archivo.

    Returns:
        Error 500 con mensaje de excepción de archivo
    """
    try:
        logger.info("Simulando excepción de archivo")
        # INTENTO ABRIR UN ARCHIVO QUE NO EXISTE
        with open('archivo_inexistente.txt', 'r') as f:
            contenido = f.read()
        return jsonify({'content': contenido})
    except Exception as e:
        logger.error(f"Error de archivo: {str(e)}")
        return jsonify({'error': 'Error de archivo', 'details': str(e)}), 500

@api.route('/exceptions/database', methods=['GET'])
def database_exception():
    """
    Simula una excepción de base de datos.

    Returns:
        Error 500 con mensaje de excepción de base de datos
    """
    try:
        logger.info("Simulando excepción de base de datos")
        # EJECUTO UNA CONSULTA INVÁLIDA
        db.session.execute("SELECT * FROM tabla_inexistente")
        return jsonify({'message': 'Consulta ejecutada correctamente'})
    except Exception as e:
        db.session.rollback()
        logger.error(f"Error de base de datos: {str(e)}")
        return jsonify({'error': 'Error de base de datos', 'details': str(e)}), 500

@api.route('/exceptions/api', methods=['GET'])
def api_exception():
    """
    Simula una excepción de llamada a API externa.

    Returns:
        Error 500 con mensaje de excepción de API
    """
    try:
        logger.info("Simulando excepción de API")
        # INTENTO LLAMAR A UNA API INEXISTENTE
        response = requests.get('https://api.ejemplo.inexistente/data')
        response.raise_for_status()
        return jsonify(response.json())
    except Exception as e:
        logger.error(f"Error de API: {str(e)}")
        return jsonify({'error': 'Error de API', 'details': str(e)}), 500

# REGISTRO EL BLUEPRINT EN LA APLICACIÓN
app.register_blueprint(api)