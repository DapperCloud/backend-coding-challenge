package backendcodingchallenge.service;

import backendcodingchallenge.dao.ExpenseRepository;
import backendcodingchallenge.model.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/*
As is, this class is not really useful, since the two use cases are quite straight forward and don't really
need any algorithmic logic ; it's here in prevision of further versions of the app, to be ready to have different layers
for different concerns.
 */

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
