package cl.vidasalud.catalog.dto.professional;

import jakarta.validation.constraints.*;

public record ProfessionalRequest(

        @NotBlank(message = "RUT is required")
        @Size(max = 20)
        String rut,

        @NotBlank(message = "First name is required")
        @Size(max = 100)
        String firstName,

        @NotBlank(message = "Last name is required")
        @Size(max = 100)
        String lastName,

        @NotNull(message = "Specialty is required")
        @Positive
        Long specialtyId,

        @Email(message = "Email must be valid")
        @Size(max = 150)
        String email,

        @Size(max = 30)
        String phone

) {
}