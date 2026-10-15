package cl.vidasalud.catalog.repository.medicalService;

import cl.vidasalud.catalog.entity.medicalService.MedicalService;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MedicalServiceRepository
        extends JpaRepository<MedicalService, Long> {

    Optional<MedicalService> findByCode(String code);

    boolean existsByCode(String code);

    List<MedicalService> findByActiveTrue();

    List<MedicalService> findBySpecialtyCodeAndActiveTrue(String code);

    boolean existsByCodeAndIdNot(String code, Long id);
}