package no.eliashaugsbakk.webserver.api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import no.eliashaugsbakk.webserver.db.PageRepository;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HomeHandler extends BaseHandler {

    public HomeHandler(PageRepository pageRepo) {
        super(pageRepo);
    }

    @Override
    protected String getTitle(HttpExchange exchange) throws Exception {
        return "Home Page";
    }

    @Override
    protected String getContent(HttpExchange exchange) throws Exception {
        return "<h1>This is the Home Page</h1>";
    }
}
