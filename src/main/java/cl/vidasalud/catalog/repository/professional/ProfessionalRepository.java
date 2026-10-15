package cl.vidasalud.catalog.repository.professional;

import cl.vidasalud.catalog.entity.professional.Professional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProfessionalRepository
        extends JpaRepository<Professional, Long> {

    Optional<Professional> findByRut(String rut);

    boolean existsByRut(String rut);

    boolean existsByRutAndIdNot(String rut, Long id);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    List<Professional> findByActiveTrue();

    List<Professional> findBySpecialtyCodeAndActiveTrue(String code);
}