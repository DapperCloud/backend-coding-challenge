package backendcodingchallenge.service;

import backendcodingchallenge.dao.ExpenseRepository;
import backendcodingchallenge.model.Expense;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    ExpenseRepository repository;

    public List<Expense> getAllExpenses() {
        /*return new ArrayList<>(Arrays.asList(
                new Expense(LocalDate.of(2015, 9, 15), 2090, "Reason 1"),
                new Expense(LocalDate.of(2018, 1, 7), 120, "Reason 2")));*/
        return repository.findAll();
    }
}
