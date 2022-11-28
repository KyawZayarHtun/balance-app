package com.example.controller.utils;

import com.example.model.BalanceAppException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class BalanceAppExceptionHandler {

    @ExceptionHandler(value = BalanceAppException.class)
    String handle(BalanceAppException e, HttpServletRequest req) {
        RequestContextUtils.getOutputFlashMap(req).put("message", e.getMessage());
        return "redirect:/";
    }



}
