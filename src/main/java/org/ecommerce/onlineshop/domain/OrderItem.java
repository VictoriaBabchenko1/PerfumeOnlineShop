package org.ecommerce.onlineshop.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "perfume_id", nullable = false)
    private Long perfumeId;

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(name = "perfume_title", nullable = false)
    private String perfumeTitle;

    @Column(name = "perfume_brand", nullable = false)
    private String perfumeBrand;

    @Column(name = "perfume_volume", nullable = false)
    private Integer perfumeVolume;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
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

