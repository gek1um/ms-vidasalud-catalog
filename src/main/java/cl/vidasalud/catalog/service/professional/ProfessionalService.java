package cl.vidasalud.catalog.service.professional;

import cl.vidasalud.catalog.dto.professional.ProfessionalRequest;
import cl.vidasalud.catalog.dto.professional.ProfessionalResponse;

import java.util.List;

public interface ProfessionalService {

    List<ProfessionalResponse> findAll(String specialtyCode);

    ProfessionalResponse findById(Long id);

    ProfessionalResponse create(ProfessionalRequest request);

    ProfessionalResponse update(
            Long id,
            ProfessionalRequest request
    );

    void activate(Long id);
    void deactivate(Long id);
}