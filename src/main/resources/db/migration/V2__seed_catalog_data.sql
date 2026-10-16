-- =========================================================
-- ESPECIALIDADES
-- =========================================================

INSERT INTO specialties (code, name, description, active)
VALUES
    ('MEDGEN', 'Medicina General', 'Atención médica general para pacientes adultos y pediátricos.', TRUE),
    ('CARD', 'Cardiología', 'Evaluación y control de patologías cardiovasculares.', TRUE),
    ('TRAU', 'Traumatología', 'Diagnóstico y tratamiento de patologías musculoesqueléticas.', TRUE),
    ('PED', 'Pediatría', 'Atención médica para pacientes pediátricos.', TRUE),
    ('GINE', 'Ginecología', 'Atención y control de salud ginecológica.', TRUE);


-- =========================================================
-- PRESTACIONES MÉDICAS
-- =========================================================

INSERT INTO medical_services
(code, name, description, specialty_id, duration_minutes, price, active)
VALUES
    (
        'MEDGEN-001',
        'Consulta Medicina General',
        'Consulta médica general para evaluación, diagnóstico y tratamiento.',
        (SELECT id FROM specialties WHERE code = 'MEDGEN'),
        30,
        25000,
        TRUE
    ),
    (
        'MEDGEN-002',
        'Control Medicina General',
        'Control posterior a una consulta médica general.',
        (SELECT id FROM specialties WHERE code = 'MEDGEN'),
        20,
        18000,
        TRUE
    ),
    (
        'CARD-001',
        'Consulta Cardiología',
        'Evaluación médica especializada en cardiología.',
        (SELECT id FROM specialties WHERE code = 'CARD'),
        30,
        45000,
        TRUE
    ),
    (
        'CARD-002',
        'Control Cardiología',
        'Control de seguimiento cardiológico.',
        (SELECT id FROM specialties WHERE code = 'CARD'),
        20,
        35000,
        TRUE
    ),
    (
        'CARD-003',
        'Electrocardiograma',
        'Registro de la actividad eléctrica del corazón.',
        (SELECT id FROM specialties WHERE code = 'CARD'),
        20,
        22000,
        TRUE
    ),
    (
        'TRAU-001',
        'Consulta Traumatología',
        'Evaluación de patologías musculoesqueléticas.',
        (SELECT id FROM specialties WHERE code = 'TRAU'),
        30,
        40000,
        TRUE
    ),
    (
        'TRAU-002',
        'Control Traumatología',
        'Control posterior a consulta traumatológica.',
        (SELECT id FROM specialties WHERE code = 'TRAU'),
        20,
        30000,
        TRUE
    ),
    (
        'PED-001',
        'Consulta Pediatría',
        'Evaluación pediátrica general.',
        (SELECT id FROM specialties WHERE code = 'PED'),
        30,
        32000,
        TRUE
    ),
    (
        'PED-002',
        'Control Pediatría',
        'Seguimiento de pacientes pediátricos.',
        (SELECT id FROM specialties WHERE code = 'PED'),
        20,
        24000,
        TRUE
    ),
    (
        'GINE-001',
        'Consulta Ginecología',
        'Evaluación ginecológica general.',
        (SELECT id FROM specialties WHERE code = 'GINE'),
        30,
        42000,
        TRUE
    );


-- =========================================================
-- PROFESIONALES
-- =========================================================

INSERT INTO professionals
(rut, first_name, last_name, specialty_id, email, phone, active)
VALUES
    (
        '15.555.555-5',
        'María',
        'González',
        (SELECT id FROM specialties WHERE code = 'CARD'),
        'maria.gonzalez@vidasalud.cl',
        '+56911111111',
        TRUE
    ),
    (
        '16.666.666-6',
        'Juan',
        'Pérez',
        (SELECT id FROM specialties WHERE code = 'MEDGEN'),
        'juan.perez@vidasalud.cl',
        '+56922222222',
        TRUE
    ),
    (
        '17.777.777-7',
        'Camila',
        'Rojas',
        (SELECT id FROM specialties WHERE code = 'TRAU'),
        'camila.rojas@vidasalud.cl',
        '+56933333333',
        TRUE
    ),
    (
        '18.888.888-8',
        'Felipe',
        'Muñoz',
        (SELECT id FROM specialties WHERE code = 'PED'),
        'felipe.munoz@vidasalud.cl',
        '+56944444444',
        TRUE
    ),
    (
        '19.999.999-9',
        'Daniela',
        'Soto',
        (SELECT id FROM specialties WHERE code = 'GINE'),
        'daniela.soto@vidasalud.cl',
        '+56955555555',
        TRUE
    );


-- =========================================================
-- BOXES
-- =========================================================

INSERT INTO boxes
(code, name, location, floor, active)
VALUES
    ('BOX-01', 'Box 1', 'Centro Médico', 'Piso 1', TRUE),
    ('BOX-02', 'Box 2', 'Centro Médico', 'Piso 1', TRUE),
    ('BOX-CARD-01', 'Box Cardiología 1', 'Centro Médico', 'Piso 2', TRUE),
    ('BOX-TRAU-01', 'Box Traumatología 1', 'Centro Médico', 'Piso 2', TRUE),
    ('BOX-PED-01', 'Box Pediatría 1', 'Centro Médico', 'Piso 3', TRUE),
    ('BOX-GINE-01', 'Box Ginecología 1', 'Centro Médico', 'Piso 3', TRUE);


-- =========================================================
-- RELACIÓN PROFESIONAL - PRESTACIÓN
-- =========================================================

INSERT INTO professional_services
(professional_id, medical_service_id, active)
VALUES
    (
        (SELECT id FROM professionals WHERE rut = '15.555.555-5'),
        (SELECT id FROM medical_services WHERE code = 'CARD-001'),
        TRUE
    ),
    (
        (SELECT id FROM professionals WHERE rut = '15.555.555-5'),
        (SELECT id FROM medical_services WHERE code = 'CARD-002'),
        TRUE
    ),
    (
        (SELECT id FROM professionals WHERE rut = '15.555.555-5'),
        (SELECT id FROM medical_services WHERE code = 'CARD-003'),
        TRUE
    ),
    (
        (SELECT id FROM professionals WHERE rut = '16.666.666-6'),
        (SELECT id FROM medical_services WHERE code = 'MEDGEN-001'),
        TRUE
    ),
    (
        (SELECT id FROM professionals WHERE rut = '16.666.666-6'),
        (SELECT id FROM medical_services WHERE code = 'MEDGEN-002'),
        TRUE
    ),
    (
        (SELECT id FROM professionals WHERE rut = '17.777.777-7'),
        (SELECT id FROM medical_services WHERE code = 'TRAU-001'),
        TRUE
    ),
    (
        (SELECT id FROM professionals WHERE rut = '17.777.777-7'),
        (SELECT id FROM medical_services WHERE code = 'TRAU-002'),
        TRUE
    ),
    (
        (SELECT id FROM professionals WHERE rut = '18.888.888-8'),
        (SELECT id FROM medical_services WHERE code = 'PED-001'),
        TRUE
    ),
    (
        (SELECT id FROM professionals WHERE rut = '18.888.888-8'),
        (SELECT id FROM medical_services WHERE code = 'PED-002'),
        TRUE
    ),
    (
        (SELECT id FROM professionals WHERE rut = '19.999.999-9'),
        (SELECT id FROM medical_services WHERE code = 'GINE-001'),
        TRUE
    );


-- =========================================================
-- DISPONIBILIDAD PROFESIONAL
-- 1 = Lunes
-- 2 = Martes
-- 3 = Miércoles
-- 4 = Jueves
-- 5 = Viernes
-- 6 = Sábado
-- 7 = Domingo
-- =========================================================

INSERT INTO professional_availability
(professional_id, box_id, day_of_week, start_time, end_time, active)
VALUES

-- María González - Cardiología
(
    (SELECT id FROM professionals WHERE rut = '15.555.555-5'),
    (SELECT id FROM boxes WHERE code = 'BOX-CARD-01'),
    1,
    '08:00',
    '13:00',
    TRUE
),
(
    (SELECT id FROM professionals WHERE rut = '15.555.555-5'),
    (SELECT id FROM boxes WHERE code = 'BOX-CARD-01'),
    3,
    '08:00',
    '13:00',
    TRUE
),
(
    (SELECT id FROM professionals WHERE rut = '15.555.555-5'),
    (SELECT id FROM boxes WHERE code = 'BOX-CARD-01'),
    5,
    '14:00',
    '18:00',
    TRUE
),

-- Juan Pérez - Medicina General
(
    (SELECT id FROM professionals WHERE rut = '16.666.666-6'),
    (SELECT id FROM boxes WHERE code = 'BOX-01'),
    1,
    '09:00',
    '17:00',
    TRUE
),
(
    (SELECT id FROM professionals WHERE rut = '16.666.666-6'),
    (SELECT id FROM boxes WHERE code = 'BOX-01'),
    2,
    '09:00',
    '17:00',
    TRUE
),
(
    (SELECT id FROM professionals WHERE rut = '16.666.666-6'),
    (SELECT id FROM boxes WHERE code = 'BOX-02'),
    4,
    '09:00',
    '17:00',
    TRUE
),

-- Camila Rojas - Traumatología
(
    (SELECT id FROM professionals WHERE rut = '17.777.777-7'),
    (SELECT id FROM boxes WHERE code = 'BOX-TRAU-01'),
    2,
    '08:30',
    '12:30',
    TRUE
),
(
    (SELECT id FROM professionals WHERE rut = '17.777.777-7'),
    (SELECT id FROM boxes WHERE code = 'BOX-TRAU-01'),
    4,
    '14:00',
    '18:00',
    TRUE
),

-- Felipe Muñoz - Pediatría
(
    (SELECT id FROM professionals WHERE rut = '18.888.888-8'),
    (SELECT id FROM boxes WHERE code = 'BOX-PED-01'),
    1,
    '08:00',
    '14:00',
    TRUE
),
(
    (SELECT id FROM professionals WHERE rut = '18.888.888-8'),
    (SELECT id FROM boxes WHERE code = 'BOX-PED-01'),
    3,
    '08:00',
    '14:00',
    TRUE
),

-- Daniela Soto - Ginecología
(
    (SELECT id FROM professionals WHERE rut = '19.999.999-9'),
    (SELECT id FROM boxes WHERE code = 'BOX-GINE-01'),
    2,
    '10:00',
    '16:00',
    TRUE
),
(
    (SELECT id FROM professionals WHERE rut = '19.999.999-9'),
    (SELECT id FROM boxes WHERE code = 'BOX-GINE-01'),
    5,
    '09:00',
    '15:00',
    TRUE
);