package cl.vidasalud.catalog.repository.professionalAvailability;

import cl.vidasalud.catalog.entity.professionalAvailability.ProfessionalAvailability;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfessionalAvailabilityRepository
        extends JpaRepository<ProfessionalAvailability, Long> {

    List<ProfessionalAvailability>
    findByProfessionalIdAndDayOfWeekAndActiveTrue(
            Long professionalId,
            Short dayOfWeek
    );

    List<ProfessionalAvailability>
    findByBoxIdAndDayOfWeekAndActiveTrue(
            Long boxId,
            Short dayOfWeek
    );

    List<ProfessionalAvailability>
    findByProfessionalIdAndActiveTrueOrderByDayOfWeekAscStartTimeAsc(
            Long professionalId
    );

    List<ProfessionalAvailability>
    findByProfessionalIdAndActiveTrue(Long professionalId);

    boolean existsByBoxIdAndActiveTrue(Long boxId);

}