package com.example.controller;

import com.example.controller.utils.Pagination;
import com.example.model.domain.entity.Balance;
import com.example.model.service.BalanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.example.model.domain.entity.Balance.*;

@Controller
@RequestMapping("user/balance")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @Autowired
    DateTimeFormatter df;


    @GetMapping
    String report(
            ModelMap model,
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Optional<Integer> size,
            @RequestParam(required = false) Optional<Integer> page
    ) {

        var result = balanceService.searchReport(type, dateFrom, dateTo, keyword, page, size);
        var pagination = Pagination.builder("/user/balance")
                .param(Map.of(
                        "type", type == null ? "" : type.name(),
                        "dateFrom", dateFrom == null ? "" : dateFrom.format(df),
                        "dateTo", dateTo == null ? "" : dateTo.format(df)
                ))
                .page(result)
                .build();

        model.put("pagination", pagination);
        model.put("list", result.getContent());

        return "balance-report";
    }

    @GetMapping("{type}")
    public String searchBalanceItems(
            ModelMap model,
            @PathVariable Type type,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Optional<Integer> size,
            @RequestParam(required = false) Optional<Integer> page) {

        model.put("title", "%s Management".formatted(type));
        model.put("type", type);

        var result = balanceService.searchItems(type, dateFrom, dateTo, keyword, page, size);
        model.put("list", result.getContent());

        var param = new HashMap<String, String>();
        param.put("type", type.name());
        param.put("dateFrom", dateFrom == null ? "" :
                dateFrom.format(df));
        param.put("dateTo", dateTo == null ? "" :
                dateTo.format(df));
        param.put("keyword", keyword != null ? "" : keyword);

        var pagination = Pagination
                .builder("/user/balance/%s".formatted(type))
                .page(result)
                .param(param)
                .build();

        model.put("pager", pagination);

        return "balance-list";
    }

    @GetMapping("details/{id:\\d+}")
    public String findById(@PathVariable int id, ModelMap model) {
        model.put("vo", balanceService.findById(id));
        return "balance-detail";
    }

    @GetMapping("delete/{id:\\d+}")
    public String delete(@PathVariable int id) {
        balanceService.deleteById(id);
        return "redirect:/";
    }

    @ModelAttribute("balanceTypes")
    Type[] types() {
        return Type.values();
    }


}
