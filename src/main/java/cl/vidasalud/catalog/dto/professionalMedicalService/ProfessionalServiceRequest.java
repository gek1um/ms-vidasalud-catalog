package cl.vidasalud.catalog.dto.professionalMedicalService;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record ProfessionalServiceRequest(

        @NotNull
        @Positive
        Long professionalId,

        @NotNull
        @Positive
        Long medicalServiceId

) {
}