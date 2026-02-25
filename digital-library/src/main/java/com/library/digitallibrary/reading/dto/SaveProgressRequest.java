package com.library.digitallibrary.reading.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record SaveProgressRequest(
        @NotNull @Min(1) Integer lastPage,
        @NotNull @Min(0) @Max(100) Double percentage
) {}