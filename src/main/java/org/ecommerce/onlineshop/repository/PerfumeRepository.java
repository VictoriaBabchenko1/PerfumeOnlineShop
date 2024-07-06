package org.ecommerce.onlineshop.repository;

import org.ecommerce.onlineshop.domain.Perfume;
import org.ecommerce.onlineshop.repository.mapper.PerfumeRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public class PerfumeRepository {
    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final PerfumeRowMapper perfumeRowMapper;

    public PerfumeRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        perfumeRowMapper = new PerfumeRowMapper();
    }

    public void savePerfume(Perfume perfume) {
        String query = """
            INSERT INTO perfumes (title, brand, year, country, description, gender, price, type, volume, fragrance_notes)
            VALUES (:title, :brand, :year, :country, :description, :gender, :price, :type, :volume, :fragrance_notes)
            """;

        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("title", perfume.title());
        params.addValue("brand", perfume.brand());
        params.addValue("year", perfume.year());
        params.addValue("country", perfume.country());
        params.addValue("description", perfume.description());
        params.addValue("gender", perfume.gender());
        params.addValue("price", perfume.price());
        params.addValue("type", perfume.type());
        params.addValue("volume", perfume.volume());
        params.addValue("fragrance_notes", perfume.fragranceNotes());

        jdbcTemplate.update(query, params);
    }

    public void deletePerfume(Long id) {
        String query = "DELETE FROM perfumes WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        jdbcTemplate.update(query, params);
    }

    public Perfume getPerfumeById(Long id) {
        String query = "SELECT * FROM perfumes WHERE id = :id";
        MapSqlParameterSource params = new MapSqlParameterSource("id", id);

        return jdbcTemplate.queryForObject(query, params, perfumeRowMapper);
    }

    public List<Perfume> getAllPerfumes() {
        String query = "SELECT * FROM perfumes ORDER BY id DESC";

        return jdbcTemplate.query(query, perfumeRowMapper);
    }

    public List<Perfume> getPerfumesByBrands(List<String> brands, int pageNumber, int pageSize) {
        String query = "SELECT * FROM perfumes WHERE brand IN (:brands) ORDER BY id DESC LIMIT :pageSize OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("brands", brands);
        params.addValue("pageSize", pageSize);
        params.addValue("offset", pageNumber * pageSize);

        return jdbcTemplate.query(query, params, perfumeRowMapper);
    }

    public List<Perfume> getPerfumesByGenders(List<String> genders, int pageNumber, int pageSize) {
        String query = "SELECT * FROM perfumes WHERE gender IN (:genders) ORDER BY id DESC LIMIT :pageSize OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("genders", genders);
        params.addValue("pageSize", pageSize);
        params.addValue("offset", pageNumber * pageSize);

        return jdbcTemplate.query(query, params, perfumeRowMapper);
    }

    public List<Perfume> getPerfumesByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, int pageNumber, int pageSize) {
        String query = "SELECT * FROM perfumes WHERE price BETWEEN :minPrice AND :maxPrice ORDER BY id DESC LIMIT :pageSize OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("minPrice", minPrice);
        params.addValue("maxPrice", maxPrice);
        params.addValue("pageSize", pageSize);
        params.addValue("offset", pageNumber * pageSize);

        return jdbcTemplate.query(query, params, perfumeRowMapper);
    }

    public List<Perfume> getPerfumesByBrandsAndGenders(List<String> brands, List<String> genders, int pageNumber, int pageSize) {
        String query = "SELECT * FROM perfumes WHERE brand IN (:brands) AND gender IN (:genders) ORDER BY id DESC LIMIT :pageSize OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("brands", brands);
        params.addValue("genders", genders);
        params.addValue("pageSize", pageSize);
        params.addValue("offset", pageNumber * pageSize);

        return jdbcTemplate.query(query, params, perfumeRowMapper);
    }

    public List<Perfume> getPerfumesByGendersAndPriceRange(List<String> genders, BigDecimal minPrice,
                                                           BigDecimal maxPrice, int pageNumber, int pageSize) {
        String query = "SELECT * FROM perfumes WHERE gender IN (:genders) AND price BETWEEN :minPrice AND :maxPrice ORDER BY id DESC LIMIT :pageSize OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("genders", genders);
        params.addValue("minPrice", minPrice);
        params.addValue("maxPrice", maxPrice);
        params.addValue("pageSize", pageSize);
        params.addValue("offset", pageNumber * pageSize);

        return jdbcTemplate.query(query, params, perfumeRowMapper);
    }

    public List<Perfume> getPerfumesByBrandsAndPriceRange(List<String> brands, BigDecimal minPrice,
                                                          BigDecimal maxPrice, int pageNumber, int pageSize) {
        String query = "SELECT * FROM perfumes WHERE brand IN (:brands) AND price BETWEEN :minPrice AND :maxPrice ORDER BY id DESC LIMIT :pageSize OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("brands", brands);
        params.addValue("minPrice", minPrice);
        params.addValue("maxPrice", maxPrice);
        params.addValue("pageSize", pageSize);
        params.addValue("offset", pageNumber * pageSize);

        return jdbcTemplate.query(query, params, perfumeRowMapper);
    }

    public List<Perfume> getPerfumesByBrandsAndGendersAndPriceRange(List<String> brands, List<String> genders, BigDecimal minPrice,
                                                                    BigDecimal maxPrice, int pageNumber, int pageSize) {
        String query = "SELECT * FROM perfumes WHERE brand IN (:brands) AND gender IN (:genders) AND price BETWEEN :minPrice AND :maxPrice ORDER BY id DESC LIMIT :pageSize OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("brands", brands);
        params.addValue("genders", genders);
        params.addValue("minPrice", minPrice);
        params.addValue("maxPrice", maxPrice);
        params.addValue("pageSize", pageSize);
        params.addValue("offset", pageNumber * pageSize);

        return jdbcTemplate.query(query, params, perfumeRowMapper);
    }

    public List<Perfume> searchPerfumes(String keyword, int pageNumber, int pageSize) {
        String sql = """
            SELECT * FROM perfumes WHERE title ILIKE :keyword OR description ILIKE :keyword OR brand ILIKE :keyword
            ORDER BY id DESC LIMIT :pageSize OFFSET :offset
            """;
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("keyword", "%" + keyword + "%");
        params.addValue("pageSize", pageSize);
        params.addValue("offset", pageNumber * pageSize);

        return jdbcTemplate.query(sql, params, perfumeRowMapper);
    }

    public List<Perfume> getPerfumes(int pageNumber, int pageSize) {
        String query = "SELECT * FROM perfumes ORDER BY id DESC LIMIT :pageSize OFFSET :offset";
        MapSqlParameterSource params = new MapSqlParameterSource();
        params.addValue("pageSize", pageSize);
        params.addValue("offset", pageNumber * pageSize);

        return jdbcTemplate.query(query, params, perfumeRowMapper);
    }
}
