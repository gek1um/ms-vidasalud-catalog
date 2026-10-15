package cl.vidasalud.catalog.service.professionalAvalaibility;

import cl.vidasalud.catalog.dto.professionalAvailability.ProfessionalAvailabilityRequest;
import cl.vidasalud.catalog.dto.professionalAvailability.ProfessionalAvailabilityResponse;
import cl.vidasalud.catalog.entity.box.Box;
import cl.vidasalud.catalog.entity.professional.Professional;
import cl.vidasalud.catalog.entity.professionalAvailability.ProfessionalAvailability;
import cl.vidasalud.catalog.exception.BusinessRuleException;
import cl.vidasalud.catalog.exception.ResourceNotFoundException;
import cl.vidasalud.catalog.repository.box.BoxRepository;
import cl.vidasalud.catalog.repository.professionalAvailability.ProfessionalAvailabilityRepository;
import cl.vidasalud.catalog.repository.professional.ProfessionalRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProfessionalAvailabilityServiceImpl
        implements ProfessionalAvailabilityService {

    private final ProfessionalAvailabilityRepository availabilityRepository;
    private final ProfessionalRepository professionalRepository;
    private final BoxRepository boxRepository;

    public ProfessionalAvailabilityServiceImpl(
            ProfessionalAvailabilityRepository availabilityRepository,
            ProfessionalRepository professionalRepository,
            BoxRepository boxRepository
    ) {
        this.availabilityRepository = availabilityRepository;
        this.professionalRepository = professionalRepository;
        this.boxRepository = boxRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProfessionalAvailabilityResponse>
    findByProfessional(Long professionalId) {

        if (!professionalRepository.existsById(professionalId)) {
            throw new ResourceNotFoundException(
                    "Professional not found with id: "
                            + professionalId
            );
        }

        return availabilityRepository
                .findByProfessionalIdAndActiveTrueOrderByDayOfWeekAscStartTimeAsc(professionalId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public ProfessionalAvailabilityResponse create(
            ProfessionalAvailabilityRequest request
    ) {

        validateTimeRange(
                request.startTime(),
                request.endTime()
        );

        Professional professional =
                professionalRepository
                        .findById(request.professionalId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Professional not found with id: "
                                                + request.professionalId()
                                )
                        );

        Box box =
                boxRepository
                        .findById(request.boxId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Box not found with id: "
                                                + request.boxId()
                                )
                        );


        if (!professional.getActive()) {
            throw new BusinessRuleException(
                    "Cannot assign availability to an inactive professional"
            );
        }

        if (!box.getActive()) {
            throw new BusinessRuleException(
                    "Cannot assign availability to an inactive box"
            );
        }

        validateOverlaps(
                null,
                request.professionalId(),
                request.boxId(),
                request.dayOfWeek(),
                request.startTime(),
                request.endTime()
        );

        ProfessionalAvailability availability =
                new ProfessionalAvailability();

        availability.setProfessional(professional);
        availability.setBox(box);
        availability.setDayOfWeek(request.dayOfWeek());
        availability.setStartTime(request.startTime());
        availability.setEndTime(request.endTime());
        availability.setActive(true);

        return toResponse(
                availabilityRepository.save(availability)
        );
    }

    @Override
    @Transactional
    public ProfessionalAvailabilityResponse update(
            Long id,
            ProfessionalAvailabilityRequest request
    ) {

        ProfessionalAvailability availability =
                findEntity(id);

        validateTimeRange(
                request.startTime(),
                request.endTime()
        );

        Professional professional =
                professionalRepository
                        .findById(request.professionalId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Professional not found with id: "
                                                + request.professionalId()
                                )
                        );

        Box box =
                boxRepository
                        .findById(request.boxId())
                        .orElseThrow(() ->
                                new ResourceNotFoundException(
                                        "Box not found with id: "
                                                + request.boxId()
                                )
                        );


        if (!professional.getActive()) {
            throw new BusinessRuleException(
                    "Cannot assign availability to an inactive professional"
            );
        }

        if (!box.getActive()) {
            throw new BusinessRuleException(
                    "Cannot assign availability to an inactive box"
            );
        }

        validateOverlaps(
                id,
                request.professionalId(),
                request.boxId(),
                request.dayOfWeek(),
                request.startTime(),
                request.endTime()
        );

        availability.setProfessional(professional);
        availability.setBox(box);
        availability.setDayOfWeek(request.dayOfWeek());
        availability.setStartTime(request.startTime());
        availability.setEndTime(request.endTime());

        return toResponse(
                availabilityRepository.save(availability)
        );
    }

    @Override
    @Transactional
    public void deactivate(Long id) {

        ProfessionalAvailability availability =
                findEntity(id);

        availability.setActive(false);

        availabilityRepository.save(availability);
    }

    private ProfessionalAvailability findEntity(Long id) {

        return availabilityRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Availability not found with id: "
                                        + id
                        )
                );
    }

    @Override
    @Transactional
    public void activate(Long id) {

        ProfessionalAvailability availability =
                findEntity(id);

        Professional professional =
                availability.getProfessional();

        Box box =
                availability.getBox();

        if (!professional.getActive()) {
            throw new BusinessRuleException(
                    "Cannot activate availability because professional is inactive"
            );
        }

        if (box != null && !box.getActive()) {
            throw new BusinessRuleException(
                    "Cannot activate availability because box is inactive"
            );
        }

        validateOverlaps(
                availability.getId(),
                professional.getId(),
                box.getId(),
                availability.getDayOfWeek(),
                availability.getStartTime(),
                availability.getEndTime()
        );

        availability.setActive(true);

        availabilityRepository.save(availability);
    }

    private void validateTimeRange(
            java.time.LocalTime startTime,
            java.time.LocalTime endTime
    ) {

        if (!startTime.isBefore(endTime)) {
            throw new BusinessRuleException(
                    "Start time must be before end time"
            );
        }
    }

    private void validateOverlaps(
            Long currentAvailabilityId,
            Long professionalId,
            Long boxId,
            Short dayOfWeek,
            java.time.LocalTime startTime,
            java.time.LocalTime endTime
    ) {

        List<ProfessionalAvailability> professionalSchedules =
                availabilityRepository
                        .findByProfessionalIdAndDayOfWeekAndActiveTrue(
                                professionalId,
                                dayOfWeek
                        );

        boolean professionalOverlap =
                professionalSchedules.stream()
                        .filter(a ->
                                currentAvailabilityId == null
                                        || !a.getId().equals(
                                        currentAvailabilityId
                                )
                        )
                        .anyMatch(a ->
                                overlaps(
                                        startTime,
                                        endTime,
                                        a.getStartTime(),
                                        a.getEndTime()
                                )
                        );

        if (professionalOverlap) {
            throw new BusinessRuleException(
                    "Professional already has an overlapping schedule"
            );
        }

        List<ProfessionalAvailability> boxSchedules =
                availabilityRepository
                        .findByBoxIdAndDayOfWeekAndActiveTrue(
                                boxId,
                                dayOfWeek
                        );

        boolean boxOverlap =
                boxSchedules.stream()
                        .filter(a ->
                                currentAvailabilityId == null
                                        || !a.getId().equals(
                                        currentAvailabilityId
                                )
                        )
                        .anyMatch(a ->
                                overlaps(
                                        startTime,
                                        endTime,
                                        a.getStartTime(),
                                        a.getEndTime()
                                )
                        );

        if (boxOverlap) {
            throw new BusinessRuleException(
                    "Box already has an overlapping schedule"
            );
        }
    }

    private boolean overlaps(
            java.time.LocalTime newStart,
            java.time.LocalTime newEnd,
            java.time.LocalTime existingStart,
            java.time.LocalTime existingEnd
    ) {

        return newStart.isBefore(existingEnd)
                && newEnd.isAfter(existingStart);
    }

    private ProfessionalAvailabilityResponse toResponse(
            ProfessionalAvailability availability
    ) {

        Professional professional =
                availability.getProfessional();

        Box box =
                availability.getBox();

        return new ProfessionalAvailabilityResponse(
                availability.getId(),

                professional.getId(),
                professional.getFirstName()
                        + " "
                        + professional.getLastName(),

                box.getId(),
                box.getCode(),
                box.getName(),

                availability.getDayOfWeek(),

                availability.getStartTime(),
                availability.getEndTime(),

                availability.getActive()
        );
    }
}