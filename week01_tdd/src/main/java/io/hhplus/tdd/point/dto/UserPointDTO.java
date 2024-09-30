package io.hhplus.tdd.point.dto;

public record UserPointDTO(
        long id,
        long point
) {

    public void validate() {
        if (point < 0) {
            throw new IllegalStateException("User point amount must be greater than 0");
        }
        if (point >= 100_000_000) {
            throw new IllegalStateException("User point amount must be less than or equal to 100_000_000");
        }
    }
}
