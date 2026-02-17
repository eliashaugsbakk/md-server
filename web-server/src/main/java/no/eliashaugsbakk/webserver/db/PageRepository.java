package no.eliashaugsbakk.webserver.db;

import no.eliashaugsbakk.webserver.model.Page;

import java.sql.SQLException;
import java.util.List;

public interface PageRepository {
    Page getPageBySlug(String slug) throws SQLException;
    List<Page> getAllPages() throws SQLException;
    void addPage(Page page) throws SQLException;
    List<Page> searchInTitle(String query) throws SQLException;
}
