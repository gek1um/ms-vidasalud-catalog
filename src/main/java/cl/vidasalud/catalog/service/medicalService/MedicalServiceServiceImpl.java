package cl.vidasalud.catalog.service.medicalService;

import cl.vidasalud.catalog.dto.medicalService.MedicalServiceRequest;
import cl.vidasalud.catalog.dto.medicalService.MedicalServiceResponse;
import cl.vidasalud.catalog.entity.medicalService.MedicalService;
import cl.vidasalud.catalog.entity.specialty.Specialty;
import cl.vidasalud.catalog.repository.medicalService.MedicalServiceRepository;
import cl.vidasalud.catalog.repository.specialty.SpecialtyRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cl.vidasalud.catalog.exception.DuplicateResourceException;
import cl.vidasalud.catalog.exception.ResourceNotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@Service
public class MedicalServiceServiceImpl
        implements MedicalServiceService {

    private final MedicalServiceRepository medicalServiceRepository;
    private final SpecialtyRepository specialtyRepository;

    public MedicalServiceServiceImpl(
            MedicalServiceRepository medicalServiceRepository,
            SpecialtyRepository specialtyRepository
    ) {
        this.medicalServiceRepository = medicalServiceRepository;
        this.specialtyRepository = specialtyRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<MedicalServiceResponse> findAll(
            String specialtyCode
    ) {

        List<MedicalService> services;

        if (specialtyCode != null && !specialtyCode.isBlank()) {

            services =
                    medicalServiceRepository
                            .findBySpecialtyCodeAndActiveTrue(
                                    specialtyCode
                            );

        } else {

            services =
                    medicalServiceRepository.findByActiveTrue();
        }

        return services
                .stream()
                .map(this::toResponse)
                .toList();
    }


    @Override
    @Transactional(readOnly = true)
    public MedicalServiceResponse findById(Long id) {

        MedicalService service =
                medicalServiceRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Medical service not found with id: "
                                                + id
                                )
                        );

        return toResponse(service);
    }


    @Override
    @Transactional
    public MedicalServiceResponse create(
            MedicalServiceRequest request
    ) {

        if (medicalServiceRepository.existsByCode(request.code())) {
            throw new DuplicateResourceException(
                    "Medical service code already exists: "
                            + request.code()
            );
        }

        Specialty specialty =
                specialtyRepository
                        .findById(request.specialtyId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Specialty not found with id: " + request.specialtyId()
                                )
                        );

        MedicalService service = new MedicalService();

        service.setCode(request.code());
        service.setName(request.name());
        service.setDescription(request.description());
        service.setSpecialty(specialty);
        service.setDurationMinutes(request.durationMinutes());
        service.setPrice(request.price());
        service.setActive(true);


        MedicalService saved =
                medicalServiceRepository.save(service);

        return toResponse(saved);
    }


    @Override
    @Transactional
    public MedicalServiceResponse update(
            Long id,
            MedicalServiceRequest request
    ) {

        MedicalService service =
                medicalServiceRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Medical service not found with id: " + id
                                )
                        );

        Specialty specialty =
                specialtyRepository
                        .findById(request.specialtyId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Specialty not found with id: " + request.specialtyId()
                                )
                        );

        service.setCode(request.code());
        service.setName(request.name());
        service.setDescription(request.description());
        service.setSpecialty(specialty);
        service.setDurationMinutes(request.durationMinutes());
        service.setPrice(request.price());


        if (medicalServiceRepository
                .existsByCodeAndIdNot(request.code(), id)) {

            throw new DuplicateResourceException(
                    "Medical service code already exists: "
                            + request.code()
            );
        }

        MedicalService updated =
                medicalServiceRepository.save(service);

        return toResponse(updated);
    }


    @Override
    @Transactional
    public void deactivate(Long id) {

        MedicalService service =
                medicalServiceRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Medical service not found with id: " + id
                                )
                        );

        service.setActive(false);

        medicalServiceRepository.save(service);
    }

    @Override
    @Transactional
    public void activate(Long id) {

        MedicalService service =
                medicalServiceRepository.findById(id)
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Medical service not found with id: " + id
                                )
                        );

        service.setActive(true);

        medicalServiceRepository.save(service);
    }


    private MedicalServiceResponse toResponse(
            MedicalService service
    ) {

        return new MedicalServiceResponse(
                service.getId(),
                service.getCode(),
                service.getName(),
                service.getDescription(),

                service.getSpecialty().getId(),
                service.getSpecialty().getCode(),
                service.getSpecialty().getName(),

                service.getDurationMinutes(),
                service.getPrice(),

                service.getActive()
        );
    }
}