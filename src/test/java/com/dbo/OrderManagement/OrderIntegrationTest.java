package com.dbo.OrderManagement;


import com.dbo.OrderManagement.entity.Product;
import com.dbo.OrderManagement.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

//import static org.springframework.mock.http.server.reactive.MockServerHttpRequest.post;
//import static jdk.javadoc.internal.doclint.HtmlTag.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setup() {
        Product product = new Product();

        product.setName("Test Product");
        product.setPrice(100);
        product.setStock(10);

        productRepository.save(product);
    }

    @Test
    void shouldCreateOrder() throws Exception {

        String request = """
        {
          "userId": 1,
          "productId": 1,
          "quantity": 2
        }
    """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.quantity").value(2));
    }

    @Test
    void shouldFailWhenInvalidInput() throws Exception {

        String request = """
        {
          "userId": null,
          "productId": 1,
          "quantity": 0
        }
    """;

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldReturnNotFound() throws Exception {

        mockMvc.perform(get("/api/orders/999"))
                .andExpect(status().isNotFound());
    }


}
