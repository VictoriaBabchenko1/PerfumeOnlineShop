package org.ecommerce.onlineshop.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.ecommerce.onlineshop.domain.Perfume;
import org.ecommerce.onlineshop.service.PerfumeService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class PerfumeController {
    private final PerfumeService perfumeService;
    static final int PAGE_SIZE = 6;

    public PerfumeController(PerfumeService perfumeService) {
        this.perfumeService = perfumeService;
    }

    @GetMapping("/perfumes/perfume/{id}")
    public String getPerfumeById(@PathVariable("id") Long id, Model model) {
        Perfume perfume = perfumeService.getPerfumeById(id);
        model.addAttribute("perfume", perfume);

        return "perfume";
    }

    @GetMapping("/perfumes")
    public String allPerfumes(Model model, HttpServletRequest request, @RequestParam(defaultValue = "1") int pageNumber) {
        List<Perfume> perfumes = perfumeService.getPerfumes(pageNumber - 1, PAGE_SIZE);
        int totalPerfumesPages = perfumeService.countPerfumesPages(PAGE_SIZE);

        model.addAttribute("perfumes", perfumes);
        model.addAttribute("currentPage", pageNumber - 1);
        model.addAttribute("totalPages", totalPerfumesPages);
        model.addAttribute("currentUrl", request.getRequestURI());

        return "perfumes";
    }

    @GetMapping("/perfumes/filter")
    public String filterPerfumes(@RequestParam(required = false) List<String> brands,
                                 @RequestParam(required = false) List<String> gender,
                                 @RequestParam(required = false) String priceRange,
                                 @RequestParam(defaultValue = "1") int pageNumber,
                                 HttpServletRequest request,
                                 Model model) {
        List<Perfume> filteredPerfumes = perfumeService.getFilteredPerfumes(brands, gender, priceRange, pageNumber - 1, PAGE_SIZE);
        int totalPerfumesPages = perfumeService.countFilteredPerfumesPages(brands, gender, priceRange, PAGE_SIZE);

        model.addAttribute("perfumes", filteredPerfumes);
        model.addAttribute("currentPage", pageNumber - 1);
        model.addAttribute("totalPages", totalPerfumesPages);
        model.addAttribute("currentUrl", request.getRequestURI());

        model.addAttribute("brands", brands);
        model.addAttribute("gender", gender);
        model.addAttribute("priceRange", priceRange);

        return "perfumes";
    }

    @GetMapping("/perfumes/search")
    public String searchPerfumes(@RequestParam String keyword,
                                 @RequestParam(defaultValue = "1") int pageNumber,
                                 HttpServletRequest request,
                                 Model model) {
        List<Perfume> searchedPerfumes = perfumeService.searchPerfumes(keyword, pageNumber - 1, PAGE_SIZE);
        int totalPerfumesPages = perfumeService.countSearchedPerfumesPages(keyword, PAGE_SIZE);

        model.addAttribute("perfumes", searchedPerfumes);
        model.addAttribute("currentPage", pageNumber - 1);
        model.addAttribute("totalPages", totalPerfumesPages);
        model.addAttribute("currentUrl", request.getRequestURI());
        model.addAttribute("keyword", keyword);

        return "perfumes";
    }
}
