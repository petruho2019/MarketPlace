package com.petruho.spring.project.marketplace.service;


import com.petruho.spring.project.marketplace.model.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class ItemServiceTest {

    @Autowired
    private ItemService itemService;


    @Test
    public void findAllItemsTest() {
        List<Item> items = new ArrayList<>();
        items.add(new Item(1L,"Computer",BigDecimal.valueOf(15000).setScale(2),2L));
        items.add(new Item(2L,"Headphones",BigDecimal.valueOf(7000).setScale(2),3L));
        items.add(new Item(3L,"Telephone",BigDecimal.valueOf(10000).setScale(2),4L));
        items.add(new Item(4L,"Fen",BigDecimal.valueOf(5000).setScale(2),1L));

        Assertions.assertEquals(items, itemService.findAllItems());
    }

    @Test
    public void findItemByNameTest() {
        Item item = new Item(1L,"Computer",BigDecimal.valueOf(15000).setScale(2),2L);

        Assertions.assertEquals(item, itemService.findItemByName("Computer"));
    }
}
