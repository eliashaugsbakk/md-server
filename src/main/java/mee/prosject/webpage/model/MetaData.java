package mee.prosject.webpage.model;


import java.time.Instant;

public record MetaData(
        long pageId,
        String title,
        String slug, // a unique url safe version of the page title
        Instant written
) {
}