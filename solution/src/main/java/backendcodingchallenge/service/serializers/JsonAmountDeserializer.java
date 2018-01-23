package backendcodingchallenge.service.serializers;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class JsonAmountDeserializer extends JsonDeserializer<Integer> {

    @Override
    public Integer deserialize(JsonParser jsonparser,
        DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {
        return (int)(jsonparser.getValueAsDouble()*100);
    }
}