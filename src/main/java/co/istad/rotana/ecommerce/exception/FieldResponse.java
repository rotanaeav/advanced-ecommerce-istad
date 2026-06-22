package co.istad.rotana.ecommerce.exception;

public record FieldResponse(
        String field,
        String reason
) {
}
