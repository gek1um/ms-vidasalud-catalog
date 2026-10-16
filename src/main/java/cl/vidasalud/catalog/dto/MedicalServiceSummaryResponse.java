package cl.vidasalud.catalog.dto;

import java.math.BigDecimal;

public record MedicalServiceSummaryResponse(

        Long id,
        String code,
        String name,

        Integer durationMinutes,
        BigDecimal price

) {
}