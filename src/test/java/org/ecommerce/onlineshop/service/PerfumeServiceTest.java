package org.ecommerce.onlineshop.service;

import org.ecommerce.onlineshop.domain.Perfume;
import org.ecommerce.onlineshop.repository.PerfumeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PerfumeServiceTest {
    @Mock
    private PerfumeRepository perfumeRepository;

    private PerfumeService perfumeService;

    @BeforeEach
    void setUp() {
        perfumeService = new PerfumeService(perfumeRepository);
    }

    @Test
    void savePerfumeTest() {
        Perfume perfume = new Perfume("Test Title", "Test Brand", 2000,
            "Test Country", "Test Gender", "Test Description",
            new BigDecimal("50.00"), 50, "Test Type", "Test Fragrance Notes");
        MultipartFile image = new MockMultipartFile("test.png", new byte[]{});

        String result = perfumeService.savePerfume(perfume, image);

        assertEquals("New perfume successfully saved", result);
        verify(perfumeRepository, times(1)).savePerfume(perfume);
    }

    @Test
    void deletePerfumeTest() {
        Long id = 1L;

        String result = perfumeService.deletePerfume(id);

        assertEquals("Perfume with code " + id + " successfully deleted", result);
        verify(perfumeRepository, times(1)).deletePerfume(id);
    }

    @Test
    void getPerfumeByIdTest() {
        Long id = 1L;
        Perfume perfume = new Perfume("Test Title", "Test Brand", 2000,
            "Test Country", "Test Gender", "Test Description",
            new BigDecimal("50.00"), 50, "Test Type", "Test Fragrance Notes");

        when(perfumeRepository.getPerfumeById(id)).thenReturn(perfume);

        Perfume result = perfumeService.getPerfumeById(id);

        assertEquals(perfume, result);
        verify(perfumeRepository, times(1)).getPerfumeById(id);
    }

    @Test
    void getAllPerfumesTest() {
        List <Perfume> expectedPerfumes = new ArrayList<>();
        expectedPerfumes.add(new Perfume("Test Title 1", "Test Brand 1", 2000,
            "Test Country 1", "Test Gender 1", "Test Description 1",
            new BigDecimal("50.00"), 50, "Test Type 1", "Test Fragrance Notes 1"));

        when(perfumeRepository.getAllPerfumes()).thenReturn(expectedPerfumes);

        List <Perfume> result = perfumeService.getAllPerfumes();

        assertEquals(expectedPerfumes, result);
        verify(perfumeRepository, times(1)).getAllPerfumes();
    }

    @Test
    void getPerfumesByBrandsAndGendersAndPriceRangeTest() {
        List<String> brands = List.of("Test Brand 1", "Test Brand 2");
        List<String> genders = List.of("Male");
        String priceRange = "100.00-200.00";
        int pageNumber = 0;
        int pageSize = 10;

        List<Perfume> expectedPerfumes = new ArrayList<>();
        expectedPerfumes.add(new Perfume("Test Title 1", "Test Brand 1", 2000,
            "Test Country 1", "Male", "Test Description 1", new BigDecimal("150.00"),
            50, "Test Type 1", "Test Fragrance Notes 1"));

        when(perfumeRepository.getPerfumesByBrandsAndGendersAndPriceRange(brands, genders,
            new BigDecimal(priceRange.split("-")[0]), new BigDecimal(priceRange.split("-")[1]),
            pageNumber, pageSize)).thenReturn(expectedPerfumes);

        List<Perfume> result = perfumeService.getFilteredPerfumes(brands, genders, priceRange, pageNumber, pageSize);

        assertEquals(expectedPerfumes, result);
        verify(perfumeRepository, times(1)).getPerfumesByBrandsAndGendersAndPriceRange(brands, genders,
            new BigDecimal(priceRange.split("-")[0]), new BigDecimal(priceRange.split("-")[1]),
            pageNumber, pageSize);
    }

    @Test
    void getPerfumesByBrandsAndGendersTest() {
        List<String> brands = List.of("Test Brand 1");
        List<String> genders = List.of("Male");
        int pageNumber = 0;
        int pageSize = 10;

        List<Perfume> expectedPerfumes = new ArrayList<>();
        expectedPerfumes.add(new Perfume("Test Title 1", "Test Brand 1", 2000,
            "Test Country 1", "Male", "Test Description 1", new BigDecimal("50.00"),
            50, "Test Type 1", "Test Fragrance Notes 1"));

        when(perfumeRepository.getPerfumesByBrandsAndGenders(brands, genders, pageNumber, pageSize))
            .thenReturn(expectedPerfumes);

        List<Perfume> result = perfumeService.getFilteredPerfumes(brands, genders, null, pageNumber, pageSize);

        assertEquals(expectedPerfumes, result);
        verify(perfumeRepository, times(1))
            .getPerfumesByBrandsAndGenders(brands, genders, pageNumber, pageSize);
    }

    @Test
    void getPerfumesByBrandsAndPriceRangeTest() {
        List<String> brands = List.of("Test Brand 1", "Test Brand 2");
        String priceRange = "100.00-200.00";
        int pageNumber = 0;
        int pageSize = 10;

        List<Perfume> expectedPerfumes = new ArrayList<>();
        expectedPerfumes.add(new Perfume("Test Title 1", "Test Brand 1", 2000,
            "Test Country 1", "Male", "Test Description 1", new BigDecimal("150.00"),
            50, "Test Type 1", "Test Fragrance Notes 1"));

        when(perfumeRepository.getPerfumesByBrandsAndPriceRange(brands, new BigDecimal(priceRange.split("-")[0]),
            new BigDecimal(priceRange.split("-")[1]), pageNumber, pageSize))
            .thenReturn(expectedPerfumes);

        List<Perfume> result = perfumeService.getFilteredPerfumes(brands, null, priceRange, pageNumber, pageSize);

        assertEquals(expectedPerfumes, result);
        verify(perfumeRepository, times(1)).getPerfumesByBrandsAndPriceRange(brands, new BigDecimal(priceRange.split("-")[0]),
            new BigDecimal(priceRange.split("-")[1]), pageNumber, pageSize);
    }

    @Test
    void getPerfumesByGendersAndPriceRangeTest() {
        List<String> genders = List.of("Male", "Female");
        String priceRange = "100.00-200.00";
        int pageNumber = 0;
        int pageSize = 10;

        List<Perfume> expectedPerfumes = new ArrayList<>();
        expectedPerfumes.add(new Perfume("Test Title 1", "Test Brand 1", 2000,
            "Test Country 1", "Male", "Test Description 1", new BigDecimal("150.00"),
            50, "Test Type 1", "Test Fragrance Notes 1"));

        when(perfumeRepository.getPerfumesByGendersAndPriceRange(genders, new BigDecimal(priceRange.split("-")[0]),
            new BigDecimal(priceRange.split("-")[1]), pageNumber, pageSize))
            .thenReturn(expectedPerfumes);

        List<Perfume> result = perfumeService.getFilteredPerfumes(null, genders, priceRange, pageNumber, pageSize);

        assertEquals(expectedPerfumes, result);
        verify(perfumeRepository, times(1)).getPerfumesByGendersAndPriceRange(genders, new BigDecimal(priceRange.split("-")[0]),
            new BigDecimal(priceRange.split("-")[1]), pageNumber, pageSize);
    }

    @Test
    void getPerfumesByBrandsTest() {
        List<String> brands = List.of("Test Brand 1", "Test Brand 2");
        int pageNumber = 0;
        int pageSize = 10;

        List<Perfume> expectedPerfumes = new ArrayList<>();
        expectedPerfumes.add(new Perfume("Test Title 1", "Test Brand 1", 2000,
            "Test Country 1", "Male", "Test Description 1", new BigDecimal("150.00"),
            50, "Test Type 1", "Test Fragrance Notes 1"));

        when(perfumeRepository.getPerfumesByBrands(brands, pageNumber, pageSize))
            .thenReturn(expectedPerfumes);

        List<Perfume> result = perfumeService.getFilteredPerfumes(brands, null, null, pageNumber, pageSize);

        assertEquals(expectedPerfumes, result);
        verify(perfumeRepository, times(1)).getPerfumesByBrands(brands, pageNumber, pageSize);
    }

    @Test
    void getPerfumesByGendersTest() {
        List<String> genders = List.of("Male", "Female");
        int pageNumber = 0;
        int pageSize = 10;

        List<Perfume> expectedPerfumes = new ArrayList<>();
        expectedPerfumes.add(new Perfume("Test Title 1", "Test Brand 1", 2000,
            "Test Country 1", "Male", "Test Description 1", new BigDecimal("150.00"),
            50, "Test Type 1", "Test Fragrance Notes 1"));

        when(perfumeRepository.getPerfumesByGenders(genders, pageNumber, pageSize))
            .thenReturn(expectedPerfumes);

        List<Perfume> result = perfumeService.getFilteredPerfumes(null, genders, null, pageNumber, pageSize);

        assertEquals(expectedPerfumes, result);
        verify(perfumeRepository, times(1)).getPerfumesByGenders(genders, pageNumber, pageSize);
    }

    @Test
    void getPerfumesByPriceRangeTest() {
        String priceRange = "100.00-200.00";
        int pageNumber = 0;
        int pageSize = 10;

        List<Perfume> expectedPerfumes = new ArrayList<>();
        expectedPerfumes.add(new Perfume("Test Title 1", "Test Brand 1", 2000,
            "Test Country 1", "Male", "Test Description 1", new BigDecimal("150.00"),
            50, "Test Type 1", "Test Fragrance Notes 1"));

        when(perfumeRepository.getPerfumesByPriceRange(new BigDecimal(priceRange.split("-")[0]),
            new BigDecimal(priceRange.split("-")[1]), pageNumber, pageSize))
            .thenReturn(expectedPerfumes);

        List<Perfume> result = perfumeService.getFilteredPerfumes(null, null, priceRange, pageNumber, pageSize);

        assertEquals(expectedPerfumes, result);
        verify(perfumeRepository, times(1)).getPerfumesByPriceRange(new BigDecimal(priceRange.split("-")[0]),
            new BigDecimal(priceRange.split("-")[1]), pageNumber, pageSize);
    }

    @Test
    void searchPerfumesTest() {
        String keyword = "Test";
        int pageNumber = 0;
        int pageSize = 10;

        List<Perfume> expectedPerfumes = new ArrayList<>();
        expectedPerfumes.add(new Perfume("Test Title 1", "Test Brand 1", 2000,
            "Test Country 1", "Male", "Test Description 1", new BigDecimal("150.00"),
            50, "Test Type 1", "Test Fragrance Notes 1"));

        when(perfumeRepository.searchPerfumes(keyword, pageNumber, pageSize))
            .thenReturn(expectedPerfumes);

        List<Perfume> result = perfumeService.searchPerfumes(keyword, pageNumber, pageSize);

        assertEquals(expectedPerfumes, result);
        verify(perfumeRepository, times(1)).searchPerfumes(keyword, pageNumber, pageSize);
    }

    @Test
    void getPerfumesTest() {
        int pageNumber = 0;
        int pageSize = 10;

        List<Perfume> expectedPerfumes = new ArrayList<>();
        expectedPerfumes.add(new Perfume("Test Title 1", "Test Brand 1", 2000,
            "Test Country 1", "Male", "Test Description 1", new BigDecimal("150.00"),
            50, "Test Type 1", "Test Fragrance Notes 1"));

        when(perfumeRepository.getPerfumes(pageNumber, pageSize))
            .thenReturn(expectedPerfumes);

        List<Perfume> result = perfumeService.getPerfumes(pageNumber, pageSize);

        assertEquals(expectedPerfumes, result);
        verify(perfumeRepository, times(1)).getPerfumes(pageNumber, pageSize);
    }

    @Test
    void countPerfumesPagesTest() {
        int pageSize = 4;

        List<Perfume> perfumes = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            perfumes.add(new Perfume("Test Title " + i, "Test Brand",
                2000, "Test Country", "Unisex", "Test Description",
                new BigDecimal("150.00"), 50, "Test Type", "Test Notes"));
        }

        when(perfumeRepository.getPerfumes(0, Integer.MAX_VALUE)).thenReturn(perfumes);

        int expectedPages = 2;
        int result = perfumeService.countPerfumesPages(pageSize);

        assertEquals(expectedPages, result);
    }

    @Test
    void countFilteredPerfumesPagesTest() {
        List<String> brands = List.of("Test Brand 1");
        List<String> genders = null;
        String priceRange = null;
        int pageSize = 4;

        List<Perfume> filteredPerfumes = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            filteredPerfumes.add(new Perfume("Test Title " + i, "Test Brand 1",
                2000, "Test Country", "Unisex", "Test Description",
                new BigDecimal("150.00"), 50, "Test Type", "Test Notes"));
        }

        when(perfumeRepository.getPerfumesByBrands(brands, 0, Integer.MAX_VALUE))
            .thenReturn(filteredPerfumes);

        int expectedPages = 2;

        int result = perfumeService.countFilteredPerfumesPages(brands, genders, priceRange, pageSize);

        assertEquals(expectedPages, result);
    }

    @Test
    void countSearchedPerfumesPagesTest() {
        String keyword = "Test";
        int pageSize = 4;

        List<Perfume> searchedPerfumes = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            searchedPerfumes.add(new Perfume("Test Title " + i, "Test Brand",
                2000, "Test Country", "Unisex", "Test Description",
                new BigDecimal("150.00"), 50, "Test Type", "Test Notes"));
        }

        when(perfumeRepository.searchPerfumes(keyword, 0, Integer.MAX_VALUE))
            .thenReturn(searchedPerfumes);

        int expectedPages = 2;
        int result = perfumeService.countSearchedPerfumesPages(keyword, pageSize);

        assertEquals(expectedPages, result);
    }
}
