package backendcodingchallenge.controller.dto;

import backendcodingchallenge.model.Expense;
import backendcodingchallenge.service.serializers.JsonAmountDeserializer;
import backendcodingchallenge.service.serializers.JsonAmountSerializer;
import backendcodingchallenge.service.serializers.JsonDateDeserializer;
import backendcodingchallenge.service.serializers.JsonDateSerializer;
import backendcodingchallenge.utils.ExpenseUtils;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonAutoDetect
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExpenseDto {
    @NotNull
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date date;

    @NotNull
    @Min(1)
    @JsonDeserialize(using = JsonAmountDeserializer.class)
    @JsonSerialize(using = JsonAmountSerializer.class)
    private int amount; //The amount in cents

    @NotNull
    private String reason;

    @NotNull
    private String currency;

    @JsonDeserialize(using = JsonAmountDeserializer.class)
    @JsonSerialize(using= JsonAmountSerializer.class)
    private int vat;

    public ExpenseDto(Expense expense) {
        date = expense.getDate();
        amount = expense.getAmount();
        reason = expense.getReason();
        currency = "";
        vat = 0;
    }
}
