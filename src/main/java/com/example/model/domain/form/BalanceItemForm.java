package com.example.model.domain.form;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class BalanceItemForm implements Serializable {

    @Serial
    private static final long serialVersionUID = -2940816460129183009L;
    private int id;
    @NotBlank(message = "Enter Item Name")
    private String item;
    @Min(value = 1, message = "Enter Price")
    private int unitPrice;
    @Min(value = 1, message = "Enter Quantity")
    private int quantity;

    private boolean deleted;



}
