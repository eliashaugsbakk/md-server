package mee.prosject.webpage.model;

import java.time.Instant;

public record Page(
        long pageId,
        String title,
        String slug, // a unique url safe version of the page title
        String content, // html:w
        Instant written
) {
}