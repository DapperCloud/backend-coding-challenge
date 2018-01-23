package backendcodingchallenge.service.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JsonAmountSerializer extends JsonSerializer<Integer> {

    @Override
    public void serialize(Integer amount, JsonGenerator gen, SerializerProvider provider)
            throws IOException, JsonProcessingException {
        gen.writeNumber(((double)amount)/100);
    }
}
