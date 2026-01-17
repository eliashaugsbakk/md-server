package mee.prosject.webpage.service;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.boot.CommandLineRunner;

@Component
public class DataLoader implements CommandLineRunner {

    private final PageRegistry registry;

    public DataLoader(PageRegistry registry) {
        this.registry = registry;
    }

    @Override
    public void run(String @NonNull ... args) {
        registry.addPage("Page1", "<p>Hello from page one.</p>");
        registry.addPage("Page2", "<p>Hello from page two.</p>");
        registry.addPage("Page3", "<p>Hello from page three.</p>");
    }
}

