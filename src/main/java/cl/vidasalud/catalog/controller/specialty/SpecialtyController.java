package cl.vidasalud.catalog.controller.specialty;

import cl.vidasalud.catalog.dto.specialty.SpecialtyResponse;
import cl.vidasalud.catalog.service.specialty.SpecialtyService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/specialties")
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    public SpecialtyController(SpecialtyService specialtyService) {
        this.specialtyService = specialtyService;
    }

    @GetMapping
    public ResponseEntity<List<SpecialtyResponse>> findAll() {

        return ResponseEntity.ok(
                specialtyService.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyResponse> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                specialtyService.findById(id)
        );
    }
}