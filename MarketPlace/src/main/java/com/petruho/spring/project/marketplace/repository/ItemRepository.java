package com.petruho.spring.project.marketplace.repository;


import com.petruho.spring.project.marketplace.model.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Item findItemBySubject(String name);
}
