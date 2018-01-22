package backendcodingchallenge.service;

import backendcodingchallenge.model.Expense;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class ExpenseService {

    public List<Expense> getAllExpenses() {
        return new ArrayList<>(Arrays.asList(
                new Expense(LocalDate.of(2015, 9, 15), 2090, "Reason 1"),
                new Expense(LocalDate.of(2018, 1, 7), 120, "Reason 2")));
    }
}
