package cl.vidasalud.catalog.controller.professionalAvailability;

import cl.vidasalud.catalog.dto.professionalAvailability.ProfessionalAvailabilityRequest;
import cl.vidasalud.catalog.dto.professionalAvailability.ProfessionalAvailabilityResponse;
import cl.vidasalud.catalog.service.professionalAvalaibility.ProfessionalAvailabilityService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/availability")
public class ProfessionalAvailabilityController {

    private final ProfessionalAvailabilityService availabilityService;

    public ProfessionalAvailabilityController(
            ProfessionalAvailabilityService availabilityService
    ) {
        this.availabilityService = availabilityService;
    }

    @GetMapping("/professional/{professionalId}")
    public ResponseEntity<List<ProfessionalAvailabilityResponse>>
    findByProfessional(
            @PathVariable Long professionalId
    ) {

        return ResponseEntity.ok(
                availabilityService
                        .findByProfessional(professionalId)
        );
    }

    @PostMapping
    public ResponseEntity<ProfessionalAvailabilityResponse> create(
            @Valid
            @RequestBody ProfessionalAvailabilityRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        availabilityService.create(request)
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalAvailabilityResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody ProfessionalAvailabilityRequest request
    ) {

        return ResponseEntity.ok(
                availabilityService.update(id, request)
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(
            @PathVariable Long id
    ) {

        availabilityService.deactivate(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(
            @PathVariable Long id
    ) {

        availabilityService.activate(id);

        return ResponseEntity.noContent().build();
    }
}