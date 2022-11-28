package com.example.model.domain.form;

import com.example.model.domain.entity.Balance;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class BalanceSummaryForm implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;
    private int id;
    @NotNull(message = "Enter Date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @NotBlank(message = "Enter Category")
    private String category;
    private Balance.Type type;

}
