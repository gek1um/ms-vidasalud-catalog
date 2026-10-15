package cl.vidasalud.catalog.service.box;

import cl.vidasalud.catalog.dto.box.BoxRequest;
import cl.vidasalud.catalog.dto.box.BoxResponse;

import java.util.List;

public interface BoxService {

    List<BoxResponse> findAll();

    BoxResponse findById(Long id);

    BoxResponse create(BoxRequest request);

    BoxResponse update(
            Long id,
            BoxRequest request
    );

    void activate(Long id);
    void deactivate(Long id);
}