package no.eliashaugsbakk.webserver.model;

import java.time.Instant;

public record Page(
        long id,
        String title,
        String slug,
        Instant created,
        String content
) {}
