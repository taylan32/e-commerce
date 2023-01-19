package com.example.ecommerce.service.filter;

import com.example.ecommerce.model.Category;
import com.example.ecommerce.model.Product;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class ProductSpec {
    public static final String PRODUCT_NAME = "productName";
    public static final String PRICE = "price";
    public static final String UNITS_IN_STOCK = "unitsInStock";
    public static final String CATEGORY = "category";

    private ProductSpec() {}

    public static Specification<Product> filterBy(ProductFilter productFilter) {
        return Specification
                .where(productNameLike(productFilter.getProductName()))
                .and(hasCategory(productFilter.getCategoryId()))
                .and(hasPriceGreaterThanOrEqual(productFilter.getPriceFrom()))
                .and(hasPriceLessThanOrEqual(productFilter.getPriceTo()))
                .and(hasUnitsInStockGreaterThanOrEqual(productFilter.getStockFrom()))
                .and(hasUnitsInStockLessThanOrEqual(productFilter.getStockTo()));
    }

    private static Specification<Product> productNameLike(String productName) {
        return (root, query, criteriaBuilder) -> productName == null || productName.isBlank() ?
                criteriaBuilder.conjunction() :
                criteriaBuilder.like(root.get(PRODUCT_NAME),productName + "%");
    }
    private static Specification<Product> hasCategory(Long categoryId) {
        return (root, query, criteriaBuilder) -> categoryId == null ?
                criteriaBuilder.conjunction() :
                criteriaBuilder.equal(root.join(CATEGORY).get("id"), categoryId);
    }

    private static Specification<Product> hasPriceGreaterThanOrEqual(BigDecimal priceFrom) {
        return (root, query, criteriaBuilder) -> priceFrom == null ?
                criteriaBuilder.conjunction() :
                criteriaBuilder.greaterThanOrEqualTo(root.get(PRICE), priceFrom);
    }

    private static Specification<Product> hasPriceLessThanOrEqual(BigDecimal priceTo) {
        return (root, query, criteriaBuilder) -> priceTo == null ?
                criteriaBuilder.conjunction() :
                criteriaBuilder.lessThanOrEqualTo(root.get(PRICE), priceTo);
    }

    private static Specification<Product> hasUnitsInStockGreaterThanOrEqual(Integer unitsInStockFrom) {
        return (root, query, criteriaBuilder) -> unitsInStockFrom == null ?
                criteriaBuilder.conjunction() :
                criteriaBuilder.greaterThanOrEqualTo(root.get(UNITS_IN_STOCK), unitsInStockFrom);
    }

    private static Specification<Product> hasUnitsInStockLessThanOrEqual(Integer unitsInStockTo) {
        return (root, query, criteriaBuilder) -> unitsInStockTo == null ?
                criteriaBuilder.conjunction() :
                criteriaBuilder.lessThanOrEqualTo(root.get(UNITS_IN_STOCK), unitsInStockTo);
    }




}
