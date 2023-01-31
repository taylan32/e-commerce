package com.example.ecommerce.service;

import com.example.ecommerce.dto.converter.OrderDtoConverter;
import com.example.ecommerce.dto.order.CreateOrderRequest;
import com.example.ecommerce.dto.order.OrderDto;
import com.example.ecommerce.exception.BaseException;
import com.example.ecommerce.model.Order;
import com.example.ecommerce.model.Product;
import com.example.ecommerce.model.User;
import com.example.ecommerce.model.enums.OrderStatus;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.service.filter.OrderFilter;
import com.example.ecommerce.service.filter.OrderSpec;
import com.example.ecommerce.utils.BasePageableModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDtoConverter converter;
    private final UserService userService;
    private final ProductService productService;

    public OrderService(OrderRepository orderRepository,
                        OrderDtoConverter converter,
                        UserService userService,
                        ProductService productService) {
        this.orderRepository = orderRepository;
        this.converter = converter;
        this.userService = userService;
        this.productService = productService;
    }

    public OrderDto createOrder(CreateOrderRequest request) {

        User user = userService.findUserInContext();
        List<Product> products = new ArrayList<>();
        request.getProductIds().forEach(productId -> {
            products.add(productService.findProductById(productId));
        });

        Order order = new Order();
        order.setUser(user);
        order.setProducts(products);
        order.setOrderStatus(OrderStatus.WAITING);

        return converter.convert(orderRepository.save(order));
    }

    protected Order findOrderById(Long id) {
        Optional<Order> order = orderRepository.findById(id);

        if(!order.isPresent()) {
            throw BaseException.builder()
                    .httpStatus(HttpStatus.NOT_FOUND)
                    .errorMessage("Requested order not found")
                    .build();
        }

        if(!(order.get().getUser().getId().equals(userService.findUserInContext().getId()))) {
            throw BaseException.builder()
                    .errorMessage("You do not have permission to perform this operation.")
                    .httpStatus(HttpStatus.FORBIDDEN)
                    .build();
        }

        return order.get();
    }

    public OrderDto getOrderById(Long id) {
        return converter.convert(findOrderById(id));
    }

    public BasePageableModel<OrderDto> getAllOrder(int pageNumber, int pageSize) {

        User user = userService.findUserInContext();
        Specification<Order> specification = OrderSpec.filterBy(new OrderFilter(user.getId()));

        Page<Order> data = orderRepository.findAll(specification, PageRequest.of(pageNumber - 1, pageSize));
        return new BasePageableModel<OrderDto>(
                data,
                data.getContent()
                        .stream()
                        .map(converter::convert)
                        .collect(Collectors.toList())
        );
    }



}
