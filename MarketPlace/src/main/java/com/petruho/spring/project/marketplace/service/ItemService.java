package com.petruho.spring.project.marketplace.service;


import com.petruho.spring.project.marketplace.model.Item;
import com.petruho.spring.project.marketplace.repository.ItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemService {

    private ItemRepository itemRepository;

    public List<Item> findAllItems() {
        return itemRepository.findAll();
    }

    public Item findItemByName(String nameOfItem) {
        return itemRepository.findItemBySubject(nameOfItem);
    }
}
