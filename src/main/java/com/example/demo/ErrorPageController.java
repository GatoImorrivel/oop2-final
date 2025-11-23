package com.example.demo;

import org.springframework.boot.webmvc.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ErrorPageController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        Object message = request.getAttribute(RequestDispatcher.ERROR_MESSAGE);
        Object exception = request.getAttribute(RequestDispatcher.ERROR_EXCEPTION);

        int statusCode = (status != null) ? (int) status : 500;

        model.addAttribute("status", statusCode);
        model.addAttribute("message", message != null ? message : "An error occurred");
        model.addAttribute("exception", exception);

        // Return appropriate error template based on status code
        if (statusCode == 404) {
            return "error/404";
        } else if (statusCode == 403) {
            return "error/403";
        } else {
            return "error/error";
        }
    }
}
