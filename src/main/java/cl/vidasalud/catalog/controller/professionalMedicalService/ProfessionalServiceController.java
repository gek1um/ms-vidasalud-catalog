package cl.vidasalud.catalog.controller.professionalMedicalService;

import cl.vidasalud.catalog.dto.professionalMedicalService.ProfessionalServiceRequest;
import cl.vidasalud.catalog.dto.professionalMedicalService.ProfessionalServiceResponse;
import cl.vidasalud.catalog.service.professionalMedicalService.ProfessionalAssignmentService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/professional-services")
public class ProfessionalServiceController {

    private final ProfessionalAssignmentService assignmentService;

    public ProfessionalServiceController(
            ProfessionalAssignmentService assignmentService
    ) {
        this.assignmentService = assignmentService;
    }

    @PostMapping
    public ResponseEntity<ProfessionalServiceResponse> assign(
            @Valid
            @RequestBody ProfessionalServiceRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        assignmentService.assign(request)
                );
    }

    @GetMapping("/professional/{professionalId}")
    public ResponseEntity<List<ProfessionalServiceResponse>>
    findServicesByProfessional(
            @PathVariable Long professionalId
    ) {

        return ResponseEntity.ok(
                assignmentService
                        .findServicesByProfessional(
                                professionalId
                        )
        );
    }

    @GetMapping("/service/{medicalServiceId}")
    public ResponseEntity<List<ProfessionalServiceResponse>>
    findProfessionalsByMedicalService(
            @PathVariable Long medicalServiceId
    ) {

        return ResponseEntity.ok(
                assignmentService
                        .findProfessionalsByMedicalService(
                                medicalServiceId
                        )
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(
            @PathVariable Long id
    ) {

        assignmentService.deactivate(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(
            @PathVariable Long id
    ) {

        assignmentService.activate(id);

        return ResponseEntity.noContent().build();
    }
}