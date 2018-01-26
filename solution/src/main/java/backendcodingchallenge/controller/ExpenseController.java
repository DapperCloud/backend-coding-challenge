package backendcodingchallenge.controller;

import backendcodingchallenge.controller.dto.ExpenseDto;
import backendcodingchallenge.model.Expense;
import backendcodingchallenge.service.ExpenseService;
import backendcodingchallenge.service.exceptions.ExternalApiInvalidAnswerException;
import backendcodingchallenge.service.exceptions.ExternalApiNotOkStatusException;
import backendcodingchallenge.service.exceptions.UnknownCurrencyException;
import backendcodingchallenge.utils.ExpenseUtils;
import javassist.tools.web.BadHttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class ExpenseController {

    @Autowired
    private ExpenseService service;

    @Autowired
    private ExpenseUtils utils;

    @RequestMapping("/app/expenses")
    public List<ExpenseDto> getAllExpenses() {
        return service.getAllExpenses()
                .stream()
                .map(e -> { ExpenseDto dto = new ExpenseDto(e); dto.setVat(utils.getVatFromAmount(e.getAmount())); return dto; })
                .collect(Collectors.toList());
    }

    @RequestMapping(value = "/app/expenses", method = RequestMethod.POST)
    public void createNewExpense(@RequestBody @Valid ExpenseDto expenseDto) throws BadHttpRequest {
        service.createExpense(Expense.builder().date(expenseDto.getDate()).amount(expenseDto.getAmount()).reason(expenseDto.getReason()).build(), expenseDto.getCurrency());
    }

    @ExceptionHandler(UnknownCurrencyException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public @ResponseBody UnknownCurrencyException handleException(UnknownCurrencyException e) {
        return e;
    }

    @ExceptionHandler(ExternalApiNotOkStatusException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ExternalApiNotOkStatusException handleException(ExternalApiNotOkStatusException e) {
        return e;
    }

    @ExceptionHandler(ExternalApiInvalidAnswerException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public @ResponseBody ExternalApiInvalidAnswerException handleException(ExternalApiInvalidAnswerException e) {
        return e;
    }


    @ExceptionHandler(SocketTimeoutException.class)
    @ResponseStatus(value = HttpStatus.REQUEST_TIMEOUT)
    public @ResponseBody SocketTimeoutException handleException(SocketTimeoutException e) {
        return new SocketTimeoutException("The external API couldn't be reached !");
    }
}
