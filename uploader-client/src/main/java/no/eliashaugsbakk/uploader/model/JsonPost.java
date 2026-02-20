package no.eliashaugsbakk.uploader.model;

import java.util.List;

public record JsonPost(
        String title,
        String html,
        List<String> images
) {}
