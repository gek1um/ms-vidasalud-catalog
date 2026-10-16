package cl.vidasalud.catalog.service.specialty;

import cl.vidasalud.catalog.dto.specialty.SpecialtyResponse;
import cl.vidasalud.catalog.entity.specialty.Specialty;
import cl.vidasalud.catalog.repository.specialty.SpecialtyRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpecialtyServiceImpl implements SpecialtyService {

    private final SpecialtyRepository specialtyRepository;

    public SpecialtyServiceImpl(SpecialtyRepository specialtyRepository) {
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    public List<SpecialtyResponse> findAll() {

        return specialtyRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public SpecialtyResponse findById(Long id) {

        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Specialty not found with id: " + id)
                );

        return toResponse(specialty);
    }

    private SpecialtyResponse toResponse(Specialty specialty) {

        return new SpecialtyResponse(
                specialty.getId(),
                specialty.getCode(),
                specialty.getName(),
                specialty.getDescription(),
                specialty.getActive()
        );
    }
}