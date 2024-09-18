package com.petruho.spring.project.marketplace.service;


import com.petruho.spring.project.marketplace.DTO.OrderRequestDTO;
import com.petruho.spring.project.marketplace.error.IncorrectOrderException;
import com.petruho.spring.project.marketplace.error.NotEnoughElementsOrderException;
import com.petruho.spring.project.marketplace.model.Item;
import com.petruho.spring.project.marketplace.model.Order;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

import static org.mockito.Mockito.when;

@SpringBootTest
class OrderServiceTest {

    @Autowired
    private Order order;

    @Mock
    private ItemService itemServiceMock;


    @InjectMocks
    private OrderService orderService;

    @Test
    public void addItemToOrderQuantityIsZeroTest(){
        Assertions.assertThrows(IncorrectOrderException.class,() ->{
            OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
            orderRequestDTO.setQuantity(0);
            orderRequestDTO.setNameOfItem("Telephone");

            orderService.addItemToOrder(orderRequestDTO);
        });
    }

    @Test
    public void addItemToOrderQuantityIsNegativeTest(){
        Assertions.assertThrows(IncorrectOrderException.class,() ->{
            OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
            orderRequestDTO.setQuantity(-1);
            orderRequestDTO.setNameOfItem("Telephone");

            orderService.addItemToOrder(orderRequestDTO);
        });
    }

    @Test
    public void addItemToOrderNoItemInOrderAndQuantityMoreThanInDBTest(){
        when(itemServiceMock.findItemByName("Fen")).thenReturn(new Item(4,"Fen",BigDecimal.valueOf(5000),1L));

        order = new Order();

        Assertions.assertThrows(NotEnoughElementsOrderException.class,() ->{
            OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
            orderRequestDTO.setQuantity(2);
            orderRequestDTO.setNameOfItem("Fen");
            orderService.addItemToOrder(orderRequestDTO);
        });
    }

    @Test
    public void addItemToOrderWhereItemInOrderAndQuantityMoreThanInDBTest(){
        when(itemServiceMock.findItemByName("Computer")).thenReturn(new Item(1,"Computer",BigDecimal.valueOf(15000),2L));

        order.getOrder().put(new Item(1,"Computer", BigDecimal.valueOf(15000), 2L),1L);

        OrderRequestDTO orderRequestDTO = new OrderRequestDTO();
        orderRequestDTO.setQuantity(2);
        orderRequestDTO.setNameOfItem("Computer");

        Assertions.assertThrows(NotEnoughElementsOrderException.class,() ->{
            orderService.addItemToOrder(orderRequestDTO);
        });

        Mockito.verify(itemServiceMock, Mockito.times(1)).findItemByName("Computer");
    }

}
