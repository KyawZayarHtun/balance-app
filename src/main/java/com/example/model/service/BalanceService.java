package com.example.model.service;

import com.example.model.domain.entity.Balance;
import com.example.model.domain.entity.BalanceItem;
import com.example.model.domain.form.BalanceEditForm;
import com.example.model.domain.vo.BalanceReportVo;
import com.example.model.repo.BalanceItemRepo;
import com.example.model.repo.BalanceRepo;
import com.example.model.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class BalanceService {

    @Autowired
    private BalanceItemRepo itemRepo;

    @Autowired
    private BalanceRepo balanceRepo;

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private Integer defaultPageSize;

    @PreAuthorize("isAuthenticated()")
    public Page<BalanceItem> searchItems(Balance.Type type, LocalDate dateFrom, LocalDate dateTo, String keyword, Optional<Integer> page, Optional<Integer> size) {

        var pageInfo = PageRequest.of(page.orElse(0), size.orElse(defaultPageSize));
        var username = SecurityContextHolder.getContext().getAuthentication().getName();

        Specification<BalanceItem> spec = (root, query, builder) ->
                builder.equal(root.get("balance").get("user").get("loginId"),
                        username);

        spec = spec.and((root, query, builder) -> builder.equal(root.get("balance").get("type"),
                type));
        spec = spec.and((root, query, builder) -> builder.equal(root.get("balance").get("type"),
                type));

        // dateFrom
        if (dateFrom != null) {
            spec = spec.and((root, query, builder) ->
                    builder.greaterThanOrEqualTo(root.get("balance").get("date"), dateFrom));
        }
        // dateTo
        if (dateTo != null) {
            spec = spec.and((root, query, builder) ->
                    builder.lessThanOrEqualTo(root.get("balance").get("date"), dateTo));
        }
        // keyword
        if (StringUtils.hasLength(keyword)) {
            Specification<BalanceItem> category =
                    (root, query, builder) -> builder.like(builder.lower(root.get("balance").get("category")),
                            "%%%s%%".formatted(keyword.toLowerCase()));
            Specification<BalanceItem> item =
                    (root, query, builder) -> builder.like(builder.lower(root.get("item")),
                            "%%%s%%".formatted(keyword.toLowerCase()));

            spec = spec.and(item.or(category));
        }

        return itemRepo.findAll(spec, pageInfo);

    }

    public BalanceEditForm findById(Integer id) {
        return balanceRepo.findById(id).map(BalanceEditForm::new).orElseThrow();
    }

    @Transactional
    public int save(BalanceEditForm form) {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();

        var user = userRepo.findOneByLoginId(username).orElseThrow();

        var balance = form.getHeader().getId() == 0 ? new Balance() :
                balanceRepo.findById(form.getHeader().getId()).orElseThrow();

        balance.setUser(user);
        balance.setCategory(form.getHeader().getCategory());
        balance.setDate(form.getHeader().getDate());
        balance.setType(form.getHeader().getType());

        balance = balanceRepo.save(balance);

        for (var formItem : form.getItems()) {
            var item = formItem.getId() == 0 ? new BalanceItem() :
                    itemRepo.findById(formItem.getId()).orElseThrow();

            if (formItem.isDeleted()) {
                itemRepo.delete(item);
                continue;
            }

            item.setItem(formItem.getItem());
            item.setUnitPrice(formItem.getUnitPrice());
            item.setQuantity(formItem.getQuantity());
            item.setBalance(balance);
            itemRepo.save(item);
        }

        return balance.getId();
    }

    @Transactional
    public void deleteById(int id) {
        balanceRepo.deleteById(id);
    }

    @PreAuthorize("isAuthenticated()")
    public Page<BalanceReportVo> searchReport(Balance.Type type, LocalDate dateFrom, LocalDate dateTo, String keyword
            , Optional<Integer> page, Optional<Integer> size) {

        Specification<Balance> spec =
                (root, query, builder) -> builder.equal(root.get("user").get("loginId"),
                        SecurityContextHolder.getContext().getAuthentication().getName());

        if (type != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("type"), type));
        }

        if (dateFrom != null) {
            spec = spec.and((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("date"), dateFrom));
        }

        if (dateTo != null) {
            spec = spec.and((root, query, builder) -> builder.lessThanOrEqualTo(root.get("date"), dateTo));
        }

        var pageInfo = PageRequest.of(page.orElse(0), size.orElse(defaultPageSize));

        var result = balanceRepo.findAll(spec, pageInfo).map(BalanceReportVo::new);

        if (!result.getContent().isEmpty()) {
            var firstId = result.getContent().get(0).getId();

            var lastIncome = itemRepo.getLastBalance(firstId, Balance.Type.INCOME)
                    .map(Number::intValue).orElse(0);

            var lastExpense = itemRepo.getLastBalance(firstId, Balance.Type.EXPENSE)
                    .map(Number::intValue).orElse(0);

            var lastBalance = lastIncome - lastExpense;



            for(var vo: result.getContent()) {
                if (vo.getType() == Balance.Type.INCOME) {
                    lastBalance += vo.getAmount();
                } else {
                    lastBalance -= vo.getAmount();
                }

                vo.setBalance(lastBalance);
            }
        }

        return result;
    }
}
