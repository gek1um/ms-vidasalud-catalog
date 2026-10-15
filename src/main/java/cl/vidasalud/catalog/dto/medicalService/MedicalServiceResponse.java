package cl.vidasalud.catalog.dto.medicalService;

import java.math.BigDecimal;

public record MedicalServiceResponse(

        Long id,
        String code,
        String name,
        String description,

        Long specialtyId,
        String specialtyCode,
        String specialtyName,

        Integer durationMinutes,
        BigDecimal price,

        Boolean active
) {
}