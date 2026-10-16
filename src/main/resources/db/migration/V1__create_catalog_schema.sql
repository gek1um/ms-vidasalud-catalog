-- =========================================================
-- VIDA SALUD - CATALOG MICROSERVICE
-- V1 - CREACIÓN DEL ESQUEMA INICIAL
-- PostgreSQL
-- =========================================================


-- =========================================================
-- 1. SPECIALTIES
-- Especialidades médicas
-- =========================================================

CREATE TABLE specialties (
                             id BIGSERIAL PRIMARY KEY,

                             code VARCHAR(50) NOT NULL,
                             name VARCHAR(150) NOT NULL,
                             description VARCHAR(500),

                             active BOOLEAN NOT NULL DEFAULT TRUE,

                             created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                             CONSTRAINT uq_specialties_code UNIQUE (code),
                             CONSTRAINT uq_specialties_name UNIQUE (name)
);


-- =========================================================
-- 2. MEDICAL SERVICES
-- Prestaciones médicas disponibles en VidaSalud
-- =========================================================

CREATE TABLE medical_services (
                                  id BIGSERIAL PRIMARY KEY,

                                  code VARCHAR(50) NOT NULL,
                                  name VARCHAR(150) NOT NULL,
                                  description VARCHAR(500),

                                  specialty_id BIGINT NOT NULL,

                                  duration_minutes INTEGER NOT NULL,
                                  price NUMERIC(12,2) NOT NULL,

                                  active BOOLEAN NOT NULL DEFAULT TRUE,

                                  created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                  CONSTRAINT uq_medical_services_code
                                      UNIQUE (code),

                                  CONSTRAINT fk_medical_services_specialty
                                      FOREIGN KEY (specialty_id)
                                          REFERENCES specialties(id),

                                  CONSTRAINT chk_medical_services_duration
                                      CHECK (duration_minutes > 0),

                                  CONSTRAINT chk_medical_services_price
                                      CHECK (price >= 0)
);


-- =========================================================
-- 3. PROFESSIONALS
-- Profesionales de salud
-- =========================================================

CREATE TABLE professionals (
                               id BIGSERIAL PRIMARY KEY,

                               rut VARCHAR(20) NOT NULL,

                               first_name VARCHAR(100) NOT NULL,
                               last_name VARCHAR(100) NOT NULL,

                               specialty_id BIGINT NOT NULL,

                               email VARCHAR(150),
                               phone VARCHAR(30),

                               active BOOLEAN NOT NULL DEFAULT TRUE,

                               created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                               CONSTRAINT uq_professionals_rut
                                   UNIQUE (rut),

                               CONSTRAINT uq_professionals_email
                                   UNIQUE (email),

                               CONSTRAINT fk_professionals_specialty
                                   FOREIGN KEY (specialty_id)
                                       REFERENCES specialties(id)
);


-- =========================================================
-- 4. BOXES
-- Boxes o salas físicas de atención
-- =========================================================

CREATE TABLE boxes (
                       id BIGSERIAL PRIMARY KEY,

                       code VARCHAR(50) NOT NULL,
                       name VARCHAR(100) NOT NULL,

                       location VARCHAR(150),
                       floor VARCHAR(50),

                       active BOOLEAN NOT NULL DEFAULT TRUE,

                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT uq_boxes_code
                           UNIQUE (code)
);


-- =========================================================
-- 5. PROFESSIONAL SERVICES
-- Relación N:M entre profesionales y prestaciones
-- =========================================================

CREATE TABLE professional_services (
                                       id BIGSERIAL PRIMARY KEY,

                                       professional_id BIGINT NOT NULL,
                                       medical_service_id BIGINT NOT NULL,

                                       active BOOLEAN NOT NULL DEFAULT TRUE,

                                       created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                       updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                       CONSTRAINT fk_professional_services_professional
                                           FOREIGN KEY (professional_id)
                                               REFERENCES professionals(id),

                                       CONSTRAINT fk_professional_services_medical_service
                                           FOREIGN KEY (medical_service_id)
                                               REFERENCES medical_services(id),

                                       CONSTRAINT uq_professional_services_relation
                                           UNIQUE (professional_id, medical_service_id)
);


-- =========================================================
-- 6. PROFESSIONAL AVAILABILITY
-- Disponibilidad semanal base de los profesionales
--
-- day_of_week:
-- 1 = Lunes
-- 2 = Martes
-- 3 = Miércoles
-- 4 = Jueves
-- 5 = Viernes
-- 6 = Sábado
-- 7 = Domingo
-- =========================================================

CREATE TABLE professional_availability (
                                           id BIGSERIAL PRIMARY KEY,

                                           professional_id BIGINT NOT NULL,
                                           box_id BIGINT,

                                           day_of_week SMALLINT NOT NULL,

                                           start_time TIME NOT NULL,
                                           end_time TIME NOT NULL,

                                           active BOOLEAN NOT NULL DEFAULT TRUE,

                                           created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

                                           CONSTRAINT fk_professional_availability_professional
                                               FOREIGN KEY (professional_id)
                                                   REFERENCES professionals(id),

                                           CONSTRAINT fk_professional_availability_box
                                               FOREIGN KEY (box_id)
                                                   REFERENCES boxes(id),

                                           CONSTRAINT chk_professional_availability_day
                                               CHECK (day_of_week BETWEEN 1 AND 7),

                                           CONSTRAINT chk_professional_availability_time
                                               CHECK (start_time < end_time)
);


-- =========================================================
-- ÍNDICES
-- Mejoran consultas frecuentes
-- =========================================================

CREATE INDEX idx_medical_services_specialty
    ON medical_services(specialty_id);

CREATE INDEX idx_medical_services_active
    ON medical_services(active);

CREATE INDEX idx_professionals_specialty
    ON professionals(specialty_id);

CREATE INDEX idx_professionals_active
    ON professionals(active);

CREATE INDEX idx_professional_services_professional
    ON professional_services(professional_id);

CREATE INDEX idx_professional_services_medical_service
    ON professional_services(medical_service_id);

CREATE INDEX idx_professional_availability_professional
    ON professional_availability(professional_id);

CREATE INDEX idx_professional_availability_box
    ON professional_availability(box_id);

CREATE INDEX idx_professional_availability_day
    ON professional_availability(day_of_week);