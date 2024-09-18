package com.petruho.spring.project.marketplace.controller;


import com.petruho.spring.project.marketplace.DTO.ItemDTO;
import com.petruho.spring.project.marketplace.DTO.OrderRequestDTO;
import com.petruho.spring.project.marketplace.kafka.KafkaProducer;
import com.petruho.spring.project.marketplace.model.Order;
import com.petruho.spring.project.marketplace.service.ItemService;
import com.petruho.spring.project.marketplace.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final ItemService itemService;
    private final OrderService orderService;
    private final Order order;
    private final KafkaProducer kafkaProducer;

    private String currentOrderStatus;

    @GetMapping("/create")
    public String order(Model model) {
        order.setOrder(orderService.clearOrder(order.getOrder()));
        currentOrderStatus = null;

        model.addAttribute("order", order);
        model.addAttribute("orderService", itemService);
        model.addAttribute("itemsInOrder",order.getAllItemInOrder());
        return "show";
    }

    @ResponseBody
    @PostMapping("/add")
    public ResponseEntity<?> addItemToOrder(@RequestBody OrderRequestDTO orderRequest) {
        if (orderRequest.getNameOfItem() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        return orderService.addItemToOrder(orderRequest);
    }

    @ResponseBody
    @GetMapping("/show")
    public ResponseEntity<List<ItemDTO>> showOrder() {
       return orderService.showOrder();
    }

    @ResponseBody
    @PostMapping("/sendOrder")
    public ResponseEntity<?> sendOrder() {

        kafkaProducer.sendOrder(order);
        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    public void changeOrderStatus(String orderStatus) {
        this.currentOrderStatus = orderStatus;
    }

    @ResponseBody
    @GetMapping("/getStatus")
    public ResponseEntity<?> getOrderStatus() {
        return ResponseEntity.status(HttpStatus.OK).body(currentOrderStatus);
    }

}
