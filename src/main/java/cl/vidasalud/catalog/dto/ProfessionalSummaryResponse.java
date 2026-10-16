package cl.vidasalud.catalog.dto;

public record ProfessionalSummaryResponse(

        Long id,
        String rut,
        String firstName,
        String lastName,

        Long specialtyId,
        String specialtyCode,
        String specialtyName

) {
}