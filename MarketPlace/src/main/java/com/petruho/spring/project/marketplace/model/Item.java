package com.petruho.spring.project.marketplace.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "t_item")
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class Item {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Setter
    private String subject;

    @Setter
    private BigDecimal price;

    @Setter
    private Long amount;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return id == item.id && Objects.equals(subject, item.subject) && Objects.equals(price, item.price) && Objects.equals(amount, item.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, price, amount);
    }

    @Override
    public String toString() {
        return "Item{" +
                "id=" + id +
                ", subject='" + subject + '\'' +
                ", price=" + price +
                ", amount=" + amount +
                '}';
    }
}
