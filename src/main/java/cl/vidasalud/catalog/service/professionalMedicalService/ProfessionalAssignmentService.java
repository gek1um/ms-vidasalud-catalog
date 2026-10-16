package cl.vidasalud.catalog.service.professionalMedicalService;

import cl.vidasalud.catalog.dto.MedicalServiceSummaryResponse;
import cl.vidasalud.catalog.dto.ProfessionalSummaryResponse;
import cl.vidasalud.catalog.dto.professionalMedicalService.ProfessionalServiceRequest;
import cl.vidasalud.catalog.dto.professionalMedicalService.ProfessionalServiceResponse;

import java.util.List;

public interface ProfessionalAssignmentService {

    ProfessionalServiceResponse assign(
            ProfessionalServiceRequest request
    );

    void activate(Long id);
    void deactivate(Long id);

    List<ProfessionalServiceResponse>
    findServicesByProfessional(Long professionalId);

    List<ProfessionalServiceResponse>
    findProfessionalsByMedicalService(Long medicalServiceId);

    List<ProfessionalSummaryResponse>
    findProfessionalSummariesByMedicalService(
            Long medicalServiceId
    );

    List<MedicalServiceSummaryResponse>
    findServiceSummariesByProfessional(
            Long professionalId
    );
}