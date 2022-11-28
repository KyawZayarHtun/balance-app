package com.example.model.service;

import com.example.model.domain.entity.UserAccessLog;
import com.example.model.repo.UserAccessLogRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserAccessLogService {

    @Autowired
    private UserAccessLogRepo logRepo;

    @Autowired
    private Integer defaultPageSize;


    public Page<UserAccessLog> search(String username, Optional<Integer> page, Optional<Integer> size) {

        var pageInfo = PageRequest
                .of(page.orElse(0), size.orElse(defaultPageSize))
                .withSort(Sort.by("accessTime").descending());

        Specification<UserAccessLog> spec = (root, query, builder) ->
                builder.equal(root.get("username"), username);

        return logRepo.findAll(spec, pageInfo);
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    public Page<UserAccessLog> searchAdmin(UserAccessLog.Type type, String username, LocalDate date, Optional<Integer> page, Optional<Integer> size) {

        var pageInfo = PageRequest
                .of(page.orElse(0), size.orElse(defaultPageSize))
                .withSort(Sort.by("accessTime").descending());

        Specification<UserAccessLog> spec = Specification.where(null);

        if (type != null) {
            spec = spec.and((root, query, builder) -> builder.equal(root.get("type"), type));
        }

        if (StringUtils.hasLength(username)) {
            spec =
                    spec.and((root, query, builder) -> builder.like(builder.lower(root.get(
                            "username")), username.toLowerCase().concat("%")));
        }

        if (date != null) {
            spec =
                    spec.and((root, query, builder) -> builder.greaterThanOrEqualTo(root.get("accessTime"),
                            date.atStartOfDay()));
        }
        return logRepo.findAll(spec, pageInfo);
    }
}