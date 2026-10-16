package cl.vidasalud.catalog.service;

import cl.vidasalud.catalog.dto.medicalService.MedicalServiceRequest;
import cl.vidasalud.catalog.dto.medicalService.MedicalServiceResponse;
import cl.vidasalud.catalog.entity.medicalService.MedicalService;
import cl.vidasalud.catalog.entity.specialty.Specialty;
import cl.vidasalud.catalog.exception.DuplicateResourceException;
import cl.vidasalud.catalog.exception.ResourceNotFoundException;
import cl.vidasalud.catalog.repository.medicalService.MedicalServiceRepository;
import cl.vidasalud.catalog.repository.professionalMedicalService.ProfessionalMedicalServiceRepository;
import cl.vidasalud.catalog.repository.specialty.SpecialtyRepository;

import cl.vidasalud.catalog.service.medicalService.MedicalServiceServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MedicalServiceServiceImplTest {

    @Mock
    private MedicalServiceRepository medicalServiceRepository;

    @Mock
    private SpecialtyRepository specialtyRepository;

    @Mock
    private ProfessionalMedicalServiceRepository assignmentRepository;

    @InjectMocks
    private MedicalServiceServiceImpl medicalServiceService;

    private Specialty cardiology;
    private MedicalService medicalService;

    @BeforeEach
    void setUp() {

        cardiology = new Specialty();
        cardiology.setId(2L);
        cardiology.setCode("CARD");
        cardiology.setName("Cardiología");
        cardiology.setDescription("Especialidad de cardiología");
        cardiology.setActive(true);

        medicalService = new MedicalService();
        medicalService.setId(3L);
        medicalService.setCode("CARD-001");
        medicalService.setName("Consulta Cardiología");
        medicalService.setDescription("Consulta médica cardiológica");
        medicalService.setSpecialty(cardiology);
        medicalService.setDurationMinutes(30);
        medicalService.setPrice(new BigDecimal("45000"));
        medicalService.setActive(true);
    }

    // =====================================================
    // FIND ALL
    // =====================================================

    @Test
    void shouldReturnAllActiveMedicalServices() {

        when(medicalServiceRepository.findByActiveTrue())
                .thenReturn(List.of(medicalService));

        List<MedicalServiceResponse> result =
                medicalServiceService.findAll(null);

        assertNotNull(result);
        assertEquals(1, result.size());

        MedicalServiceResponse response = result.getFirst();

        assertEquals(3L, response.id());
        assertEquals("CARD-001", response.code());
        assertEquals("Consulta Cardiología", response.name());
        assertEquals("CARD", response.specialtyCode());
        assertEquals(new BigDecimal("45000"), response.price());

        verify(medicalServiceRepository, times(1))
                .findByActiveTrue();
    }

    @Test
    void shouldReturnMedicalServicesFilteredBySpecialty() {

        when(
                medicalServiceRepository
                        .findBySpecialtyCodeAndActiveTrue("CARD")
        )
                .thenReturn(List.of(medicalService));

        List<MedicalServiceResponse> result =
                medicalServiceService.findAll("CARD");

        assertEquals(1, result.size());
        assertEquals("CARD", result.getFirst().specialtyCode());

        verify(medicalServiceRepository, times(1))
                .findBySpecialtyCodeAndActiveTrue("CARD");

        verify(medicalServiceRepository, never())
                .findByActiveTrue();
    }

    // =====================================================
    // FIND BY ID
    // =====================================================

    @Test
    void shouldReturnMedicalServiceById() {

        when(medicalServiceRepository.findById(3L))
                .thenReturn(Optional.of(medicalService));

        MedicalServiceResponse response =
                medicalServiceService.findById(3L);

        assertNotNull(response);
        assertEquals(3L, response.id());
        assertEquals("CARD-001", response.code());
        assertEquals("Consulta Cardiología", response.name());

        verify(medicalServiceRepository, times(1))
                .findById(3L);
    }

    @Test
    void shouldThrowResourceNotFoundWhenMedicalServiceDoesNotExist() {

        when(medicalServiceRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> medicalServiceService.findById(999L)
                );

        assertTrue(
                exception.getMessage()
                        .contains("999")
        );

        verify(medicalServiceRepository, times(1))
                .findById(999L);
    }

    // =====================================================
    // CREATE
    // =====================================================

    @Test
    void shouldCreateMedicalServiceSuccessfully() {

        MedicalServiceRequest request =
                new MedicalServiceRequest(
                        "CARD-004",
                        "Ecocardiograma",
                        "Evaluación ecográfica del corazón",
                        2L,
                        30,
                        new BigDecimal("38000")
                );

        when(medicalServiceRepository.existsByCode("CARD-004"))
                .thenReturn(false);

        when(specialtyRepository.findById(2L))
                .thenReturn(Optional.of(cardiology));

        when(medicalServiceRepository.save(any(MedicalService.class)))
                .thenAnswer(invocation -> {

                    MedicalService saved =
                            invocation.getArgument(0);

                    saved.setId(12L);

                    return saved;
                });

        MedicalServiceResponse response =
                medicalServiceService.create(request);

        assertNotNull(response);
        assertEquals(12L, response.id());
        assertEquals("CARD-004", response.code());
        assertEquals("Ecocardiograma", response.name());
        assertEquals(2L, response.specialtyId());
        assertEquals("CARD", response.specialtyCode());
        assertEquals(30, response.durationMinutes());
        assertEquals(new BigDecimal("38000"), response.price());
        assertTrue(response.active());

        verify(medicalServiceRepository, times(1))
                .existsByCode("CARD-004");

        verify(specialtyRepository, times(1))
                .findById(2L);

        verify(medicalServiceRepository, times(1))
                .save(any(MedicalService.class));
    }

    @Test
    void shouldThrowDuplicateResourceWhenCodeAlreadyExists() {

        MedicalServiceRequest request =
                new MedicalServiceRequest(
                        "CARD-001",
                        "Duplicado",
                        "Prueba",
                        2L,
                        30,
                        new BigDecimal("30000")
                );

        when(medicalServiceRepository.existsByCode("CARD-001"))
                .thenReturn(true);

        DuplicateResourceException exception =
                assertThrows(
                        DuplicateResourceException.class,
                        () -> medicalServiceService.create(request)
                );

        assertTrue(
                exception.getMessage()
                        .contains("CARD-001")
        );

        verify(medicalServiceRepository, never())
                .save(any());

        verify(specialtyRepository, never())
                .findById(anyLong());
    }

    @Test
    void shouldThrowResourceNotFoundWhenCreatingWithUnknownSpecialty() {

        MedicalServiceRequest request =
                new MedicalServiceRequest(
                        "TEST-001",
                        "Prestación Test",
                        "Prueba",
                        999L,
                        30,
                        new BigDecimal("15000")
                );

        when(medicalServiceRepository.existsByCode("TEST-001"))
                .thenReturn(false);

        when(specialtyRepository.findById(999L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () -> medicalServiceService.create(request)
                );

        assertTrue(
                exception.getMessage()
                        .contains("999")
        );

        verify(medicalServiceRepository, never())
                .save(any());
    }

    // =====================================================
    // UPDATE
    // =====================================================

    @Test
    void shouldUpdateMedicalServiceSuccessfully() {

        MedicalServiceRequest request =
                new MedicalServiceRequest(
                        "CARD-001",
                        "Consulta Cardiología Actualizada",
                        "Descripción actualizada",
                        2L,
                        40,
                        new BigDecimal("50000")
                );

        when(medicalServiceRepository.findById(3L))
                .thenReturn(Optional.of(medicalService));

        when(
                medicalServiceRepository
                        .existsByCodeAndIdNot("CARD-001", 3L)
        )
                .thenReturn(false);

        when(specialtyRepository.findById(2L))
                .thenReturn(Optional.of(cardiology));

        when(medicalServiceRepository.save(any(MedicalService.class)))
                .thenAnswer(invocation ->
                        invocation.getArgument(0)
                );

        MedicalServiceResponse response =
                medicalServiceService.update(
                        3L,
                        request
                );

        assertEquals(
                "Consulta Cardiología Actualizada",
                response.name()
        );

        assertEquals(
                "Descripción actualizada",
                response.description()
        );

        assertEquals(
                40,
                response.durationMinutes()
        );

        assertEquals(
                new BigDecimal("50000"),
                response.price()
        );

        verify(medicalServiceRepository, times(1))
                .save(medicalService);
    }

    @Test
    void shouldThrowDuplicateResourceWhenUpdatingToExistingCode() {

        MedicalServiceRequest request =
                new MedicalServiceRequest(
                        "CARD-002",
                        "Consulta Test",
                        "Test",
                        2L,
                        30,
                        new BigDecimal("30000")
                );

        when(medicalServiceRepository.findById(3L))
                .thenReturn(Optional.of(medicalService));

        when(specialtyRepository.findById(2L))
                .thenReturn(Optional.of(cardiology));

        when(
                medicalServiceRepository
                        .existsByCodeAndIdNot("CARD-002", 3L)
        )
                .thenReturn(true);

        DuplicateResourceException exception =
                assertThrows(
                        DuplicateResourceException.class,
                        () -> medicalServiceService.update(
                                3L,
                                request
                        )
                );

        assertTrue(
                exception.getMessage()
                        .contains("CARD-002")
        );

        verify(medicalServiceRepository, never())
                .save(any());
    }

    // =====================================================
    // DEACTIVATE
    // =====================================================

    @Test
    void shouldDeactivateMedicalService() {

        when(medicalServiceRepository.findById(3L))
                .thenReturn(Optional.of(medicalService));

        medicalServiceService.deactivate(3L);

        assertFalse(medicalService.getActive());

        verify(medicalServiceRepository, times(1))
                .findById(3L);
    }

    @Test
    void shouldThrowResourceNotFoundWhenDeactivatingUnknownService() {

        when(medicalServiceRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                ResourceNotFoundException.class,
                () -> medicalServiceService.deactivate(999L)
        );
    }

    // =====================================================
    // ACTIVATE
    // =====================================================

    @Test
    void shouldActivateMedicalService() {

        medicalService.setActive(false);

        when(medicalServiceRepository.findById(3L))
                .thenReturn(Optional.of(medicalService));

        when(medicalServiceRepository.save(medicalService))
                .thenReturn(medicalService);

        medicalServiceService.activate(3L);

        assertTrue(medicalService.getActive());

        verify(medicalServiceRepository, times(1))
                .save(medicalService);
    }
}