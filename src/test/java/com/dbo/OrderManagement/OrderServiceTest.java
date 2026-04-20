package com.dbo.OrderManagement;

import com.dbo.OrderManagement.dto.OrderRequest;
import com.dbo.OrderManagement.dto.OrderResponse;
import com.dbo.OrderManagement.entity.Order;
import com.dbo.OrderManagement.entity.Product;
import com.dbo.OrderManagement.repository.OrderRepository;
import com.dbo.OrderManagement.repository.ProductRepository;
import com.dbo.OrderManagement.service.OrderService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.parameters.P;

import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    public void TestCreateOrder(){

        OrderRequest orderRequest=new OrderRequest();
        orderRequest.setProductId(1L);
        orderRequest.setQuantity(2);
        orderRequest.setUserId(1L);

        Product product=new Product();
        product.setStock(10);
        product.setId(1L);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        when(orderRepository.save(any(Order.class))).thenAnswer(invocation->invocation.getArgument(0));

        OrderResponse orderResponse=orderService.createOrder(orderRequest);

        assertNotNull(orderResponse);
        assertEquals(8,product.getStock());


    }

    public void TestCreateOrderFailure(){

        OrderRequest orderRequest=new OrderRequest();
        orderRequest.setQuantity(5);
        orderRequest.setUserId(1L);
        orderRequest.setProductId(1L);

        Product product=new Product();
        product.setId(1L);
        product.setStock(2);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        RuntimeException ex=assertThrows(RuntimeException.class,()->orderService.createOrder(orderRequest));

        assertEquals("insufficient stock",ex.getMessage());

    }


}
