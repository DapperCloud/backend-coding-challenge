package backendcodingchallenge.model;

import backendcodingchallenge.service.serializers.JsonAmountDeserializer;
import backendcodingchallenge.service.serializers.JsonAmountSerializer;
import backendcodingchallenge.service.serializers.JsonDateDeserializer;
import backendcodingchallenge.service.serializers.JsonDateSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

@JsonAutoDetect
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "DATE")
    @NotNull
    @JsonDeserialize(using = JsonDateDeserializer.class)
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date date;

    @Column
    @NotNull
    @Min(1)
    @JsonDeserialize(using = JsonAmountDeserializer.class)
    @JsonSerialize(using = JsonAmountSerializer.class)
    private int amount; //The amount in cents

    @Column
    @NotNull
    private String reason;

    @JsonSerialize(using= JsonAmountSerializer.class)
    public int getVat() {
        return (int)Math.round((double)amount/5);
    }
}
