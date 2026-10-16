package cl.vidasalud.catalog.entity.professionalMedicalService;

import cl.vidasalud.catalog.entity.BaseEntity;
import cl.vidasalud.catalog.entity.medicalService.MedicalService;
import cl.vidasalud.catalog.entity.professional.Professional;
import jakarta.persistence.*;

@Entity
@Table(
        name = "professional_services",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uq_professional_services_relation",
                        columnNames = {
                                "professional_id",
                                "medical_service_id"
                        }
                )
        }
)
public class ProfessionalMedicalService extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "professional_id",
            nullable = false
    )
    private Professional professional;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "medical_service_id",
            nullable = false
    )
    private MedicalService medicalService;

    @Column(nullable = false)
    private Boolean active = true;

    public ProfessionalMedicalService() {
    }

    public Long getId() {
        return id;
    }

    public Professional getProfessional() {
        return professional;
    }

    public MedicalService getMedicalService() {
        return medicalService;
    }

    public Boolean getActive() {
        return active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setProfessional(
            Professional professional
    ) {
        this.professional = professional;
    }

    public void setMedicalService(
            MedicalService medicalService
    ) {
        this.medicalService = medicalService;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }
}