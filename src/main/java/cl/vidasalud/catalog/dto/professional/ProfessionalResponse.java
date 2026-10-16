package cl.vidasalud.catalog.dto.professional;

public record ProfessionalResponse(

        Long id,
        String rut,
        String firstName,
        String lastName,

        Long specialtyId,
        String specialtyCode,
        String specialtyName,

        String email,
        String phone,
        Boolean active

) {
}