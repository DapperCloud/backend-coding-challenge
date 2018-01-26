package backendcodingchallenge.controller;

import static java.util.Arrays.asList;

import static java.util.Collections.singletonList;

import static org.assertj.core.api.Assertions.assertThat;

import backendcodingchallenge.AppConfig;
import backendcodingchallenge.controller.dto.ExpenseDto;
import backendcodingchallenge.dao.ExpenseRepository;
import backendcodingchallenge.model.Expense;
import backendcodingchallenge.utils.ExpenseUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import org.springframework.boot.autoconfigure.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class ExpenseControllerIntegrationTest {

    @Autowired
    private ExpenseUtils utils;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ExpenseRepository expenseRepository;

    private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

    @Before
    public void setUp() {
        expenseRepository.deleteAll();
    }

    @Test
    public void getAllExpensesOK() throws ParseException {
        // Given
        Expense expense1 = Expense.builder().date(sdf.parse("04/05/1993")).amount(4242).reason("My birthday !").build();
        Expense expense2 = Expense.builder().date(sdf.parse("11/9/2001")).amount(10000000).reason("Budget for US government conspiracy").build();
        expenseRepository.save(asList(expense1, expense2));

        // When
        ResponseEntity<ExpenseDto[]> response = restTemplate.getForEntity("/app/expenses", ExpenseDto[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).usingElementComparatorIgnoringFields("vat").containsExactlyElementsOf(asList(new ExpenseDto(expense1), new ExpenseDto(expense2)));
    }

    @Test
    public void getAllExpensesCheckVatOK() throws ParseException {
        // Given
        Expense expense1 = Expense.builder().date(sdf.parse("04/05/1993")).amount(4242).reason("My birthday !").build();
        expenseRepository.save(singletonList(expense1));

        // When
        ResponseEntity<ExpenseDto[]> response = restTemplate.getForEntity("/app/expenses", ExpenseDto[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).usingElementComparatorIgnoringFields("vat").containsExactlyElementsOf(asList(new ExpenseDto(expense1)));
        assertThat(response.getBody()[0].getVat()).isEqualTo(utils.getVatFromAmount(expense1.getAmount()));
    }

    @Test
    public void getAllExpensesWithEmptyDbOK() {
        // When
        ResponseEntity<Expense[]> response = restTemplate.getForEntity("/app/expenses", Expense[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    public void getUnknownEndpointKO() {
        // When
        ResponseEntity<Void> response = restTemplate.exchange("/app/unknown", HttpMethod.GET, null, Void.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    public void saveNewExpenseAndRoundAmountAndEmptyCurrencyOK() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{ \"date\":\"04/5/1993\", \"amount\":12.349, \"reason\":\"My Birthday !\", \"currency\": \"\" }", headers);

        // When
        ResponseEntity<Void> response = restTemplate.exchange("/app/expenses", HttpMethod.POST, entity, Void.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Expense> allExpenses = expenseRepository.findAll();
        assertThat(allExpenses).hasSize(1);
        Expense savedExpense = allExpenses.get(0);
        assertThat(savedExpense.getAmount()).isEqualTo(1235);
        assertThat(savedExpense.getReason()).isEqualTo("My Birthday !");
        assertThat(sdf.format(savedExpense.getDate())).isEqualTo("04/05/1993");
    }

    @Test
    //TODO : Mock the external API for testing purposes ?
    public void saveNewExpenseAndConvertEurToGbpOK() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{ \"date\":\"04/5/1993\", \"amount\":12.349, \"reason\":\"My Birthday !\", \"currency\": \"EUR\" }", headers);

        // When
        ResponseEntity<Void> response = restTemplate.exchange("/app/expenses", HttpMethod.POST, entity, Void.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        List<Expense> allExpenses = expenseRepository.findAll();
        assertThat(allExpenses).hasSize(1);
        Expense savedExpense = allExpenses.get(0);
        assertThat(savedExpense.getAmount()).isNotEqualTo(1235);
        assertThat(savedExpense.getReason()).isEqualTo("My Birthday !");
        assertThat(sdf.format(savedExpense.getDate())).isEqualTo("04/05/1993");
    }

    @Test
    public void saveNewExpenseAndRoundAmountAndNoCurrencyKO() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{ \"date\":\"04/5/1993\", \"amount\":12.349, \"reason\":\"My Birthday !\" }", headers);

        // When
        ResponseEntity<Void> response = restTemplate.exchange("/app/expenses", HttpMethod.POST, entity, Void.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(expenseRepository.findAll()).hasSize(0);
    }

    @Test
    public void saveNewExpenseAndInvalidCurrencyKO() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{ \"date\":\"04/5/1993\", \"amount\":12.349, \"reason\":\"My Birthday !\", \"currency\": \"USD\" }", headers);

        // When
        ResponseEntity<Void> response = restTemplate.exchange("/app/expenses", HttpMethod.POST, entity, Void.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(expenseRepository.findAll()).hasSize(0);
    }

    @Test
    public void saveNewExpenseInvalidDateKO() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{ \"date\":\"31/02/1993\", \"amount\":12.34, \"reason\":\"My Birthday !\" }", headers);

        // When
        ResponseEntity<Void> response = restTemplate.exchange("/app/expenses", HttpMethod.POST, entity, Void.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(expenseRepository.findAll()).hasSize(0);
    }

    @Test
    public void saveNewExpenseInvalidAmountKO() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{ \"date\":\"04/5/1993\", \"amount\":12..34, \"reason\":\"My Birthday !\" }", headers);

        // When
        ResponseEntity<Void> response = restTemplate.exchange("/app/expenses", HttpMethod.POST, entity, Void.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(expenseRepository.findAll()).hasSize(0);
    }

    @Test
    public void saveNewExpenseInvalidJsonKO() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{ \"date\":\"04/5/1993\", \"amount\":12..34, \"reason\":\"My Birthday !\"", headers);

        // When
        ResponseEntity<Void> response = restTemplate.exchange("/app/expenses", HttpMethod.POST, entity, Void.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(expenseRepository.findAll()).hasSize(0);
    }

    @Test
    public void saveNewExpenseAmountMissingKO() {
        // Given
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<>("{ \"date\":\"04/5/1993\", \"reason\":\"My Birthday !\" }", headers);

        // When
        ResponseEntity<Void> response = restTemplate.exchange("/app/expenses", HttpMethod.POST, entity, Void.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(expenseRepository.findAll()).hasSize(0);
    }
}
