package com.petruho.spring.project.marketplace.DTO;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ItemDTO {
    private String subject;
    private Long quantity;
    private BigDecimal price;

    public ItemDTO(String subject, Long quantity, BigDecimal price) {
        this.subject = subject;
        this.quantity = quantity;
        this.price = price;
    }
}
