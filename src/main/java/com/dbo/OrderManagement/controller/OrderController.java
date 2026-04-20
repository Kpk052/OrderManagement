package com.dbo.OrderManagement.controller;

import com.dbo.OrderManagement.dto.OrderRequest;
import com.dbo.OrderManagement.dto.OrderResponse;
import com.dbo.OrderManagement.entity.Order;
import com.dbo.OrderManagement.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService){
        this.orderService=orderService;
    }

    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> createOrder(@Valid @RequestBody OrderRequest orderRequest){
        OrderResponse orderResponse=orderService.createOrder(orderRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(orderResponse);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable Long id){
        OrderResponse orderResponse=orderService.getOrderById(id);

        return ResponseEntity.status(HttpStatus.FOUND).body(orderResponse);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/orders")
    public ResponseEntity<Page<OrderResponse>> getAllOrders(@RequestParam int page,@RequestParam int size){

        return ResponseEntity.status(HttpStatus.FOUND).body(orderService.getAllOrders(page,size));

    }

    @GetMapping("/orders/sort")
    public ResponseEntity<Page<OrderResponse>> getAllOrdersBySort(@RequestParam(defaultValue = "0") int page,@RequestParam(defaultValue = "5") int size,@RequestParam(defaultValue = "id") String sortBy){

        return ResponseEntity.status(HttpStatus.FOUND).body(orderService.getAllOrdersBySorting(page, size, sortBy));

    }




}
