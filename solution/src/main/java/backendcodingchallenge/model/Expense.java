package backendcodingchallenge.model;

import backendcodingchallenge.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import java.time.LocalDate;

@JsonAutoDetect
public class Expense {

    private LocalDate date;
    private int value;
    private String reason;

    public Expense(LocalDate date, int value, String reason) {
        this.date = date;
        this.value = value;
        this.reason = reason;
    }

    @JsonSerialize(using= JsonDateSerializer.class)
    public LocalDate getDate() {
        return date;
    }

    public int getValue() {
        return value;
    }

    public String getReason() {
        return reason;
    }
}
