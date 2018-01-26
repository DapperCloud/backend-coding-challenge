package backendcodingchallenge.service;

import static java.util.Arrays.asList;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.times;
import static org.mockito.Matchers.any;
import static org.hamcrest.Matchers.equalTo;
import static org.assertj.core.api.Assertions.assertThat;

import backendcodingchallenge.dao.ExpenseRepository;
import backendcodingchallenge.model.Expense;
import backendcodingchallenge.service.exceptions.UnknownCurrencyException;
import backendcodingchallenge.service.external.IRatesFetcher;
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

    @InjectMocks
    private ExpenseService service;

    @Mock
    private ExpenseRepository repository;

    @Mock
    private IRatesFetcher ratesFetcher;

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
        when(repository.save(any(Expense.class))).then(answer -> answer.getArguments()[0]);
    }

    @Test
    public void getAllExpensesOK() throws ParseException {
        List<Expense> allExpenses = asList(Expense.builder().date(sdf.parse("04/05/1993")).amount(4242).reason("My birthday !").build());
        when(repository.findAll()).thenReturn(allExpenses);
        List<Expense> returned = service.getAllExpenses();
        assertThat(returned).isEqualTo(allExpenses);
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void getAllExpensesWithEmptyRepoOK() {
        List<Expense> allExpenses = asList();
        when(repository.findAll()).thenReturn(allExpenses);
        List<Expense> returned = service.getAllExpenses();
        assertThat(returned).isEqualTo(allExpenses);
        verify(repository, times(1)).findAll();
        verifyNoMoreInteractions(repository);
    }

    @Test
    public void createExpenseDefaultCurrencyOK() throws ParseException {
        Expense newExpense = Expense.builder().date(sdf.parse("04/05/1993")).amount(4242).reason("My birthday !").build();
        service.createExpense(newExpense, "");
        verify(repository, times(1)).save(newExpense);
        verifyNoMoreInteractions(repository);
        verifyNoMoreInteractions(ratesFetcher);
    }

    @Test
    public void createExpenseEurCurrencyOK() throws ParseException {
        // Given
        when(ratesFetcher.getRateToGBP("EUR")).thenReturn(0.5);
        Expense newExpense = Expense.builder().date(sdf.parse("04/05/1993")).amount(99).reason("My birthday !").build();

        // When
        service.createExpense(newExpense, "EUR");

        // Then
        assertThat(newExpense.getAmount()).isEqualTo(50);
        verify(repository, times(1)).save(newExpense);
        verifyNoMoreInteractions(repository);
        verify(ratesFetcher, times(1)).getRateToGBP("EUR");
        verifyNoMoreInteractions(ratesFetcher);
    }

    @Test
    public void createExpenseUnknownCurrencyKO() throws ParseException {
        // Given
        when(ratesFetcher.getRateToGBP("USD")).thenThrow(UnknownCurrencyException.class);
        Expense newExpense = Expense.builder().date(sdf.parse("04/05/1993")).amount(99).reason("My birthday !").build();
        Exception thrownException = null;

        // When
        try {
            service.createExpense(newExpense, "USD");
        } catch (Exception ex) {
            thrownException = ex;
        }

        // Then
        assertThat(thrownException).isInstanceOf(UnknownCurrencyException.class);
        verifyNoMoreInteractions(repository);
    }
}
