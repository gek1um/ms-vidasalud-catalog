package cl.vidasalud.catalog.service.professionalMedicalService;

import cl.vidasalud.catalog.dto.MedicalServiceSummaryResponse;
import cl.vidasalud.catalog.dto.ProfessionalSummaryResponse;
import cl.vidasalud.catalog.dto.professionalMedicalService.ProfessionalServiceRequest;
import cl.vidasalud.catalog.dto.professionalMedicalService.ProfessionalServiceResponse;
import cl.vidasalud.catalog.entity.medicalService.MedicalService;
import cl.vidasalud.catalog.entity.professional.Professional;
import cl.vidasalud.catalog.entity.professionalMedicalService.ProfessionalMedicalService;
import cl.vidasalud.catalog.exception.BusinessRuleException;
import cl.vidasalud.catalog.exception.DuplicateResourceException;
import cl.vidasalud.catalog.exception.ResourceNotFoundException;
import cl.vidasalud.catalog.repository.medicalService.MedicalServiceRepository;
import cl.vidasalud.catalog.repository.professionalMedicalService.ProfessionalMedicalServiceRepository;
import cl.vidasalud.catalog.repository.professional.ProfessionalRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProfessionalAssignmentServiceImpl
        implements ProfessionalAssignmentService {

    private final ProfessionalMedicalServiceRepository assignmentRepository;
    private final ProfessionalRepository professionalRepository;
    private final MedicalServiceRepository medicalServiceRepository;

    public ProfessionalAssignmentServiceImpl(
            ProfessionalMedicalServiceRepository assignmentRepository,
            ProfessionalRepository professionalRepository,
            MedicalServiceRepository medicalServiceRepository
    ) {
        this.assignmentRepository = assignmentRepository;
        this.professionalRepository = professionalRepository;
        this.medicalServiceRepository = medicalServiceRepository;
    }

    @Override
    @Transactional
    public ProfessionalServiceResponse assign(
            ProfessionalServiceRequest request
    ) {

        if (assignmentRepository
                .existsByProfessionalIdAndMedicalServiceId(
                        request.professionalId(),
                        request.medicalServiceId()
                )) {

            throw new DuplicateResourceException(
                    "Professional is already assigned to this medical service"
            );
        }

        Professional professional =
                professionalRepository
                        .findById(request.professionalId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Professional not found with id: "
                                                + request.professionalId()
                                )
                        );

        MedicalService medicalService =
                medicalServiceRepository
                        .findById(request.medicalServiceId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Medical service not found with id: "
                                                + request.medicalServiceId()
                                )
                        );

        if (!professional.getSpecialty().getId()
                .equals(medicalService.getSpecialty().getId())) {

            throw new BusinessRuleException(
                    "Professional and medical service must belong to the same specialty"
            );
        }

        ProfessionalMedicalService assignment =
                new ProfessionalMedicalService();

        assignment.setProfessional(professional);
        assignment.setMedicalService(medicalService);
        assignment.setActive(true);

        return toResponse(
                assignmentRepository.save(assignment)
        );
    }

    @Override
    @Transactional
    public void deactivate(Long id) {

        ProfessionalMedicalService assignment =
                assignmentRepository
                        .findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Professional service assignment not found with id: "
                                                + id
                                )
                        );

        assignment.setActive(false);

        assignmentRepository.save(assignment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalServiceResponse>
    findServicesByProfessional(Long professionalId) {

        if (!professionalRepository.existsById(professionalId)) {
            throw new ResourceNotFoundException(
                    "Professional not found with id: "
                            + professionalId
            );
        }

        return assignmentRepository
                .findByProfessionalIdAndActiveTrue(
                        professionalId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalServiceResponse>
    findProfessionalsByMedicalService(Long medicalServiceId) {

        if (!medicalServiceRepository.existsById(medicalServiceId)) {
            throw new ResourceNotFoundException(
                    "Medical service not found with id: "
                            + medicalServiceId
            );
        }

        return assignmentRepository
                .findByMedicalServiceIdAndActiveTrue(
                        medicalServiceId
                )
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalSummaryResponse>
    findProfessionalSummariesByMedicalService(
            Long medicalServiceId
    ) {

        if (!medicalServiceRepository.existsById(medicalServiceId)) {

            throw new ResourceNotFoundException(
                    "Medical service not found with id: "
                            + medicalServiceId
            );
        }

        return assignmentRepository
                .findByMedicalServiceIdAndActiveTrue(
                        medicalServiceId
                )
                .stream()
                .map(
                        ProfessionalMedicalService::getProfessional
                )
                .filter(Professional::getActive)
                .map(professional ->
                        new ProfessionalSummaryResponse(
                                professional.getId(),
                                professional.getRut(),
                                professional.getFirstName(),
                                professional.getLastName(),

                                professional.getSpecialty().getId(),
                                professional.getSpecialty().getCode(),
                                professional.getSpecialty().getName()
                        )
                )
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalServiceSummaryResponse>
    findServiceSummariesByProfessional(
            Long professionalId
    ) {

        if (!professionalRepository.existsById(professionalId)) {

            throw new ResourceNotFoundException(
                    "Professional not found with id: "
                            + professionalId
            );
        }

        return assignmentRepository
                .findByProfessionalIdAndActiveTrue(
                        professionalId
                )
                .stream()
                .map(
                        ProfessionalMedicalService::getMedicalService
                )
                .filter(MedicalService::getActive)
                .map(service ->
                        new MedicalServiceSummaryResponse(
                                service.getId(),
                                service.getCode(),
                                service.getName(),
                                service.getDurationMinutes(),
                                service.getPrice()
                        )
                )
                .toList();
    }

    @Override
    @Transactional
    public void activate(Long id) {

        ProfessionalMedicalService assignment =
                assignmentRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Professional service assignment not found with id: "
                                                + id
                                )
                        );

        Professional professional =
                assignment.getProfessional();

        MedicalService medicalService =
                assignment.getMedicalService();

        if (!professional.getActive()) {
            throw new BusinessRuleException(
                    "Cannot activate assignment because professional is inactive"
            );
        }

        if (!medicalService.getActive()) {
            throw new BusinessRuleException(
                    "Cannot activate assignment because medical service is inactive"
            );
        }

        assignment.setActive(true);

        assignmentRepository.save(assignment);
    }

    private ProfessionalServiceResponse toResponse(
            ProfessionalMedicalService assignment
    ) {

        Professional professional =
                assignment.getProfessional();

        MedicalService medicalService =
                assignment.getMedicalService();

        return new ProfessionalServiceResponse(
                assignment.getId(),

                professional.getId(),
                professional.getFirstName()
                        + " "
                        + professional.getLastName(),

                medicalService.getId(),
                medicalService.getCode(),
                medicalService.getName(),

                assignment.getActive()
        );
    }
}