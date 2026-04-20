package com.dbo.OrderManagement.dto;

import com.dbo.OrderManagement.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private Long userId;
    private Long productId;
    private int quantity;
    private Status status;

}
