package com.example.model.domain.entity;

import com.example.model.domain.form.BalanceItemForm;
import com.example.model.domain.form.BalanceSummaryForm;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
public class Balance implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;



    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private String category;

    @ManyToOne(optional = false, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private User user;

    @OneToMany(mappedBy = "balance", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<BalanceItem> items;

    private Type type;

    public enum Type {
        INCOME,
        EXPENSE
    }

    public Balance() {
        items = new ArrayList<>();
    }

    public void addItem(BalanceItem item) {
        item.setBalance(this);
        items.add(item);
    }

}
