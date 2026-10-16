package cl.vidasalud.catalog.service.medicalService;

import cl.vidasalud.catalog.dto.medicalService.MedicalServiceRequest;
import cl.vidasalud.catalog.dto.medicalService.MedicalServiceResponse;

import java.util.List;

public interface MedicalServiceService {

    List<MedicalServiceResponse> findAll(String specialtyCode);

    MedicalServiceResponse findById(Long id);

    MedicalServiceResponse create(MedicalServiceRequest request);

    MedicalServiceResponse update(
            Long id,
            MedicalServiceRequest request
    );


    void activate(Long id);
    void deactivate(Long id);
}