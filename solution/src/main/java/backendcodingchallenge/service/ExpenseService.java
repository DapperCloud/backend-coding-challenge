package backendcodingchallenge.service;

import backendcodingchallenge.dao.ExpenseRepository;
import backendcodingchallenge.model.Expense;
import backendcodingchallenge.service.external.IRatesFetcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository repository;

    @Autowired
    private IRatesFetcher ratesFetcher;

    public List<Expense> getAllExpenses() {
        return repository.findAll();
    }

    public void createExpense(Expense expense, String currency) {
        if(!currency.isEmpty()) {
            double rate = ratesFetcher.getRateToGBP(currency);
            expense.setAmount((int)Math.round(rate*expense.getAmount()));
        }
        repository.save(expense);
    }
}
