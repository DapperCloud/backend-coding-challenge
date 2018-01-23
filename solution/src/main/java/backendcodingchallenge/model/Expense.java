package backendcodingchallenge.model;

import backendcodingchallenge.JsonAmountSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import lombok.NoArgsConstructor;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Date;

@JsonAutoDetect
@Entity
@Data
@NoArgsConstructor
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(columnDefinition = "DATE")
    private Date date;

    @Column
    private int amount;

    @Column
    private String reason;

    @JsonSerialize(using= JsonAmountSerializer.class)
    public int getAmount() {
        return amount;
    }

    @JsonSerialize(using= JsonAmountSerializer.class)
    public int getVat() {
        return (int)Math.round((double)amount/5);
    }
}
