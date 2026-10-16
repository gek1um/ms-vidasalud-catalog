package cl.vidasalud.catalog.controller.box;

import cl.vidasalud.catalog.dto.box.BoxRequest;
import cl.vidasalud.catalog.dto.box.BoxResponse;
import cl.vidasalud.catalog.service.box.BoxService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/catalog/boxes")
public class BoxController {

    private final BoxService boxService;

    public BoxController(BoxService boxService) {
        this.boxService = boxService;
    }

    @GetMapping
    public ResponseEntity<List<BoxResponse>> findAll() {

        return ResponseEntity.ok(
                boxService.findAll()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<BoxResponse> findById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                boxService.findById(id)
        );
    }

    @PostMapping
    public ResponseEntity<BoxResponse> create(
            @Valid
            @RequestBody BoxRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        boxService.create(request)
                );
    }

    @PutMapping("/{id}")
    public ResponseEntity<BoxResponse> update(
            @PathVariable Long id,
            @Valid
            @RequestBody BoxRequest request
    ) {

        return ResponseEntity.ok(
                boxService.update(id, request)
        );
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<Void> deactivate(
            @PathVariable Long id
    ) {

        boxService.deactivate(id);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/activate")
    public ResponseEntity<Void> activate(
            @PathVariable Long id
    ) {

        boxService.activate(id);

        return ResponseEntity.noContent().build();
    }
}