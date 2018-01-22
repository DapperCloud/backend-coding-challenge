package backendcodingchallenge.controller;

import backendcodingchallenge.model.Expense;
import backendcodingchallenge.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class ExpenseController {

    @Autowired
    private ExpenseService service;

    @RequestMapping("/app/expenses")
    List<Expense> getAllExpenses() {
        return service.getAllExpenses();
    }
}
