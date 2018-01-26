package backendcodingchallenge.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ExpenseUtils {

    @Value("${config.business.vat-rate}")
    private double vatRate;

    public int getVatFromAmount(int amount) {
        return (int)Math.round((double)amount*vatRate);
    }
}
