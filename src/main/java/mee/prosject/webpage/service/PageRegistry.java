package mee.prosject.webpage.service;

import mee.prosject.webpage.model.Page;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class PageRegistry {
    private final Map<String, Page> slugIndex = new ConcurrentHashMap<>();

    long nextPageId = 1;

    public Page addPage(String title, String content) {
        long pageId = nextPageId++;
        String slug = generateSlug(title);
        Instant written = Instant.now();
        Page page = new Page(pageId, title, slug, content, written);
        slugIndex.put(slug, page);
        return page;
    }

    // Lookup by slug
    public Page getBySlug(String slug) {
        return slugIndex.get(slug);
    }

    public Collection<Page> getAllPages() {
        return slugIndex.values();
    }


    private String generateSlug(String title) {
        String base = title.toLowerCase()
                .replaceAll("æ", "ae")
                .replaceAll("ø", "oe")
                .replaceAll("å", "aa")
                .replaceAll("[^a-z0-9]+", "-")
                .replaceAll("^-|-$", "");

        String slug = base;
        int counter = 2;
        while (slugIndex.containsKey(slug)) {
            slug = base + "-" + counter++;
        }
        return slug;
    }
}
