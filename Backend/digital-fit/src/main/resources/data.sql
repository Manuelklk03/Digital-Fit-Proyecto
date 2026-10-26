-- Insertar datos de entrenamientos base
INSERT IGNORE INTO
    entrenamientos_base (nombre, descripcion)
VALUES (
        'Full Body Principiante',
        'Rutina completa para principiantes: 3 series de 12 repeticiones de sentadillas, flexiones y abdominales.'
    ),
    (
        'Cardio Intensivo',
        'Entrenamiento de resistencia: 20 minutos de cinta o correr en el rio Turia + 10 minutos de salto de cuerda.'
    ),
    (
        'Pierna y Glúteos',
        'Rutina enfocada en tren inferior: sentadillas, zancadas, peso muerto y elevaciones de cadera.'
    ),
    (
        'Espalda y Bíceps',
        'Dominadas, remo con barra, curl de bíceps y ejercicios de fuerza para parte superior.'
    ),
    (
        'Entrenamiento en Parque',
        'Rutina para hacer en zonas públicas: dominadas en barra, fondos, sprint y abdominales en banco.'
    ),
    (
        'HIIT 20 minutos',
        'Entrenamiento de alta intensidad: 30 segundos de ejercicio + 30 segundos descanso (burpees, jumping jacks, mountain climbers).'
    );

--Centros privados
INSERT IGNORE INTO
    centros_privados_base (
        nombre,
        direccion,
        telefono,
        horario,
        precio_mensual,
        descripcion,
        latitud,
        longitud
    )
VALUES (
        'AltaFit Valencia Centro',
        'Calle Xàtiva 14, Valencia',
        '960111111',
        'L-V 7:00-23:00',
        29.90,
        'Gimnasio moderno en el centro con zona de musculación y clases dirigidas.',
        39.4699,
        -0.3763
    ),
    (
        'DreamFit Valencia',
        'Av. del Cid 62, Valencia',
        '961222222',
        'L-D 6:00-24:00',
        24.90,
        'Gimnasio amplio con zona cardio y entrenamiento funcional.',
        39.4712,
        -0.3974
    ),
    (
        'Basic-Fit Valencia Ruzafa',
        'Carrer de Cadis 10, Valencia',
        '962333333',
        'L-D 6:00-23:00',
        19.99,
        'Centro económico con máquinas modernas.',
        39.4637,
        -0.3704
    ),
    (
        'Synergym Valencia',
        'Av. del Puerto 123, Valencia',
        '963444444',
        'L-D 6:00-24:00',
        26.99,
        'Gimnasio completo con zona cross training.',
        39.4630,
        -0.3405
    ),
    (
        'Fitness Park Valencia',
        'Calle Menorca 19, Valencia',
        '964555555',
        'L-D 6:00-23:30',
        27.99,
        'Centro con maquinaria profesional y zona funcional.',
        39.4587,
        -0.3320
    ),
    (
        'Metropolitan Valencia',
        'Av. de Francia 20, Valencia',
        '965666666',
        'L-D 7:00-22:00',
        49.90,
        'Centro premium con piscina y spa.',
        39.4561,
        -0.3468
    );