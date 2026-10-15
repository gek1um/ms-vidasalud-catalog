package cl.vidasalud.catalog.repository.professionalMedicalService;

import cl.vidasalud.catalog.entity.professionalMedicalService.ProfessionalMedicalService;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfessionalMedicalServiceRepository
        extends JpaRepository<ProfessionalMedicalService, Long> {

    boolean existsByProfessionalIdAndMedicalServiceId(
            Long professionalId,
            Long medicalServiceId
    );

    Optional<ProfessionalMedicalService>
    findByProfessionalIdAndMedicalServiceId(
            Long professionalId,
            Long medicalServiceId
    );

    List<ProfessionalMedicalService>
    findByProfessionalIdAndActiveTrue(
            Long professionalId
    );

    List<ProfessionalMedicalService>
    findByMedicalServiceIdAndActiveTrue(
            Long medicalServiceId
    );
}