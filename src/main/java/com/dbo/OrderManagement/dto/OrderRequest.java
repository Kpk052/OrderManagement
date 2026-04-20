package com.dbo.OrderManagement.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class OrderRequest {

    @NotNull(message = "UserId is required")
    private Long userId;

    @NotNull(message = "ProductId is Required")
    private Long productId;

    @Min(value = 1,message = "Quantity must be at least 1")
    private int quantity;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
