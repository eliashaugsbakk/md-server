package mee.prosject.webpage.controller;

import mee.prosject.webpage.service.PageRegistry;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class PageController {

    private final PageRegistry registry;

    public PageController(PageRegistry registry) {
        this.registry = registry;
    }


    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("title", "Home");
        model.addAttribute("content", "<p>Welcome to my website.</p>");
        model.addAttribute("pages", registry.getAllPages());
        return "pageView";
    }


    // Show page by slug
    @GetMapping("/{slug}")
    public String viewPage(@PathVariable String slug, Model model) {
        var page = registry.getBySlug(slug);
        model.addAttribute("title", page.title());
        model.addAttribute("content", page.content());
        model.addAttribute("pages", registry.getAllPages());
        return "pageView";
    }
}