package com.petruho.spring.project.marketplace.repository;


import com.petruho.spring.project.marketplace.model.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void findItemBySubjectTest() {
        Item item = new Item(1, "Computer", BigDecimal.valueOf(15000).setScale(2), 2L);

        Assertions.assertEquals(item, itemRepository.findItemBySubject("Computer"));
    }
}
