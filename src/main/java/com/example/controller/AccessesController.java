package com.example.controller;

import com.example.controller.utils.Pagination;
import com.example.model.domain.entity.UserAccessLog.Type;
import com.example.model.service.UserAccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Optional;

@Controller
@RequestMapping("/admin/accesses")
public class AccessesController {

    @Autowired
    private UserAccessLogService logService;


    @GetMapping
    String search(
            @RequestParam(required = false) Type type,
            @RequestParam(required = false) String username,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            ModelMap model) {

        var result = logService.searchAdmin(type, username, date, page, size);
        model.put("list", result.getContent());

        var params = new HashMap<String, String>();
        params.put("username", StringUtils.hasLength(username) ? username : "");
        params.put("type", type == null ? "" : type.name());
        params.put("date", date == null ? "" :
                date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        size.ifPresent(a -> params.put("size", a.toString()));

        model.put("pager",
                Pagination.builder("/admin/accesses").page(result).param(params).build());
        return "access-logs";
    }

    @ModelAttribute(name = "types")
    Type[] types() {
        return Type.values();
    }

}
