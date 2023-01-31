package com.example.ecommerce.service.filter;

import com.example.ecommerce.model.Order;
import org.springframework.data.jpa.domain.Specification;

public class OrderSpec {
    private static final String USER = "user";

    private OrderSpec() {}

    public static Specification<Order> filterBy(OrderFilter orderFilter) {
        return Specification.where(hasUser(orderFilter.getUserId()));
    }

    public static Specification<Order> hasUser(Long userId) {
        return (root, query, criteriaBuilder) -> criteriaBuilder.equal(root.join(USER).get("id"), userId);
    }

}
