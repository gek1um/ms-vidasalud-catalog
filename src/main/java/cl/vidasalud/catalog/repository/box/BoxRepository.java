package cl.vidasalud.catalog.repository.box;

import cl.vidasalud.catalog.entity.box.Box;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoxRepository
        extends JpaRepository<Box, Long> {

    Optional<Box> findByCode(String code);

    boolean existsByCode(String code);

    boolean existsByCodeAndIdNot(String code, Long id);

    List<Box> findByActiveTrue();
}