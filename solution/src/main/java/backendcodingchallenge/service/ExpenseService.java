package backendcodingchallenge.service;

import backendcodingchallenge.dao.ExpenseRepository;
import backendcodingchallenge.model.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository repository;

    public List<Expense> getAllExpenses() {
        return repository.findAll();
    }

    public void saveExpense(Expense expense) {
        repository.save(expense);
    }


}
