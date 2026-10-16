package cl.vidasalud.catalog.controller.medicalService;

import cl.vidasalud.catalog.dto.ProfessionalSummaryResponse;
import cl.vidasalud.catalog.dto.medicalService.MedicalServiceRequest;
import cl.vidasalud.catalog.dto.medicalService.MedicalServiceResponse;
import cl.vidasalud.catalog.service.medicalService.MedicalServiceService;
import cl.vidasalud.catalog.dto.professionalMedicalService.ProfessionalServiceResponse;
import cl.vidasalud.catalog.service.professionalMedicalService.ProfessionalAssignmentService;

import cl.vidasalud.catalog.service.professionalMedicalService.ProfessionalAssignmentService;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/services")
public class MedicalServiceController {

    private final MedicalServiceService medicalServiceService;
    private final ProfessionalAssignmentService assignmentService;

    public MedicalServiceController(
            MedicalServiceService medicalServiceService,
            ProfessionalAssignmentService assignmentService
    ) {
        this.medicalServiceService = medicalServiceService;
        this.assignmentService = assignmentService;
    }


    @GetMapping
    public ResponseEntity<List<MedicalServiceResponse>> findAll(
            @RequestParam(required = false)
            String specialty
    ) {

        return ResponseEntity.ok(
                medicalServiceService.findAll(specialty)
        );
    }


    @GetMapping("/{id}")
    public ResponseEntity<MedicalServiceResponse> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                medicalServiceService.findById(id)
        );
    }


    @PostMapping
    public ResponseEntity<MedicalServiceResponse> create(
            @Valid
            @RequestBody MedicalServiceRequest request
    ) {

        MedicalServiceResponse response =
                medicalServiceService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }


    @PutMapping("/{id}")
    public ResponseEntity<MedicalServiceResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody MedicalServiceRequest request
    ) {

        return ResponseEntity.ok(
                medicalServiceService.update(id, request)
        );
    }


    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(
            @PathVariable Long id
    ) {

        medicalServiceService.deactivate(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{serviceId}/professionals")
    public ResponseEntity<List<ProfessionalSummaryResponse>>
    findProfessionalsByService(
            @PathVariable Long serviceId
    ) {

        return ResponseEntity.ok(
                assignmentService
                        .findProfessionalSummariesByMedicalService(
                                serviceId
                        )
        );
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(
            @PathVariable Long id
    ) {

        medicalServiceService.activate(id);

        return ResponseEntity.noContent().build();
    }
}