package cl.vidasalud.catalog.dto.specialty;

public record SpecialtyResponse(
        Long id,
        String code,
        String name,
        String description,
        Boolean active
) {
}