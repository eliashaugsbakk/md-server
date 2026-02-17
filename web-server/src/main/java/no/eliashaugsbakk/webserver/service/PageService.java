package no.eliashaugsbakk.webserver.service;

import no.eliashaugsbakk.webserver.db.Jdbc.JdbcPageRepository;
import no.eliashaugsbakk.webserver.db.PageRepository;
import no.eliashaugsbakk.webserver.model.Page;

import java.sql.SQLException;
import java.util.List;

public class PageService {
    private final PageRepository pageRepo;

    public PageService(PageRepository pageRepo) {
        this.pageRepo = pageRepo;
    }

    public List<Page> searchPages(String query) {
        try {
            return pageRepo.searchInTitle(query);
        } catch (SQLException e) {
            System.err.println("Database failure during search: " + e.getMessage());
            throw new RuntimeException("The search service is currently unavailable.", e);
        }
    }
}
