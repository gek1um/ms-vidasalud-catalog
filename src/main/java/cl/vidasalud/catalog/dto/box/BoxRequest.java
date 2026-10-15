package cl.vidasalud.catalog.dto.box;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record BoxRequest(

        @NotBlank(message = "Code is required")
        @Size(max = 50)
        String code,

        @NotBlank(message = "Name is required")
        @Size(max = 100)
        String name,

        @Size(max = 150)
        String location,

        @Size(max = 50)
        String floor

) {
}