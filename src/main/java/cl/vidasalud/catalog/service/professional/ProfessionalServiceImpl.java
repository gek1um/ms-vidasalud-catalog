package cl.vidasalud.catalog.service.professional;

import cl.vidasalud.catalog.dto.professional.ProfessionalRequest;
import cl.vidasalud.catalog.dto.professional.ProfessionalResponse;
import cl.vidasalud.catalog.entity.professional.Professional;
import cl.vidasalud.catalog.entity.specialty.Specialty;
import cl.vidasalud.catalog.exception.DuplicateResourceException;
import cl.vidasalud.catalog.exception.ResourceNotFoundException;
import cl.vidasalud.catalog.repository.professional.ProfessionalRepository;
import cl.vidasalud.catalog.repository.professionalAvailability.ProfessionalAvailabilityRepository;
import cl.vidasalud.catalog.repository.professionalMedicalService.ProfessionalMedicalServiceRepository;
import cl.vidasalud.catalog.repository.specialty.SpecialtyRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProfessionalServiceImpl
        implements ProfessionalService {

    private final ProfessionalRepository professionalRepository;
    private final SpecialtyRepository specialtyRepository;
    private final ProfessionalMedicalServiceRepository assignmentRepository;
    private final ProfessionalAvailabilityRepository availabilityRepository;

    public ProfessionalServiceImpl(
            ProfessionalRepository professionalRepository,
            SpecialtyRepository specialtyRepository,
            ProfessionalMedicalServiceRepository assignmentRepository,
            ProfessionalAvailabilityRepository availabilityRepository
    ) {
        this.professionalRepository = professionalRepository;
        this.specialtyRepository = specialtyRepository;
        this.assignmentRepository = assignmentRepository;
        this.availabilityRepository = availabilityRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalResponse> findAll(
            String specialtyCode
    ) {

        List<Professional> professionals;

        if (specialtyCode != null && !specialtyCode.isBlank()) {

            professionals =
                    professionalRepository
                            .findBySpecialtyCodeAndActiveTrue(
                                    specialtyCode
                            );

        } else {

            professionals =
                    professionalRepository.findByActiveTrue();
        }

        return professionals
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public ProfessionalResponse findById(Long id) {

        Professional professional =
                findEntity(id);

        return toResponse(professional);
    }

    @Override
    @Transactional
    public ProfessionalResponse create(
            ProfessionalRequest request
    ) {

        if (professionalRepository.existsByRut(request.rut())) {
            throw new DuplicateResourceException(
                    "Professional RUT already exists: "
                            + request.rut()
            );
        }

        if (request.email() != null
                && professionalRepository.existsByEmail(request.email())) {

            throw new DuplicateResourceException(
                    "Professional email already exists: "
                            + request.email()
            );
        }

        Specialty specialty =
                specialtyRepository
                        .findById(request.specialtyId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Specialty not found with id: "
                                                + request.specialtyId()
                                )
                        );

        Professional professional = new Professional();

        professional.setRut(request.rut());
        professional.setFirstName(request.firstName());
        professional.setLastName(request.lastName());
        professional.setSpecialty(specialty);
        professional.setEmail(request.email());
        professional.setPhone(request.phone());
        professional.setActive(true);

        return toResponse(
                professionalRepository.save(professional)
        );
    }

    @Override
    @Transactional
    public ProfessionalResponse update(
            Long id,
            ProfessionalRequest request
    ) {

        Professional professional =
                findEntity(id);

        if (professionalRepository
                .existsByRutAndIdNot(request.rut(), id)) {

            throw new DuplicateResourceException(
                    "Professional RUT already exists: "
                            + request.rut()
            );
        }

        if (request.email() != null
                && professionalRepository
                .existsByEmailAndIdNot(request.email(), id)) {

            throw new DuplicateResourceException(
                    "Professional email already exists: "
                            + request.email()
            );
        }

        Specialty specialty =
                specialtyRepository
                        .findById(request.specialtyId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Specialty not found with id: "
                                                + request.specialtyId()
                                )
                        );

        professional.setRut(request.rut());
        professional.setFirstName(request.firstName());
        professional.setLastName(request.lastName());
        professional.setSpecialty(specialty);
        professional.setEmail(request.email());
        professional.setPhone(request.phone());

        return toResponse(
                professionalRepository.save(professional)
        );
    }

    @Override
    @Transactional
    public void deactivate(Long id) {

        Professional professional =
                findEntity(id);

        professional.setActive(false);

        professionalRepository.save(professional);

        assignmentRepository
                .findByProfessionalIdAndActiveTrue(id)
                .forEach(assignment ->
                        assignment.setActive(false)
                );

        availabilityRepository
                .findByProfessionalIdAndActiveTrue(id)
                .forEach(availability ->
                        availability.setActive(false)
                );
    }

    @Override
    @Transactional
    public void activate(Long id) {

        Professional professional =
                findEntity(id);

        professional.setActive(true);

        professionalRepository.save(professional);
    }

    private Professional findEntity(Long id) {

        return professionalRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Professional not found with id: "
                                        + id
                        )
                );
    }

    private ProfessionalResponse toResponse(
            Professional professional
    ) {

        return new ProfessionalResponse(
                professional.getId(),
                professional.getRut(),
                professional.getFirstName(),
                professional.getLastName(),

                professional.getSpecialty().getId(),
                professional.getSpecialty().getCode(),
                professional.getSpecialty().getName(),

                professional.getEmail(),
                professional.getPhone(),
                professional.getActive()
        );
    }
}