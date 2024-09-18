package com.petruho.spring.project.marketplace.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.petruho.spring.project.marketplace.DTO.ItemDTO;
import com.petruho.spring.project.marketplace.DTO.OrderRequestDTO;
import com.petruho.spring.project.marketplace.model.Item;
import com.petruho.spring.project.marketplace.model.Order;
import com.petruho.spring.project.marketplace.service.ItemService;
import com.petruho.spring.project.marketplace.service.OrderService;
import com.petruho.spring.project.marketplace.kafka.KafkaProducer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.math.BigDecimal;
import java.util.*;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = OrderController.class)
@AutoConfigureMockMvc(addFilters = false)
public class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private OrderService orderService;

    @MockBean
    private KafkaProducer kafkaProducer;

    ObjectMapper objectMapper;

    @MockBean
    private Order order;

    @MockBean
    private ItemService itemService;

    Map<Item,Long> items = new HashMap<>();
    {
        items.put(new Item(1L, "Computer", BigDecimal.valueOf(15000).setScale(2), 2L), 2L);
    }

    @BeforeEach
    public void setUp() {
        objectMapper = new ObjectMapper();
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testShowOrder() throws Exception {

        List<ItemDTO> itemsDTO = new ArrayList<>();
        itemsDTO.add(new ItemDTO("Computer", 2L, BigDecimal.valueOf(15000).setScale(2)));

        // Мокируем сервис для возврата списка товаров
        when(orderService.showOrder()).thenReturn(ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_JSON)
                .body(itemsDTO));

        mockMvc.perform(get("/order/show"))
                .andDo(print())  // Добавьте этот метод для вывода содержимого ответа в консоль
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));


        // Проверяем, что метод сервиса был вызван
        verify(orderService, times(1)).showOrder();
    }


    @Test
    public void testAddItemToOrder_Success() throws Exception {
        // Создаем запрос на добавление товара
        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setNameOfItem("Computer");
        requestDTO.setQuantity(1);

        // Мокируем успешный ответ от сервиса
        when(orderService.addItemToOrder(any(OrderRequestDTO.class)))
                .thenReturn(ResponseEntity.status(HttpStatus.OK).body(order));

        mockMvc.perform(post("/order/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isOk());

        verify(orderService, times(1)).addItemToOrder(any(OrderRequestDTO.class));
    }

    @Test
    public void testAddItemToOrder_InvalidRequest() throws Exception {

        OrderRequestDTO requestDTO = new OrderRequestDTO();
        requestDTO.setQuantity(2);
        // Проверяем невалидный запрос (пустое имя товара)
        mockMvc.perform(post("/order/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDTO)))
                .andExpect(status().isBadRequest());

        // Убеждаемся, что сервис не был вызван при невалидном запросе
        verify(orderService, times(0)).addItemToOrder(any(OrderRequestDTO.class));
    }

    @Test
    public void testSendOrder_Success() throws Exception {

        Map<Item,Long> mapOrder = new HashMap<>();
        mapOrder.put(new Item(),2L);

        order.setOrder(mapOrder);
        order.setQuantity(2L);
        order.setOrderStatus("Success");

        // Мокируем успешный ответ
        doNothing().when(kafkaProducer).sendOrder(any());

        ResultActions resultActions = mockMvc.perform(post("/order/sendOrder")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order))) // добавляем тип контента
                .andExpect(status().isOk());

        resultActions.andExpect(status().isOk());

        verify(kafkaProducer, times(1)).sendOrder(any());

    }
}
