package com.example.controller.utils;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class Pagination {

    private Pagination(int current, int total, boolean first, boolean last, String url,
                       Map<String, String> params) {
        this.current = current;
        this.total = total;
        this.first = first;
        this.last = last;
        this.url = url;
        this.params = params;

        if (params == null) {
            params = new HashMap<>();
        }
        pages = new ArrayList<>();
        pages.add(current);

        while (pages.size() < 3 && pages.get(0) > 0) {
            pages.add(0, pages.get(0) - 1);
        }

        while (pages.size() < 5 && pages.get(pages.size() - 1) < total - 1) {
            pages.add(pages.get(pages.size() - 1) + 1);
        }

        while (pages.size() < 5 && pages.get(0) > 0) {
            pages.add(0, pages.get(0) - 1);
        }

    }

    public String getParam() {
        return params.entrySet().stream().map(a -> "%s=%s".formatted(a.getKey(), a.getValue()))
                .reduce("", (a, b) -> "%s&%s".formatted(a, b));
    }

    public static Builder builder(String url) {
        return new Builder(url);
    }

    public static class Builder {
        public Builder(String url) {
            this.url = url;
        }

        public <T> Builder page(Page<T> page) {
            this.current = page.getNumber();
            this.total = page.getTotalPages();
            this.first = page.isFirst();
            this.last = page.isLast();
            return this;
        }

        private int current;
        private int total;
        private boolean first;
        private boolean last;
        private String url;
        private Map<String, String> params;


        public Builder current(int current) {
            this.current = current;
            return this;
        }

        public Builder total(int total) {
            this.total = total;
            return this;
        }

        public Builder last(Boolean last) {
            this.last = last;
            return this;
        }

        public Builder first(Boolean first) {
            this.first = first;
            return this;
        }

        public Builder param(Map<String, String> params) {
            this.params = params;
            return this;
        }

        public Pagination build() {
            return new Pagination(current, total, first, last, url, params);
        }
    }

    private int current;
    private int total;
    private boolean first;
    private boolean last;
    private String url;
    private List<Integer> pages;
    private Map<String, String> params;

    public boolean isShow() {
        return pages.size() > 1;
    }

}
