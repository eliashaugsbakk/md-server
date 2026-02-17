package no.eliashaugsbakk.webserver.api;

import com.sun.net.httpserver.HttpExchange;
import no.eliashaugsbakk.webserver.db.PageRepository;
import no.eliashaugsbakk.webserver.model.Page;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.List;

public class SearchHandler extends BaseHandler{

    public SearchHandler(PageRepository pageRepo) {
         super(pageRepo);
    }

    private String getSearchQuery(HttpExchange exchange) {
        // 1. Use getRawQuery() to see the encoded "%25" instead of the raw "%"
        String rawQuery = exchange.getRequestURI().getRawQuery();

        if (rawQuery == null || rawQuery.isEmpty()) {
            return "";
        }

        try {
            String[] pairs = rawQuery.split("&");
            for (String pair : pairs) {
                String[] parts = pair.split("=", 2);
                if (parts.length == 2 && parts[0].equals("q")) {
                    return URLDecoder.decode(parts[1], StandardCharsets.UTF_8);
                }
            }
        } catch (IllegalArgumentException e) {
            System.err.println("Malformed search parameters: " + e.getMessage());
        }

        return "";
    }

    @Override
    protected String getTitle(HttpExchange exchange) {
        String q = getSearchQuery(exchange);
        return q.isEmpty() ? "Search" : q + " - Search results";
    }

    @Override
    protected String getContent(HttpExchange exchange) throws SQLException {
        String q = getSearchQuery(exchange);

        if (q.isEmpty()) {
            return "<p>Empty search</p>";
        }

        List<Page> results = pageRepo.searchInTitle(q);

        if (results.isEmpty()) {
            return "<p>No results found for <strong>" + q + "<strong></p>";
        }

        StringBuilder html = new StringBuilder("<h3>Found " + results.size() + " matches:</h3><ul>");
        for (Page p : results) {
            html.append("<li><a href=\"/wiki/").append(p.slug()).append("\">")
                    .append(p.title()).append("</a></li>");
        }
        html.append("</ul>");
        return html.toString();
    }
}
