package com.petruho.spring.project.marketplace.DTO;


import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
public class OrderRequestDTO {

    private String nameOfItem;

    private long quantity;
}
