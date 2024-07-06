package org.ecommerce.onlineshop.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private Long perfumeId;
    private Integer quantity;
    private BigDecimal price;
    private String perfumeTitle;
    private String perfumeBrand;
    private Integer perfumeVolume;

    public CartItem() {
    }

    public CartItem(Long userId, Long perfumeId, Integer quantity, BigDecimal price) {
        this.userId = userId;
        this.perfumeId = perfumeId;
        this.quantity = quantity;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getPerfumeId() {
        return perfumeId;
    }

    public void setPerfumeId(Long perfumeId) {
        this.perfumeId = perfumeId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public String getPerfumeTitle() {
        return perfumeTitle;
    }

    public void setPerfumeTitle(String perfumeTitle) {
        this.perfumeTitle = perfumeTitle;
    }

    public String getPerfumeBrand() {
        return perfumeBrand;
    }

    public void setPerfumeBrand(String perfumeBrand) {
        this.perfumeBrand = perfumeBrand;
    }

    public Integer getPerfumeVolume() {
        return perfumeVolume;
    }

    public void setPerfumeVolume(Integer perfumeVolume) {
        this.perfumeVolume = perfumeVolume;
    }
}
