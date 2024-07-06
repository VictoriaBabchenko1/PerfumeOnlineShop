package org.ecommerce.onlineshop.repository.mapper;

import org.ecommerce.onlineshop.domain.Perfume;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class PerfumeRowMapper implements RowMapper<Perfume> {
    @Override
    public Perfume mapRow(ResultSet rs, int rowNum) throws SQLException {
        Long id = rs.getLong("id");
        String title = rs.getString("title");
        String brand = rs.getString("brand");
        Integer year = rs.getInt("year");
        String country = rs.getString("country");
        String gender = rs.getString("gender");
        String description = rs.getString("description");
        BigDecimal price = rs.getBigDecimal("price");
        Integer volume = rs.getInt("volume");
        String type = rs.getString("type");
        String fragranceNotes = rs.getString("fragrance_notes");

        return new Perfume(id, title, brand, year, country, gender, description, price, volume, type, fragranceNotes);
    }
}
