package cl.vidasalud.catalog.service.specialty;

import cl.vidasalud.catalog.dto.specialty.SpecialtyResponse;

import java.util.List;

public interface SpecialtyService {

    List<SpecialtyResponse> findAll();

    SpecialtyResponse findById(Long id);
}