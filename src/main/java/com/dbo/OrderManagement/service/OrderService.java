package com.dbo.OrderManagement.service;

import com.dbo.OrderManagement.dto.OrderRequest;
import com.dbo.OrderManagement.dto.OrderResponse;
import com.dbo.OrderManagement.entity.Order;
import com.dbo.OrderManagement.entity.Product;
import com.dbo.OrderManagement.enums.Status;
import com.dbo.OrderManagement.exceptions.OrderNotFoundException;
import com.dbo.OrderManagement.repository.OrderRepository;
import com.dbo.OrderManagement.repository.ProductRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;



@Service
public class OrderService {

    private final OrderRepository orderRepository;

    private final ProductRepository productRepository;

    private static final Logger logger= LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderRepository orderRepository,ProductRepository productRepository){
        this.orderRepository=orderRepository;
        this.productRepository=productRepository;
    }

    @Transactional(rollbackOn=Exception.class)
    public OrderResponse createOrder(OrderRequest orderRequest){
        logger.info("Starting the transaction");

        logger.info("Creating order for userId: {}", orderRequest.getUserId());

        Product product=productRepository.findById(orderRequest.getProductId())
                .orElseThrow(()->new RuntimeException("Product not found"));

        if(product.getStock()<orderRequest.getQuantity()){
            throw new RuntimeException("insufficient stock");
        }

        product.setStock(product.getStock()-orderRequest.getQuantity());
        productRepository.save(product);


        Order order=Order.builder()
                .quantity(orderRequest.getQuantity())
                .productId(orderRequest.getProductId())
                .userId(orderRequest.getUserId())
                .status(Status.CREATED)
                .build();

        Order saved=orderRepository.save(order);

        logger.info("Order created successfully with the id: {}",saved.getId());

        OrderResponse orderResponse=new OrderResponse();

        orderResponse.setId(saved.getId());
        orderResponse.setQuantity(saved.getQuantity());
        orderResponse.setProductId(saved.getProductId());
        orderResponse.setUserId(saved.getUserId());
        orderResponse.setStatus(saved.getStatus());



        return orderResponse;
    }

    public OrderResponse getOrderById(Long id){

        logger.info("Fetching order with id: {} ",id);
        Order order=orderRepository.findById(id)
                .orElseThrow(()->{
                    logger.error("Order not found with id : {}",id);
                    return new OrderNotFoundException("Order Not Found");
                });

        OrderResponse orderResponse=new OrderResponse();

        orderResponse.setId(order.getId());
        orderResponse.setQuantity(order.getQuantity());
        orderResponse.setProductId(order.getProductId());
        orderResponse.setUserId(order.getUserId());
        orderResponse.setStatus(order.getStatus());

        return orderResponse;


    }

    public Page<OrderResponse> getAllOrders(int page,int size){
        Pageable pageable= PageRequest.of(page,size);

        logger.info("getting all the order in the form of pages");

        Page<Order> orders=orderRepository.findAll(pageable);

        return orders.map(this::mapToResponse);
    }

    public Page<OrderResponse> getAllOrdersBySorting(int page,int size,String sortBy){
        Pageable pageable= PageRequest.of(page,size, Sort.by(sortBy).descending());

        logger.info("getting all the order in the form of pages");

        Page<Order> orders=orderRepository.findAll(pageable);

        return orders.map(this::mapToResponse);
    }

    public OrderResponse mapToResponse(Order order){
        return new OrderResponse(order.getId(),order.getProductId(),order.getUserId(),order.getQuantity(),order.getStatus());
    }



}
