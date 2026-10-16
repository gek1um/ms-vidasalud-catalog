package cl.vidasalud.catalog.service;

import cl.vidasalud.catalog.dto.professionalAvailability.ProfessionalAvailabilityRequest;
import cl.vidasalud.catalog.dto.professionalAvailability.ProfessionalAvailabilityResponse;
import cl.vidasalud.catalog.entity.box.Box;
import cl.vidasalud.catalog.entity.professional.Professional;
import cl.vidasalud.catalog.entity.professionalAvailability.ProfessionalAvailability;
import cl.vidasalud.catalog.entity.specialty.Specialty;
import cl.vidasalud.catalog.exception.BusinessRuleException;
import cl.vidasalud.catalog.exception.ResourceNotFoundException;
import cl.vidasalud.catalog.repository.box.BoxRepository;
import cl.vidasalud.catalog.repository.professionalAvailability.ProfessionalAvailabilityRepository;
import cl.vidasalud.catalog.repository.professional.ProfessionalRepository;

import cl.vidasalud.catalog.service.professionalAvalaibility.ProfessionalAvailabilityServiceImpl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProfessionalAvailabilityServiceImplTest {

    @Mock
    private ProfessionalAvailabilityRepository availabilityRepository;

    @Mock
    private ProfessionalRepository professionalRepository;

    @Mock
    private BoxRepository boxRepository;

    @InjectMocks
    private ProfessionalAvailabilityServiceImpl availabilityService;

    private Specialty cardiology;
    private Professional professional;
    private Box box;
    private ProfessionalAvailability availability;

    @BeforeEach
    void setUp() {

        // =====================================================
        // SPECIALTY
        // =====================================================

        cardiology = new Specialty();
        cardiology.setId(2L);
        cardiology.setCode("CARD");
        cardiology.setName("Cardiología");
        cardiology.setDescription("Especialidad de cardiología");
        cardiology.setActive(true);

        // =====================================================
        // PROFESSIONAL
        // =====================================================

        professional = new Professional();
        professional.setId(1L);
        professional.setRut("15.555.555-5");
        professional.setFirstName("María");
        professional.setLastName("González");
        professional.setSpecialty(cardiology);
        professional.setEmail("maria.gonzalez@vidasalud.cl");
        professional.setPhone("+56911111111");
        professional.setActive(true);

        // =====================================================
        // BOX
        // =====================================================

        box = new Box();
        box.setId(3L);
        box.setCode("BOX-CARD-01");
        box.setName("Box Cardiología 1");
        box.setLocation("Centro Médico");
        box.setFloor("Piso 2");
        box.setActive(true);

        // =====================================================
        // AVAILABILITY
        // =====================================================

        availability = new ProfessionalAvailability();
        availability.setId(1L);
        availability.setProfessional(professional);
        availability.setBox(box);
        availability.setDayOfWeek((short) 1);
        availability.setStartTime(LocalTime.of(8, 0));
        availability.setEndTime(LocalTime.of(12, 0));
        availability.setActive(true);
    }

    // =====================================================
    // FIND BY PROFESSIONAL
    // =====================================================

    @Test
    void shouldReturnAvailabilityByProfessional() {

        when(professionalRepository.existsById(1L))
                .thenReturn(true);

        when(
                availabilityRepository
                        .findByProfessionalIdAndActiveTrueOrderByDayOfWeekAscStartTimeAsc(1L)
        )
                .thenReturn(List.of(availability));

        List<ProfessionalAvailabilityResponse> result =
                availabilityService.findByProfessional(1L);

        assertNotNull(result);
        assertEquals(1, result.size());

        ProfessionalAvailabilityResponse response = result.getFirst();

        assertEquals(1L, response.id());
        assertEquals(1L, response.professionalId());
        assertEquals(3L, response.boxId());
        assertEquals((short) 1, response.dayOfWeek());
        assertEquals(LocalTime.of(8, 0), response.startTime());
        assertEquals(LocalTime.of(12, 0), response.endTime());

        verify(
                availabilityRepository,
                times(1)
        ).findByProfessionalIdAndActiveTrueOrderByDayOfWeekAscStartTimeAsc(1L);
    }

    @Test
    void shouldThrowResourceNotFoundWhenProfessionalDoesNotExist() {

        when(professionalRepository.existsById(999L))
                .thenReturn(false);

        assertThrows(
                ResourceNotFoundException.class,
                () -> availabilityService.findByProfessional(999L)
        );

        verify(availabilityRepository, never())
                .findByProfessionalIdAndActiveTrueOrderByDayOfWeekAscStartTimeAsc(any());
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Test
    void shouldCreateAvailabilitySuccessfully() {

        ProfessionalAvailabilityRequest request =
                new ProfessionalAvailabilityRequest(
                        1L,
                        3L,
                        (short) 2,
                        LocalTime.of(14, 0),
                        LocalTime.of(18, 0)
                );

        when(professionalRepository.findById(1L))
                .thenReturn(Optional.of(professional));

        when(boxRepository.findById(3L))
                .thenReturn(Optional.of(box));

        when(
                availabilityRepository
                        .findByProfessionalIdAndDayOfWeekAndActiveTrue(
                                1L,
                                (short) 2
                        )
        )
                .thenReturn(List.of());

        when(
                availabilityRepository
                        .findByBoxIdAndDayOfWeekAndActiveTrue(
                                3L,
                                (short) 2
                        )
        )
                .thenReturn(List.of());

        when(availabilityRepository.save(any(ProfessionalAvailability.class)))
                .thenAnswer(invocation -> {

                    ProfessionalAvailability saved =
                            invocation.getArgument(0);

                    saved.setId(10L);

                    return saved;
                });

        ProfessionalAvailabilityResponse response =
                availabilityService.create(request);

        assertNotNull(response);

        assertEquals(10L, response.id());
        assertEquals(1L, response.professionalId());
        assertEquals(3L, response.boxId());
        assertEquals((short) 2, response.dayOfWeek());
        assertEquals(LocalTime.of(14, 0), response.startTime());
        assertEquals(LocalTime.of(18, 0), response.endTime());
        assertTrue(response.active());

        verify(availabilityRepository, times(1))
                .save(any(ProfessionalAvailability.class));
    }

    // =====================================================
    // INVALID TIME RANGE
    // =====================================================

    @Test
    void shouldRejectAvailabilityWhenStartTimeEqualsEndTime() {

        ProfessionalAvailabilityRequest request =
                new ProfessionalAvailabilityRequest(
                        1L,
                        3L,
                        (short) 2,
                        LocalTime.of(10, 0),
                        LocalTime.of(10, 0)
                );

        assertThrows(
                BusinessRuleException.class,
                () -> availabilityService.create(request)
        );

        verify(availabilityRepository, never())
                .save(any());
    }

    @Test
    void shouldRejectAvailabilityWhenStartTimeIsAfterEndTime() {

        ProfessionalAvailabilityRequest request =
                new ProfessionalAvailabilityRequest(
                        1L,
                        3L,
                        (short) 2,
                        LocalTime.of(18, 0),
                        LocalTime.of(14, 0)
                );

        assertThrows(
                BusinessRuleException.class,
                () -> availabilityService.create(request)
        );

        verify(availabilityRepository, never())
                .save(any());
    }

    // =====================================================
    // INACTIVE PROFESSIONAL
    // =====================================================

    @Test
    void shouldRejectAvailabilityWhenProfessionalIsInactive() {

        professional.setActive(false);

        ProfessionalAvailabilityRequest request =
                new ProfessionalAvailabilityRequest(
                        1L,
                        3L,
                        (short) 2,
                        LocalTime.of(14, 0),
                        LocalTime.of(18, 0)
                );

        when(professionalRepository.findById(1L))
                .thenReturn(Optional.of(professional));

        assertThrows(
                BusinessRuleException.class,
                () -> availabilityService.create(request)
        );

        verify(availabilityRepository, never())
                .save(any());
    }

    // =====================================================
    // INACTIVE BOX
    // =====================================================

    @Test
    void shouldRejectAvailabilityWhenBoxIsInactive() {

        box.setActive(false);

        ProfessionalAvailabilityRequest request =
                new ProfessionalAvailabilityRequest(
                        1L,
                        3L,
                        (short) 2,
                        LocalTime.of(14, 0),
                        LocalTime.of(18, 0)
                );

        when(professionalRepository.findById(1L))
                .thenReturn(Optional.of(professional));

        when(boxRepository.findById(3L))
                .thenReturn(Optional.of(box));

        assertThrows(
                BusinessRuleException.class,
                () -> availabilityService.create(request)
        );

        verify(availabilityRepository, never())
                .save(any());
    }

    // =====================================================
    // PROFESSIONAL OVERLAP
    // =====================================================

    @Test
    void shouldRejectOverlappingProfessionalAvailability() {

        ProfessionalAvailabilityRequest request =
                new ProfessionalAvailabilityRequest(
                        1L,
                        3L,
                        (short) 1,
                        LocalTime.of(10, 0),
                        LocalTime.of(14, 0)
                );

        when(professionalRepository.findById(1L))
                .thenReturn(Optional.of(professional));

        when(boxRepository.findById(3L))
                .thenReturn(Optional.of(box));

        when(
                availabilityRepository
                        .findByProfessionalIdAndDayOfWeekAndActiveTrue(
                                1L,
                                (short) 1
                        )
        )
                .thenReturn(List.of(availability));

        assertThrows(
                BusinessRuleException.class,
                () -> availabilityService.create(request)
        );

        verify(availabilityRepository, never())
                .save(any());
    }

    // =====================================================
    // BOX OVERLAP
    // =====================================================

    @Test
    void shouldRejectOverlappingBoxAvailability() {

        Professional anotherProfessional = new Professional();

        anotherProfessional.setId(2L);
        anotherProfessional.setRut("16.666.666-6");
        anotherProfessional.setFirstName("Juan");
        anotherProfessional.setLastName("Pérez");
        anotherProfessional.setSpecialty(cardiology);
        anotherProfessional.setEmail("juan.perez@vidasalud.cl");
        anotherProfessional.setPhone("+56922222222");
        anotherProfessional.setActive(true);

        ProfessionalAvailability boxAvailability =
                new ProfessionalAvailability();

        boxAvailability.setId(20L);
        boxAvailability.setProfessional(anotherProfessional);
        boxAvailability.setBox(box);
        boxAvailability.setDayOfWeek((short) 2);
        boxAvailability.setStartTime(LocalTime.of(8, 0));
        boxAvailability.setEndTime(LocalTime.of(12, 0));
        boxAvailability.setActive(true);

        ProfessionalAvailabilityRequest request =
                new ProfessionalAvailabilityRequest(
                        1L,
                        3L,
                        (short) 2,
                        LocalTime.of(10, 0),
                        LocalTime.of(13, 0)
                );

        when(professionalRepository.findById(1L))
                .thenReturn(Optional.of(professional));

        when(boxRepository.findById(3L))
                .thenReturn(Optional.of(box));

        when(
                availabilityRepository
                        .findByProfessionalIdAndDayOfWeekAndActiveTrue(
                                1L,
                                (short) 2
                        )
        )
                .thenReturn(List.of());

        when(
                availabilityRepository
                        .findByBoxIdAndDayOfWeekAndActiveTrue(
                                3L,
                                (short) 2
                        )
        )
                .thenReturn(List.of(boxAvailability));

        assertThrows(
                BusinessRuleException.class,
                () -> availabilityService.create(request)
        );

        verify(availabilityRepository, never())
                .save(any());
    }

    // =====================================================
    // NON-OVERLAPPING AVAILABILITY
    // =====================================================

    @Test
    void shouldAllowAvailabilityImmediatelyAfterExistingSchedule() {

        ProfessionalAvailabilityRequest request =
                new ProfessionalAvailabilityRequest(
                        1L,
                        3L,
                        (short) 1,
                        LocalTime.of(12, 0),
                        LocalTime.of(14, 0)
                );

        when(professionalRepository.findById(1L))
                .thenReturn(Optional.of(professional));

        when(boxRepository.findById(3L))
                .thenReturn(Optional.of(box));

        when(
                availabilityRepository
                        .findByProfessionalIdAndDayOfWeekAndActiveTrue(
                                1L,
                                (short) 1
                        )
        )
                .thenReturn(List.of(availability));

        when(
                availabilityRepository
                        .findByBoxIdAndDayOfWeekAndActiveTrue(
                                3L,
                                (short) 1
                        )
        )
                .thenReturn(List.of(availability));

        when(availabilityRepository.save(any(ProfessionalAvailability.class)))
                .thenAnswer(invocation -> {

                    ProfessionalAvailability saved =
                            invocation.getArgument(0);

                    saved.setId(11L);

                    return saved;
                });

        ProfessionalAvailabilityResponse response =
                availabilityService.create(request);

        assertNotNull(response);
        assertEquals(LocalTime.of(12, 0), response.startTime());
        assertEquals(LocalTime.of(14, 0), response.endTime());

        verify(availabilityRepository, times(1))
                .save(any(ProfessionalAvailability.class));
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Test
    void shouldUpdateAvailabilitySuccessfully() {

        ProfessionalAvailabilityRequest request =
                new ProfessionalAvailabilityRequest(
                        1L,
                        3L,
                        (short) 3,
                        LocalTime.of(9, 0),
                        LocalTime.of(13, 0)
                );

        when(availabilityRepository.findById(1L))
                .thenReturn(Optional.of(availability));

        when(professionalRepository.findById(1L))
                .thenReturn(Optional.of(professional));

        when(boxRepository.findById(3L))
                .thenReturn(Optional.of(box));

        when(
                availabilityRepository
                        .findByProfessionalIdAndDayOfWeekAndActiveTrue(
                                1L,
                                (short) 3
                        )
        )
                .thenReturn(List.of());

        when(
                availabilityRepository
                        .findByBoxIdAndDayOfWeekAndActiveTrue(
                                3L,
                                (short) 3
                        )
        )
                .thenReturn(List.of());

        when(availabilityRepository.save(any(ProfessionalAvailability.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        ProfessionalAvailabilityResponse response =
                availabilityService.update(
                        1L,
                        request
                );

        assertNotNull(response);

        assertEquals((short) 3, response.dayOfWeek());
        assertEquals(LocalTime.of(9, 0), response.startTime());
        assertEquals(LocalTime.of(13, 0), response.endTime());

        verify(availabilityRepository, times(1))
                .save(availability);
    }

    // =====================================================
    // NOT FOUND
    // =====================================================

    @Test
    void shouldThrowResourceNotFoundWhenUpdatingUnknownAvailability() {

        when(availabilityRepository.findById(999L))
                .thenReturn(Optional.empty());

        ProfessionalAvailabilityRequest request =
                new ProfessionalAvailabilityRequest(
                        1L,
                        3L,
                        (short) 1,
                        LocalTime.of(8, 0),
                        LocalTime.of(12, 0)
                );

        assertThrows(
                ResourceNotFoundException.class,
                () -> availabilityService.update(
                        999L,
                        request
                )
        );

        verify(availabilityRepository, never())
                .save(any());
    }

    // =====================================================
    // DEACTIVATE
    // =====================================================

    @Test
    void shouldDeactivateAvailability() {

        when(availabilityRepository.findById(1L))
                .thenReturn(Optional.of(availability));

        availabilityService.deactivate(1L);

        assertFalse(availability.getActive());

        verify(availabilityRepository, times(1))
                .findById(1L);
    }

    @Test
    void shouldThrowResourceNotFoundWhenDeactivatingUnknownAvailability() {

        when(availabilityRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> availabilityService.deactivate(999L)
        );
    }

    // =====================================================
    // ACTIVATE
    // =====================================================

    @Test
    void shouldActivateAvailabilitySuccessfully() {

        availability.setActive(false);

        when(availabilityRepository.findById(1L))
                .thenReturn(Optional.of(availability));

        when(
                availabilityRepository
                        .findByProfessionalIdAndDayOfWeekAndActiveTrue(
                                1L,
                                (short) 1
                        )
        )
                .thenReturn(List.of());

        when(
                availabilityRepository
                        .findByBoxIdAndDayOfWeekAndActiveTrue(
                                3L,
                                (short) 1
                        )
        )
                .thenReturn(List.of());

        when(availabilityRepository.save(availability))
                .thenReturn(availability);

        availabilityService.activate(1L);

        assertTrue(availability.getActive());

        verify(availabilityRepository, times(1))
                .save(availability);
    }

    @Test
    void shouldRejectActivationWhenProfessionalIsInactive() {

        availability.setActive(false);
        professional.setActive(false);

        when(availabilityRepository.findById(1L))
                .thenReturn(Optional.of(availability));

        assertThrows(
                BusinessRuleException.class,
                () -> availabilityService.activate(1L)
        );

        verify(availabilityRepository, never())
                .save(any());
    }

    @Test
    void shouldRejectActivationWhenBoxIsInactive() {

        availability.setActive(false);
        box.setActive(false);

        when(availabilityRepository.findById(1L))
                .thenReturn(Optional.of(availability));

        assertThrows(
                BusinessRuleException.class,
                () -> availabilityService.activate(1L)
        );

        verify(availabilityRepository, never())
                .save(any());
    }

    @Test
    void shouldRejectActivationWhenProfessionalScheduleOverlaps() {

        availability.setActive(false);

        ProfessionalAvailability existing =
                new ProfessionalAvailability();

        existing.setId(50L);
        existing.setProfessional(professional);
        existing.setBox(box);
        existing.setDayOfWeek((short) 1);
        existing.setStartTime(LocalTime.of(10, 0));
        existing.setEndTime(LocalTime.of(14, 0));
        existing.setActive(true);

        when(availabilityRepository.findById(1L))
                .thenReturn(Optional.of(availability));

        when(
                availabilityRepository
                        .findByProfessionalIdAndDayOfWeekAndActiveTrue(
                                1L,
                                (short) 1
                        )
        )
                .thenReturn(List.of(existing));

        assertThrows(
                BusinessRuleException.class,
                () -> availabilityService.activate(1L)
        );

        verify(availabilityRepository, never())
                .save(any());
    }
}