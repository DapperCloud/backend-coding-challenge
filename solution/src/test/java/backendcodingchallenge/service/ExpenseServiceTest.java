package backendcodingchallenge.service;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.times;
import static org.mockito.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;

import backendcodingchallenge.dao.ExpenseRepository;
import backendcodingchallenge.model.Expense;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class ExpenseServiceTest {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(repository.save(any(Expense.class))).then(answer -> answer.getArguments()[0]);
    }

    @InjectMocks
    private ExpenseService service;

    @Mock
    private ExpenseRepository repository;

    @Test
    public void getAllExpensesOK() throws ParseException {
        List<Expense> allExpenses = asList(Expense.builder().date(sdf.parse("04/05/1993")).amount(4242).reason("My birthday !").build());
        when(repository.findAll()).thenReturn(allExpenses);
        List<Expense> returned = service.getAllExpenses();
        assertThat(returned, equalTo(allExpenses));
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void getAllExpensesWithEmptyRepoOK() {
        List<Expense> allExpenses = asList();
        when(repository.findAll()).thenReturn(allExpenses);
        List<Expense> returned = service.getAllExpenses();
        assertThat(returned, equalTo(allExpenses));
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void saveNewExpenseOK() throws ParseException {
        Expense newExpense = Expense.builder().date(sdf.parse("04/05/1993")).amount(4242).reason("My birthday !").build();
        service.saveExpense(newExpense);
        verify(repository, times(1)).save(newExpense);
        verifyNoMoreInteractions(repository);
    }
}
