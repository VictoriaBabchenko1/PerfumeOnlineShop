package org.ecommerce.onlineshop.domain;

import io.micrometer.common.lang.NonNull;
import io.micrometer.common.lang.Nullable;

import java.math.BigDecimal;

public record Perfume(@Nullable Long id,
                      @NonNull String title,
                      @NonNull String brand,
                      @NonNull Integer year,
                      @NonNull String country,
                      @NonNull String gender,
                      @NonNull String description,
                      @NonNull BigDecimal price,
                      @NonNull Integer volume,
                      @NonNull String type,
                      @NonNull String fragranceNotes) {
    public Perfume(@NonNull String title,
                   @NonNull String brand,
                   @NonNull Integer year,
                   @NonNull String country,
                   @NonNull String gender,
                   @NonNull String description,
                   @NonNull BigDecimal price,
                   @NonNull Integer volume,
                   @NonNull String type,
                   @NonNull String fragranceNotes) {
        this(null,
            title,
            brand,
            year,
            country,
            gender,
            description,
            price,
            volume,
            type,
            fragranceNotes);
    }
}
