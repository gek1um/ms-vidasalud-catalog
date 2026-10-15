package cl.vidasalud.catalog.repository.specialty;

import cl.vidasalud.catalog.entity.specialty.Specialty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SpecialtyRepository extends JpaRepository<Specialty, Long> {

    Optional<Specialty> findByCode(String code);

    List<Specialty> findByActiveTrue();
}