package com.example.model.domain.form;

import com.example.model.domain.entity.Balance;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class BalanceEditForm implements Serializable {

    @Serial
    private static final long serialVersionUID = -4151718936175095847L;
    private BalanceSummaryForm header;
    private List<BalanceItemForm> items;

    public int getTotal() {
        return items.stream().filter(a -> !a.isDeleted()).mapToInt(a -> a.getQuantity() * a.getUnitPrice()).sum();
    }

    public int getTotalCount() {
        return items.stream().mapToInt(a -> a.getQuantity()).sum();
    }

    public BalanceEditForm() {
        header = new BalanceSummaryForm();
        items = new ArrayList<>();
    }

    public BalanceEditForm(Balance entity) {
        header = new BalanceSummaryForm();
        header.setId(entity.getId());
        header.setCategory(entity.getCategory());
        header.setDate(entity.getDate());
        header.setType(entity.getType());

        items = new ArrayList<>(entity.getItems().stream().map(a -> {
            var item = new BalanceItemForm();
            item.setId(a.getId());
            item.setItem(a.getItem());
            item.setQuantity(a.getQuantity());
            item.setUnitPrice(a.getUnitPrice());
            return item;
        }).toList());
    }

    public BalanceEditForm type(Balance.Type type) {
        header.setType(type);
        return this;
    }

    public boolean isShowSaveBtn() {
        return !items.isEmpty();
    }

    public String queryParam() {
        return header.getId() == 0 ?
                "type=%s".formatted(header.getType()) :
                "id=%s".formatted(header.getId());
    }

    public List<BalanceItemForm> getValidItems() {
        return items.stream().filter(a -> !a.isDeleted()).toList();
    }

    public void clear() {
        header = new BalanceSummaryForm();
        items.clear();
    }
}
