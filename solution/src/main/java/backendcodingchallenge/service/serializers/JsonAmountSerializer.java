package backendcodingchallenge.service.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Locale;

@Component
public class JsonAmountSerializer extends JsonSerializer<Integer> {

    @Override
    public void serialize(Integer amount, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeNumber(String.format(Locale.UK, "%.2f", ((double)amount)/100));
    }
}
