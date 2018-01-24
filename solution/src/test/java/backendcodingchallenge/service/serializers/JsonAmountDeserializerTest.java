package backendcodingchallenge.service.serializers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JsonAmountDeserializerTest {

    private ObjectMapper mapper;

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void deserializeAmountOK() throws IOException {
        int deserializedAmount = deserialize("042");
        assertThat(deserializedAmount, is(equalTo(4200)));
    }

    @Test
    public void deserializeAmountRoundedOK() throws IOException {
        int deserializedAmount = deserialize("12.345");
        assertThat(deserializedAmount, is(equalTo(1235)));
    }

    @Test
    public void deserializeZeroAmountOK() throws IOException {
        int deserializedAmount = deserialize("0");
        assertThat(deserializedAmount, is(equalTo(0)));
    }

    @Test(expected = JsonMappingException.class) // Sadly, the workaround doesn't let us check the actual exception type
    public void deserializeInvalidAmountKO() throws IOException {
        deserialize("0,42");
    }

    @Test(expected = JsonMappingException.class)
    public void deserializeInvalidAmount2KO() throws IOException {
        deserialize("12..345");
    }

    @Test(expected = JsonMappingException.class)
    public void deserializeNullAmountKO() throws IOException {
        deserialize(null);
    }

    private int deserialize(String amount) throws IOException {
        return mapper.readValue("{\"amount\":\"" + amount + "\"}", TestObject.class).getAmount();
    }

    //Workaround to unit test the Jackson deserializer without having to provide a DeserializationContext
    private static class TestObject {

        @JsonDeserialize(using = JsonAmountDeserializer.class)
        private final Integer amount = null;

        public int getAmount() {
            return amount;
        }
    }
}
