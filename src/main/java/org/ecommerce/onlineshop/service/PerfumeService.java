package org.ecommerce.onlineshop.service;

import org.ecommerce.onlineshop.domain.Perfume;
import org.ecommerce.onlineshop.repository.PerfumeRepository;
import org.ecommerce.onlineshop.utils.FileUploadUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class PerfumeService {
    private static final Logger logger = LoggerFactory.getLogger(PerfumeService.class);
    private final PerfumeRepository perfumeRepository;

    public PerfumeService(PerfumeRepository perfumeRepository) {
        this.perfumeRepository = perfumeRepository;
    }

    public String savePerfume(Perfume perfume, MultipartFile image) {
        perfumeRepository.savePerfume(perfume);

        String imageName = perfume.brand().replaceAll("\\s+", "") + perfume.title().replaceAll("\\s+", "") + ".png";
        String uploadDir = "Coursework/src/main/resources/static/images/perfumes";
        try {
            FileUploadUtils.saveFile(uploadDir, imageName, image);
        } catch (IOException e) {
            logger.error("An error occurred while saving the perfume image: {}", e.getMessage());
        }

        return "New perfume successfully saved";
    }

    public String deletePerfume(Long id) {
        perfumeRepository.deletePerfume(id);

        return "Perfume with code " + id + " successfully deleted";
    }

    public Perfume getPerfumeById(Long perfumeId) {
        return perfumeRepository.getPerfumeById(perfumeId);
    }

    public List<Perfume> getAllPerfumes() {
        return perfumeRepository.getAllPerfumes();
    }

    public List<Perfume> getPerfumesByBrandsAndGendersAndPriceRange(List<String> brands, List<String> genders, BigDecimal minPrice, BigDecimal maxPrice,
                                                                    int pageNumber, int pageSize) {
        return perfumeRepository.getPerfumesByBrandsAndGendersAndPriceRange(brands, genders, minPrice, maxPrice, pageNumber, pageSize);
    }

    public List<Perfume> getPerfumesByBrandsAndGenders(List<String> brands, List<String> genders, int pageNumber, int pageSize) {
        return perfumeRepository.getPerfumesByBrandsAndGenders(brands, genders, pageNumber, pageSize);
    }

    public List<Perfume> getPerfumesByBrandsAndPriceRange(List<String> brands, BigDecimal minPrice, BigDecimal maxPrice,
                                                          int pageNumber, int pageSize) {
        return perfumeRepository.getPerfumesByBrandsAndPriceRange(brands, minPrice, maxPrice, pageNumber, pageSize);
    }

    public List<Perfume> getPerfumesByGendersAndPriceRange(List<String> genders, BigDecimal minPrice, BigDecimal maxPrice,
                                                           int pageNumber, int pageSize) {
        return perfumeRepository.getPerfumesByGendersAndPriceRange(genders, minPrice, maxPrice, pageNumber, pageSize);
    }

    public List<Perfume> getPerfumesByBrands(List<String> brands, int pageNumber, int pageSize) {
        return perfumeRepository.getPerfumesByBrands(brands, pageNumber, pageSize);
    }

    public List<Perfume> getPerfumesByGenders(List<String> genders, int pageNumber, int pageSize) {
        return perfumeRepository.getPerfumesByGenders(genders, pageNumber, pageSize);
    }

    public List<Perfume> getPerfumesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, int pageNumber, int pageSize) {
        return perfumeRepository.getPerfumesByPriceRange(minPrice, maxPrice, pageNumber, pageSize);
    }

    public List<Perfume> getFilteredPerfumes(List<String> brands, List<String> genders, String priceRange, int pageNumber, int pageSize) {
        if (brands != null && genders != null && priceRange != null) {
            return getPerfumesByBrandsAndGendersAndPriceRange(brands, genders, new BigDecimal((priceRange.split("-"))[0]), new BigDecimal((priceRange.split("-"))[1]), pageNumber, pageSize);
        }
        else if (brands != null && genders != null) {
            return getPerfumesByBrandsAndGenders(brands, genders, pageNumber, pageSize);
        }
        else if (brands != null && priceRange != null) {
            return getPerfumesByBrandsAndPriceRange(brands, new BigDecimal((priceRange.split("-"))[0]), new BigDecimal((priceRange.split("-"))[1]), pageNumber, pageSize);
        }
        else if (genders != null && priceRange != null) {
            return getPerfumesByGendersAndPriceRange(genders, new BigDecimal((priceRange.split("-"))[0]), new BigDecimal((priceRange.split("-"))[1]), pageNumber, pageSize);
        }
        else if (brands != null) {
            return getPerfumesByBrands(brands, pageNumber, pageSize);
        }
        else if (genders != null) {
            return getPerfumesByGenders(genders, pageNumber, pageSize);
        }

        return getPerfumesByPriceRange(new BigDecimal((priceRange.split("-"))[0]), new BigDecimal((priceRange.split("-"))[1]), pageNumber, pageSize);
    }

    public List<Perfume> searchPerfumes(String keyword, int pageNumber, int pageSize) {
        return perfumeRepository.searchPerfumes(keyword, pageNumber, pageSize);
    }

    public List<Perfume> getPerfumes(int pageNumber, int pageSize) {
        return perfumeRepository.getPerfumes(pageNumber, pageSize);
    }

    public int countPerfumesPages(int pageSize) {
        List<Perfume> perfumes = getPerfumes(0, Integer.MAX_VALUE);
        int totalPerfumes = perfumes.size();

        return (int) Math.ceil((double) totalPerfumes / pageSize);
    }

    public int countFilteredPerfumesPages(List<String> brands, List<String> genders, String priceRange, int pageSize) {
        List<Perfume> filteredPerfumes = getFilteredPerfumes(brands, genders, priceRange, 0, Integer.MAX_VALUE);
        int totalFilteredPerfumes = filteredPerfumes.size();
        return (int) Math.ceil((double) totalFilteredPerfumes / pageSize);
    }

    public int countSearchedPerfumesPages(String keyword, int pageSize) {
        List<Perfume> searchedPerfumes = searchPerfumes(keyword, 0, Integer.MAX_VALUE);
        int totalSearchedPerfumes = searchedPerfumes.size();
        return (int) Math.ceil((double) totalSearchedPerfumes / pageSize);
    }
}
