package cl.vidasalud.catalog.dto.professionalAvailability;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalTime;

public record ProfessionalAvailabilityRequest(

        @NotNull
        @Positive
        Long professionalId,

        @NotNull
        @Positive
        Long boxId,

        @NotNull
        @Min(1)
        @Max(7)
        Short dayOfWeek,

        @NotNull
        LocalTime startTime,

        @NotNull
        LocalTime endTime

) {
}