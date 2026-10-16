package cl.vidasalud.catalog.dto.professionalMedicalService;

public record ProfessionalServiceResponse(

        Long id,

        Long professionalId,
        String professionalName,

        Long medicalServiceId,
        String medicalServiceCode,
        String medicalServiceName,

        Boolean active

) {
}