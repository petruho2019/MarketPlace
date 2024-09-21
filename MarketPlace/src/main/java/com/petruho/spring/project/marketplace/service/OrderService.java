package com.petruho.spring.project.marketplace.service;


import com.petruho.spring.project.marketplace.DTO.ItemDTO;
import com.petruho.spring.project.marketplace.DTO.OrderRequestDTO;
import com.petruho.spring.project.marketplace.error.IncorrectOrderException;
import com.petruho.spring.project.marketplace.error.NotEnoughElementsOrderException;
import com.petruho.spring.project.marketplace.model.Item;
import com.petruho.spring.project.marketplace.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final  Order order;
    private final ItemService itemService;

    public Map<Item,Long> clearOrder(Map<Item, Long> mapOrder) {
        mapOrder.clear();
        return mapOrder;
    }

    private void validate(Item item, long quantity){

        // Если нажал на добавить 0 определенного элемента
        if (quantity == 0 || quantity < 0){
            throw new IncorrectOrderException("Incorrect input");
        }

        // Если добавляемое кол-во товара, больше чем есть в бд
        if (item.getAmount() <= quantity){
            throw new NotEnoughElementsOrderException("Not enough elements");
        }
    }

    public ResponseEntity<Order> addItemToOrder(OrderRequestDTO orderRequest){

        String nameOfItem = orderRequest.getNameOfItem();

        // Получаем Item по имени
        Item item = itemService.findItemByName(nameOfItem);

        Long quantity = orderRequest.getQuantity();

        validate(item, quantity);

        // Добавляем элемент, если его нет в заказе
        if (order.getOrder().get(item) == null){
            order.getOrder().put(item, quantity);

            return ResponseEntity.status(HttpStatus.OK).body(order);
        }
        // Если item уже есть в заказе с определенном количеством, и user хочет добавить некоторое кол-во товара которое превышает содержимое в бд
        if (item.getAmount() < order.getOrder().get(item) + quantity || !(order.getOrder().containsKey(item))) {
            throw new NotEnoughElementsOrderException("There is no such quantity of goods");
        }


        return addQuantityToOrder(item,quantity);
    }



    private ResponseEntity<Order> addQuantityToOrder(Item item, /* Добавляемое кол-во */Long quantity){
        // Берем количество которое уже есть в заказе
        Long quantityInOrder = order.getOrder().get(item);

        // Удаляем бакет
        order.getOrder().remove(item);

        // Новый бакет с новым количеством
        order.getOrder().put(item, quantity + quantityInOrder);

        return ResponseEntity.status(HttpStatus.OK).body(order);
    }

    public ResponseEntity<List<ItemDTO>> showOrder(){

        // Получаем заказ
        Map<Item, Long> orderItems = order.getOrder();

        // Здесь будут хранится все items из заказа
        List<ItemDTO> orderItemsDTO = new ArrayList<>();

        for (Map.Entry<Item, Long> entry : orderItems.entrySet()) {

            // Берем все items из нынешнего заказа
            Item item = entry.getKey();

            // И их количество
            Long quantity = entry.getValue();

            // Помещаем в ArrayList, и устанавливаем их количество
            ItemDTO itemDTO = new ItemDTO(item.getSubject(), quantity, item.getPrice());
            orderItemsDTO.add(itemDTO);
        }

        // Возвращаем, и обрабатываем на уровне пользователя
        return ResponseEntity.ok().contentType(MediaType.APPLICATION_JSON).body(orderItemsDTO);
    }
}
