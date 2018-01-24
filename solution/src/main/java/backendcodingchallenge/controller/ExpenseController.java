package backendcodingchallenge.controller;

import backendcodingchallenge.model.Expense;
import backendcodingchallenge.service.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
public class ExpenseController {

    @Autowired
    private ExpenseService service;

    @RequestMapping("/app/expenses")
    public List<Expense> getAllExpenses() {
        return service.getAllExpenses();
    }

    @RequestMapping(value = "/app/expenses", method = RequestMethod.POST)
    public void createNewExpense(@Valid @RequestBody Expense expense) {
        service.saveExpense(expense);
    }

}
