package cl.vidasalud.catalog.dto.box;

public record BoxResponse(

        Long id,
        String code,
        String name,
        String location,
        String floor,
        Boolean active

) {
}