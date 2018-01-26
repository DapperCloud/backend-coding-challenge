package backendcodingchallenge.controller.dto;

import backendcodingchallenge.service.serializers.JsonAmountDeserializer;
import backendcodingchallenge.service.serializers.JsonDateDeserializer;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDto {
    @NotNull
    @JsonDeserialize(using = JsonDateDeserializer.class)
    private Date date;

    @NotNull
    @Min(1)
    @JsonDeserialize(using = JsonAmountDeserializer.class)
    private int amount; //The amount in cents

    @NotNull
    private String reason;

    @NotNull
    private String currency;
}
