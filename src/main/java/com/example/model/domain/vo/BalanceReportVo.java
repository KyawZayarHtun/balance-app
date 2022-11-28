package com.example.model.domain.vo;

import com.example.BalanceDemoWebConfig;
import com.example.model.domain.entity.Balance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class BalanceReportVo {

    private int id;
    private LocalDate date;
    private Balance.Type type;
    private String category;
    private int amount;
    private int balance;

    public BalanceReportVo(Balance entity) {
        this.id = entity.getId();
        this.date = entity.getDate();
        this.category = entity.getCategory();
        this.type = entity.getType();
        this.amount =
                entity.getItems().stream().mapToInt(a -> a.getQuantity() * a.getUnitPrice()).sum();
    }

    public int getIncome() {
        return type == Balance.Type.INCOME ? amount : 0;
    }

    public int getExpense() {
        return type == Balance.Type.EXPENSE ? amount : 0;
    }
}
