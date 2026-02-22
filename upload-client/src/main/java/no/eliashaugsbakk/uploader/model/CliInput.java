package no.eliashaugsbakk.uploader.model;

import java.util.List;

public record CliInput(
    List<String> filePaths,
    String url,
    Integer port,
    String token,
    boolean generateToken,
    boolean networkTest,
    boolean helpRequested
) {}
