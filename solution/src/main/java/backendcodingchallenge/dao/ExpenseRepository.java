package backendcodingchallenge.dao;

import backendcodingchallenge.model.Expense;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

/*
Just like the service layer, for the two use cases we have here, this repository is not really useful.
In the future, all custom querying and JPA-related concerns should go here.
 */

public interface ExpenseRepository extends CrudRepository<Expense, Long> {
    List<Expense> findAll();
}
