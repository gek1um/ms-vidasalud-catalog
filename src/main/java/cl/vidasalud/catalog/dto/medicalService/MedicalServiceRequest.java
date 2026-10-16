package cl.vidasalud.catalog.dto.medicalService;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record MedicalServiceRequest(

        @NotBlank(message = "Code is required")
        @Size(
                max = 50,
                message = "Code cannot exceed 50 characters"
        )
        String code,


        @NotBlank(message = "Name is required")
        @Size(
                max = 150,
                message = "Name cannot exceed 150 characters"
        )
        String name,


        @Size(
                max = 500,
                message = "Description cannot exceed 500 characters"
        )
        String description,


        @NotNull(message = "Specialty is required")
        @Positive(message = "Specialty id must be greater than zero")
        Long specialtyId,


        @NotNull(message = "Duration is required")
        @Positive(message = "Duration must be greater than zero")
        Integer durationMinutes,


        @NotNull(message = "Price is required")
        @DecimalMin(
                value = "0.0",
                inclusive = true,
                message = "Price cannot be negative"
        )
        BigDecimal price

) {
}