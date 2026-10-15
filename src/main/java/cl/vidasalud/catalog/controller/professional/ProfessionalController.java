package cl.vidasalud.catalog.controller.professional;

import cl.vidasalud.catalog.dto.MedicalServiceSummaryResponse;
import cl.vidasalud.catalog.dto.professional.ProfessionalRequest;
import cl.vidasalud.catalog.dto.professional.ProfessionalResponse;
import cl.vidasalud.catalog.service.professional.ProfessionalService;

import cl.vidasalud.catalog.service.professionalAvalaibility.ProfessionalAvailabilityService;
import cl.vidasalud.catalog.service.professionalMedicalService.ProfessionalAssignmentService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import cl.vidasalud.catalog.dto.professionalMedicalService.ProfessionalServiceResponse;
import cl.vidasalud.catalog.service.professionalMedicalService.ProfessionalAssignmentService;
import cl.vidasalud.catalog.dto.professionalAvailability.ProfessionalAvailabilityResponse;

@RestController
@RequestMapping("/api/catalog/professionals")
public class ProfessionalController {

    private final ProfessionalService professionalService;
    private final ProfessionalAssignmentService assignmentService;
    private final ProfessionalAvailabilityService availabilityService;

    public ProfessionalController(
            ProfessionalService professionalService,
            ProfessionalAssignmentService assignmentService,
            ProfessionalAvailabilityService availabilityService
    ) {
        this.professionalService = professionalService;
        this.assignmentService = assignmentService;
        this.availabilityService = availabilityService;
    }

    @GetMapping
    public ResponseEntity<List<ProfessionalResponse>> findAll(
            @RequestParam(required = false)
            String specialty
    ) {

        return ResponseEntity.ok(
                professionalService.findAll(specialty)
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfessionalResponse> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                professionalService.findById(id)
        );
    }

    @PostMapping
    public ResponseEntity<ProfessionalResponse> create(
            @Valid
            @RequestBody ProfessionalRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        professionalService.create(request)
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProfessionalResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody ProfessionalRequest request
    ) {

        return ResponseEntity.ok(
                professionalService.update(id, request)
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(
            @PathVariable Long id
    ) {

        professionalService.deactivate(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{professionalId}/services")
    public ResponseEntity<List<MedicalServiceSummaryResponse>>
    findServicesByProfessional(
            @PathVariable Long professionalId
    ) {

        return ResponseEntity.ok(
                assignmentService
                        .findServiceSummariesByProfessional(
                                professionalId
                        )
        );
    }

    @GetMapping("/{professionalId}/availability")
    public ResponseEntity<List<ProfessionalAvailabilityResponse>>
    findAvailabilityByProfessional(
            @PathVariable Long professionalId
    ) {

        return ResponseEntity.ok(
                availabilityService
                        .findByProfessional(professionalId)
        );
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(
            @PathVariable Long id
    ) {

        professionalService.activate(id);

        return ResponseEntity.noContent().build();
    }
}