package mee.prosject.webpage.model;

import java.time.Instant;

public record ImageMetaData(
        Long id,
        String filename,
        String contentType,
        Instant uploadedAt
) {}
