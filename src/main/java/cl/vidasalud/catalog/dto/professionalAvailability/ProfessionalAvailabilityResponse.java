package cl.vidasalud.catalog.dto.professionalAvailability;

import java.time.LocalTime;

public record ProfessionalAvailabilityResponse(

        Long id,

        Long professionalId,
        String professionalName,

        Long boxId,
        String boxCode,
        String boxName,

        Short dayOfWeek,

        LocalTime startTime,
        LocalTime endTime,

        Boolean active

) {
}