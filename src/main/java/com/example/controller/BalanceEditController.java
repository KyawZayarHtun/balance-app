package com.example.controller;

import com.example.model.BalanceAppException;
import com.example.model.domain.entity.Balance.Type;
import com.example.model.domain.form.BalanceEditForm;
import com.example.model.domain.form.BalanceItemForm;
import com.example.model.domain.form.BalanceSummaryForm;
import com.example.model.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("user/balance-edit")
@SessionAttributes("balanceEditForm")
public class BalanceEditController {

    @Autowired
    private BalanceService service;


    @GetMapping()
    public String edit(@ModelAttribute(name = "balanceEditForm") BalanceEditForm form,
                       @RequestParam(required = false) Integer id,
                       @RequestParam(required = false) Type type) {

        if (id != null && form.getHeader().getId() != id) {
            var result = service.findById(id);
            form.setHeader(result.getHeader());
            form.setItems(result.getItems());
        }

        if (type != null && form.getHeader().getType() != type) {
            form.setHeader(new BalanceSummaryForm());
            form.getHeader().setType(type);
            form.getItems().clear();
        }

        return "balance-edit";
    }


    @PostMapping("item")
    public String addItem(@ModelAttribute(name = "balanceEditForm") BalanceEditForm form,
                          @ModelAttribute(name = "itemForm") @Valid BalanceItemForm itemForm,
                          BindingResult error) {
        if (error.hasErrors()) {
            return "balance-edit";
        }
        form.getItems().add(itemForm);


        return "redirect:/user/balance-edit?%s".formatted(form.queryParam());
    }

    @GetMapping("item")
    public String deleteItem(@ModelAttribute(name = "balanceEditForm") BalanceEditForm form,
                             @RequestParam int index) {
        var item = form.getItems().get(index);

        if (item.getId() == 0) {
            form.getItems().remove(item);
        } else {
            item.setDeleted(true);
        }

        return "redirect:/user/balance-edit?%s".formatted(form.queryParam());
    }


    @GetMapping("confirm")
    public String confirm(@ModelAttribute(name = "balanceEditForm") BalanceEditForm form) {
        return "balance-edit-confirm";
    }

    @PostMapping()
    public String save(@ModelAttribute(name = "balanceEditForm") BalanceEditForm form,
                       @ModelAttribute(name = "summeryForm") @Valid BalanceSummaryForm summaryForm,
                       BindingResult errors) {

        if (errors.hasErrors()) {
            return "balance-edit-confirm";
        }

        form.getHeader().setCategory(summaryForm.getCategory());
        form.getHeader().setDate(summaryForm.getDate());

        var id = service.save(form);
        form.clear();
        return "redirect:/user/balance/details/%d".formatted(id);
    }

    @ModelAttribute(name = "itemForm")
    public BalanceItemForm itemForm() {
        return new BalanceItemForm();
    }

    @ModelAttribute(name = "balanceEditForm")
    public BalanceEditForm form(@RequestParam(required = false) Integer id,
                                @RequestParam(required = false) Type type) {

        if (id != null) {
            return service.findById(id);
        }

        if (type == null) {
            throw new BalanceAppException("Please set type for balance.");
        }

        return new BalanceEditForm().type(type);

    }

    @ModelAttribute(name = "summeryForm")
    BalanceSummaryForm summaryForm(@ModelAttribute(name = "balanceEditForm") BalanceEditForm form) {
        return form.getHeader();
    }


}
