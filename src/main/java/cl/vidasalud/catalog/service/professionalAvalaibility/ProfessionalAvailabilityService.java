package cl.vidasalud.catalog.service.professionalAvalaibility;

import cl.vidasalud.catalog.dto.professionalAvailability.ProfessionalAvailabilityRequest;
import cl.vidasalud.catalog.dto.professionalAvailability.ProfessionalAvailabilityResponse;

import java.util.List;

public interface ProfessionalAvailabilityService {

    List<ProfessionalAvailabilityResponse>
    findByProfessional(Long professionalId);

    ProfessionalAvailabilityResponse create(
            ProfessionalAvailabilityRequest request
    );

    ProfessionalAvailabilityResponse update(
            Long id,
            ProfessionalAvailabilityRequest request
    );

    void activate(Long id);
    void deactivate(Long id);
}