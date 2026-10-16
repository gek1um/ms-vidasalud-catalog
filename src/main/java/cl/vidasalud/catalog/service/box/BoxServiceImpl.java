package cl.vidasalud.catalog.service.box;

import cl.vidasalud.catalog.dto.box.BoxRequest;
import cl.vidasalud.catalog.dto.box.BoxResponse;
import cl.vidasalud.catalog.entity.box.Box;
import cl.vidasalud.catalog.exception.BusinessRuleException;
import cl.vidasalud.catalog.exception.DuplicateResourceException;
import cl.vidasalud.catalog.exception.ResourceNotFoundException;
import cl.vidasalud.catalog.repository.box.BoxRepository;

import cl.vidasalud.catalog.repository.professionalAvailability.ProfessionalAvailabilityRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class BoxServiceImpl implements BoxService {

    private final BoxRepository boxRepository;
    private final ProfessionalAvailabilityRepository availabilityRepository;

    public BoxServiceImpl(BoxRepository boxRepository, ProfessionalAvailabilityRepository availabilityRepository) {
        this.boxRepository = boxRepository;
        this.availabilityRepository = availabilityRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public List<BoxResponse> findAll() {

        return boxRepository.findByActiveTrue()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public BoxResponse findById(Long id) {

        return toResponse(findEntity(id));
    }

    @Override
    @Transactional
    public BoxResponse create(BoxRequest request) {

        if (boxRepository.existsByCode(request.code())) {

            throw new DuplicateResourceException(
                    "Box code already exists: "
                            + request.code()
            );
        }

        Box box = new Box();

        box.setCode(request.code());
        box.setName(request.name());
        box.setLocation(request.location());
        box.setFloor(request.floor());
        box.setActive(true);

        return toResponse(
                boxRepository.save(box)
        );
    }

    @Override
    @Transactional
    public BoxResponse update(
            Long id,
            BoxRequest request
    ) {

        Box box = findEntity(id);

        if (boxRepository
                .existsByCodeAndIdNot(request.code(), id)) {

            throw new DuplicateResourceException(
                    "Box code already exists: "
                            + request.code()
            );
        }

        box.setCode(request.code());
        box.setName(request.name());
        box.setLocation(request.location());
        box.setFloor(request.floor());

        return toResponse(
                boxRepository.save(box)
        );
    }

    @Override
    @Transactional
    public void deactivate(Long id) {

        Box box = findEntity(id);

        if (availabilityRepository.existsByBoxIdAndActiveTrue(id)) {
            throw new BusinessRuleException(
                    "Cannot deactivate box because it has active availabilities"
            );
        }

        box.setActive(false);

        boxRepository.save(box);
    }

    @Override
    @Transactional
    public void activate(Long id) {

        Box box = findEntity(id);

        box.setActive(true);

        boxRepository.save(box);
    }

    private Box findEntity(Long id) {

        return boxRepository
                .findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Box not found with id: " + id
                        )
                );
    }

    private BoxResponse toResponse(Box box) {

        return new BoxResponse(
                box.getId(),
                box.getCode(),
                box.getName(),
                box.getLocation(),
                box.getFloor(),
                box.getActive()
        );
    }
}