package backendcodingchallenge.service.serializers;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class JsonDateDeserializerTest {

    private ObjectMapper mapper;
    private SimpleDateFormat sdf = new SimpleDateFormat(JsonDateSerializer.FORMAT);

    @Before
    public void setup() {
        mapper = new ObjectMapper();
    }

    @Test
    public void deserializeDateOK() throws IOException, ParseException {
        String dateStr = "04/05/1993";
        Date deserializedDate = deserialize(dateStr);
        assertThat(deserializedDate, is(equalTo(sdf.parse(dateStr))));
    }

    @Test
    public void deserializeDate2OK() throws IOException, ParseException {
        String dateStr = "4/5/1993";
        Date deserializedDate = deserialize(dateStr);
        assertThat(deserializedDate, is(equalTo(sdf.parse(dateStr))));
    }

    @Test(expected = JsonMappingException.class) // Sadly, the workaround doesn't let us check the actual exception type
    public void deserializeDateInvalidFormatKO() throws IOException {
        deserialize("31/02/2010");
    }

    @Test(expected = JsonMappingException.class)
    public void deserializeDateInvalidFormat2KO() throws IOException {
        deserialize("01/13/2000");
    }

    @Test(expected = JsonMappingException.class)
    public void deserializeNullDateKO() throws IOException {
        deserialize(null);
    }

    private Date deserialize(String date) throws IOException {
        return mapper.readValue("{\"date\":\"" + date + "\"}", TestObject.class).getDate();
    }

    //Workaround to unit test the Jackson deserializer without having to provide a DeserializationContext
    private static class TestObject {

        @JsonDeserialize(using = JsonDateDeserializer.class)
        private final Date date = null;

        public Date getDate() {
            return date;
        }
    }
}
