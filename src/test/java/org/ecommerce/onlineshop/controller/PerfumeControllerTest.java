package org.ecommerce.onlineshop.controller;

import org.ecommerce.onlineshop.domain.Perfume;
import org.ecommerce.onlineshop.service.PerfumeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PerfumeControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PerfumeService perfumeService;

    @Test
    void getPerfumeByIdTest() throws Exception {
        Perfume perfume = new Perfume("Test Title", "Test Brand", 2000,
            "Test Country", "Test Gender", "Test Description",
            new BigDecimal("50.00"), 50, "Test Type", "Test Fragrance Notes");

        when(perfumeService.getPerfumeById(1L)).thenReturn(perfume);

        mockMvc.perform(get("/perfumes/perfume/1"))
            .andExpect(status().isOk())
            .andExpect(view().name("perfume"))
            .andExpect(model().attributeExists("perfume"))
            .andExpect(model().attribute("perfume", perfume));
    }

    @Test
    void allPerfumesTest() throws Exception {
        when(perfumeService.getPerfumes(anyInt(), anyInt())).thenReturn(Collections.emptyList());
        when(perfumeService.countPerfumesPages(anyInt())).thenReturn(1);

        mockMvc.perform(get("/perfumes"))
            .andExpect(status().isOk())
            .andExpect(view().name("perfumes"))
            .andExpect(model().attributeExists("perfumes", "currentPage", "totalPages", "currentUrl"));
    }

    @Test
    void filterPerfumesTest_WithFilters() throws Exception {
        List<String> brands = Collections.singletonList("Brand X");
        List<String> gender = Collections.singletonList("Male");
        String priceRange = "100-200";

        when(perfumeService.getFilteredPerfumes(eq(brands), eq(gender), eq(priceRange), anyInt(), anyInt()))
            .thenReturn(Collections.singletonList(new Perfume("Test Title", "Brand X", 2000,
                "Test Country", "Male", "Test Description",
                new BigDecimal("50.00"), 50, "Test Type", "Test Fragrance Notes")));
        when(perfumeService.countFilteredPerfumesPages(eq(brands), eq(gender), eq(priceRange), anyInt())).thenReturn(1);

        mockMvc.perform(get("/perfumes/filter")
                .param("brands", "Brand X")
                .param("gender", "Male")
                .param("priceRange", "100-200"))
            .andExpect(status().isOk())
            .andExpect(view().name("perfumes"))
            .andExpect(model().attributeExists("perfumes"))
            .andExpect(model().attribute("perfumes", Collections.singletonList(new Perfume("Test Title", "Brand X", 2000,
                "Test Country", "Male", "Test Description",
                new BigDecimal("50.00"), 50, "Test Type", "Test Fragrance Notes"))));

    }

    @Test
    void filterPerfumesTest_WithEmptyFilters() throws Exception {
        when(perfumeService.getFilteredPerfumes(anyList(), anyList(), anyString(), anyInt(), anyInt()))
            .thenReturn(Collections.emptyList());
        when(perfumeService.countFilteredPerfumesPages(anyList(), anyList(), anyString(), anyInt())).thenReturn(0);

        mockMvc.perform(get("/perfumes/filter"))
            .andExpect(status().isOk())
            .andExpect(view().name("perfumes"))
            .andExpect(model().attributeExists("perfumes"))
            .andExpect(model().attribute("perfumes", Collections.emptyList()));
    }

    @Test
    void searchPerfumesTest_WithKeyword() throws Exception {
        String keyword = "test";
        List<Perfume> searchedPerfumes = Collections.singletonList(new Perfume("Test Title", "Test Brand", 2000,
            "Test Country", "Test Gender", "Test Description",
            new BigDecimal("50.00"), 50, "Test Type", "Test Fragrance Notes"));
        when(perfumeService.searchPerfumes(eq(keyword), anyInt(), anyInt())).thenReturn(searchedPerfumes);
        when(perfumeService.countSearchedPerfumesPages(eq(keyword), anyInt())).thenReturn(1);

        mockMvc.perform(get("/perfumes/search")
                .param("keyword", keyword))
            .andExpect(status().isOk())
            .andExpect(view().name("perfumes"))
            .andExpect(model().attributeExists("perfumes"))
            .andExpect(model().attribute("perfumes", searchedPerfumes));
    }

    @Test
    void searchPerfumesTest_WithEmptyKeyword() throws Exception {
        when(perfumeService.searchPerfumes(eq(""), anyInt(), anyInt())).thenReturn(Collections.emptyList());
        when(perfumeService.countSearchedPerfumesPages(eq(""), anyInt())).thenReturn(0);

        mockMvc.perform(get("/perfumes/search")
                .param("keyword", ""))
            .andExpect(status().isOk())
            .andExpect(view().name("perfumes"))
            .andExpect(model().attributeExists("perfumes"))
            .andExpect(model().attribute("perfumes", Collections.emptyList()));
    }
}
